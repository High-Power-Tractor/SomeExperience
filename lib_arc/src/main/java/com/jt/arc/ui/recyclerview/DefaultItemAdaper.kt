package com.jt.arc.ui.recyclerview

/**
 * 其他的ItemAdapter匹配不上，就使用这个兜底的ItemAdapter。
 * 该ItemAdapter只能注册一次。
 */
abstract class DefaultItemAdaper<T>: ItemAdapter<T>() {
    final override fun isSameViewType(position: Int): Boolean {
        return true
    }
}