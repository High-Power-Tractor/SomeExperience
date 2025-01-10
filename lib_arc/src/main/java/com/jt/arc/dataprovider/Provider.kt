package com.jt.arc.dataprovider

fun interface Provider<T>{
    fun onData(): T //运行在主线程之中
}