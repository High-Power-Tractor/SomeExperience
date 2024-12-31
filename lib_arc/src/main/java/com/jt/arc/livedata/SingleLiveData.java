package com.jt.arc.livedata;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;

/**
 *  作用：除去MutableLiveData自带的粘性事件特性，支持多Observe监听，使用方法和MutableLiveData保持一致。
 *  原理：通过记录订阅时间点、数据更新时间点，订阅操作之后更新的数据才会分发。
 */
public class SingleLiveData<T> {
    private final MutableLiveData<SingleEvent<T>> realLiveData = new MutableLiveData<>();

    private final HashMap<Observer<T>, TimeObserverWrapper> foreverObserverMaps = new HashMap<>();

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        realLiveData.observe(owner, new TimeObserverWrapper(observer));
    }


    public void observeForever(@NonNull Observer<T> observer) {
        TimeObserverWrapper wrapper = new TimeObserverWrapper(observer);
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
     *  原始Observer的代理类，多增加一个createTime字段，用于记录订阅时间
     */
    private class TimeObserverWrapper implements Observer<SingleEvent<T>>{
        private final long createTime = SystemClock.elapsedRealtime(); //记录当前订阅操作时间（订阅时创建该对象）
        private final Observer<T> hostObserver;

        public TimeObserverWrapper(Observer<T> observer){
            hostObserver = observer;
        }


        @Override
        final public void onChanged(SingleEvent<T>  t) {
            if(t == null) return;
            if(createTime <= t.createTime){
                if(hostObserver != null){
                    hostObserver.onChanged(t.getValue());
                }
            }
        }

    }

    /**
     * 原始数据代理类，多增加一个createTime字段，用于记录数据创建/更新的时间。
     */
    private static class  SingleEvent<T> {
        protected long createTime = SystemClock.elapsedRealtime(); //记录数据更新时间（数据更新时创建该对象）

        private final T value;

        public SingleEvent(T value){
            this.value = value;
        }

        public T getValue(){
            return value;
        }
    }
}
