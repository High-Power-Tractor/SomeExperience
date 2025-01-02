package com.jt.arc.livedata;


import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;

/**
 * 作用：数据不更新情况下，对一个订阅者 数据只分发一次（同一个订阅者的再次订阅不会分发旧数据）；
 *      既实现了一次数据更新只分发一次，也保留了livedata的粘性特性。
 * 原理：每一个订阅者分配一个固定唯一的ID，数据分发后记录该订阅者ID。数据分发时 查询当前订阅者ID如果已经分发过了，就不分发了。
 */
public class SingleLiveDataSticky<T> {
    private final MutableLiveData<SingleEvent<T>> realLiveData = new MutableLiveData<>();

    private final HashMap<Observer<T>, TimeObserverWrapper> foreverObserverMaps = new HashMap<>();

    /**
     * 当只有一个Observe订阅者时 可以使用该方法
     */
    public void observeDefaultID(@NonNull LifecycleOwner owner,  @NonNull Observer<T> observer) {
        observe(owner, Integer.MAX_VALUE, observer);
    }

    public void observe(@NonNull LifecycleOwner owner, int observeID, @NonNull Observer<T> observer) {
        realLiveData.observe(owner, new TimeObserverWrapper(observeID, observer));
    }


    public void observeForever(int observePos, @NonNull Observer<T> observer) {
        TimeObserverWrapper wrapper = new TimeObserverWrapper(observePos, observer);
        foreverObserverMaps.put(observer, wrapper);
        realLiveData.observeForever(wrapper);
    }

    public void removeObserver(@NonNull Observer<T> observer) {
        TimeObserverWrapper observerWrapper = foreverObserverMaps.get(observer);
        if(observerWrapper == null) return;
        realLiveData.removeObserver(observerWrapper);
    }

    public void setValue(T value) {
        realLiveData.setValue(new SingleEvent<>(value));
    }

    public void postValue(T value) {
        realLiveData.postValue(new SingleEvent<>(value));
    }

    /**
     * 原始Observer代理类，控制数据分发逻辑
     */
    private class TimeObserverWrapper implements Observer<SingleEvent<T>>{
        private final int observeId; //每一个订阅者对应唯一的订阅ID
        private final Observer<T> hostObserver;

        public TimeObserverWrapper(int observePos, Observer<T> observer){
            this.observeId = observePos;
            hostObserver = observer;
        }


        @Override
        final public void onChanged(SingleEvent<T>  t) {
            if(t == null) return;
            if(hostObserver != null && !t.isIdConsumed(observeId)){ //没有被分发过的订阅者 才进行数据分发
                hostObserver.onChanged(t.getValue());
                t.idConsume(observeId);
            }
        }

    }

    /**
     * 原始数据代理类，记录订阅者的分发状态。
     */
    private static class  SingleEvent<T> {
        private final HashMap<Integer, Boolean> id_consumeStateMap = new HashMap<>(); //记录订阅者的分发状态

        private final T value;

        public void idConsume(int id){
            id_consumeStateMap.put(id, true);
        }

        public boolean isIdConsumed(int id){
            return Boolean.TRUE.equals(id_consumeStateMap.get(id));
        }

        public SingleEvent(T value){
            this.value = value;
        }

        public T getValue(){
            return value;
        }
    }
}
