package com.jt.arc.livedata;


import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;

/**
 * 作用：数据不更新情况下，一个订阅位置数据只分发一次（同一个位置的再次订阅不会分发就数据）；
 *      既实现了一次数据更新只分发一次，也保留了livedata的粘性特性。
 * 原理：每一个订阅的地方分配一个固定的pos，数据分发后记录该位置。数据分发时 判断当前位置如果已经分发过了，就不分发了。
 */
public class SingleLiveDataSticky<T> {
    private final MutableLiveData<SingleEvent<T>> realLiveData = new MutableLiveData<>();

    private final HashMap<Observer<T>, TimeObserverWrapper> foreverObserverMaps = new HashMap<>();

    /**
     * 当只有一个Observe订阅者时 可以使用该方法
     */
    public void observeDefaultPosition(@NonNull LifecycleOwner owner,  @NonNull Observer<T> observer) {
        observe(owner, Integer.MAX_VALUE, observer);
    }

    public void observe(@NonNull LifecycleOwner owner, int observePos, @NonNull Observer<T> observer) {
        realLiveData.observe(owner, new TimeObserverWrapper(observePos, observer));
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
        private final int observePos;
        private final Observer<T> hostObserver;

        public TimeObserverWrapper(int observePos, Observer<T> observer){
            this.observePos = observePos;
            hostObserver = observer;
        }


        @Override
        final public void onChanged(SingleEvent<T>  t) {
            if(t == null) return;
            if(hostObserver != null && !t.isPosConsumed(observePos)){ //没有被消费过的订阅位置 才进行数据分发
                hostObserver.onChanged(t.getValue());
                t.posConsume(observePos);
            }
        }

    }

    /**
     * 原始数据分发类，记录哪些订阅位置分发过。
     */
    private static class  SingleEvent<T> {
        private final HashMap<Integer, Boolean> pos_consumeStateMap = new HashMap<>();

        private final T value;

        public void posConsume(int pos){
            pos_consumeStateMap.put(pos, true);
        }

        public boolean isPosConsumed(int pos){
            return Boolean.TRUE.equals(pos_consumeStateMap.get(pos));
        }

        public SingleEvent(T value){
            this.value = value;
        }

        public T getValue(){
            return value;
        }
    }
}
