package com.jt.arc.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView

/**
 * 其他的ItemAdapter匹配不上，就使用这个兜底的ItemAdapter。
 * 该ItemAdapter只能注册一次。
 */
abstract class DefaultItemAdaper<T, VH: RecyclerView.ViewHolder>: ItemAdapter<T, VH>() {
    final override fun isSameViewType(position: Int): Boolean {
        return true
    }
}