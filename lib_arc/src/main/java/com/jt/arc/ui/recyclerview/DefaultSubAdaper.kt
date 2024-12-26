package com.jt.arc.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView

/**
 * 其他的SubAdapter匹配不上，就使用这个兜底的SubAdapter。
 * 该SubAdapter只能注册一次。
 */
abstract class DefaultSubAdaper<T, VH: RecyclerView.ViewHolder>: SubAdapter<T, VH>() {
    final override fun isSameViewType(position: Int): Boolean {
        return true
    }
}