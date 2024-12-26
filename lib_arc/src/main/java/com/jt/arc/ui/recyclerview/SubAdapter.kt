package com.jt.arc.ui.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class SubAdapter<T, VH : RecyclerView.ViewHolder> {
    private var hostRecyclerViewAdapter: MultiItemTypeAdapter<T>? = null

    abstract fun onCreateViewHolder(parent: ViewGroup): VH

    abstract fun onBindViewHolder(holder: VH, position: Int)

    abstract fun getItemViewType(): Int

    abstract fun isSameViewType(position: Int): Boolean

    internal fun onBindViewHolder_inner(holder: RecyclerView.ViewHolder, position: Int){
        onBindViewHolder(holder as VH, position)
    }

    internal fun attach(apdater: MultiItemTypeAdapter<T>){
        hostRecyclerViewAdapter = apdater
    }

    fun getItemCount(): Int {
        return hostRecyclerViewAdapter?.itemCount?:0
    }

    fun getData():List<T>{
        return hostRecyclerViewAdapter?.getData()?: arrayListOf()
    }
}