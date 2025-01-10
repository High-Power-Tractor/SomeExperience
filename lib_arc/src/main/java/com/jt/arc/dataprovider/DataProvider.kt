package com.jt.arc.dataprovider

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.CountDownLatch

class DataProvider<T>{
    private var mProvider: Provider<T>? = null
    val mainHandler = Handler(Looper.getMainLooper())


    fun provide(lifecycleOwner: LifecycleOwner, provider: Provider<T>){
        this.mProvider = provider
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver{
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                lifecycleOwner.lifecycle.removeObserver(this)
                mProvider = null
            }
        })
    }

    fun getData(): T?{
        val isUiThread = Looper.myLooper() == Looper.getMainLooper() //保证mProvider?.onData()在主线程中执行
        if(isUiThread){
            return mProvider?.onData()
        }else{
            val latch = CountDownLatch(1)
            var result: T? = null
            mainHandler.post {
                result = mProvider?.onData()
                latch.countDown()
            }
            latch.await()
            return result
        }
    }
}