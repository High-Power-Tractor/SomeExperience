package com.jt.arc.ui.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class ItemAdapter<T, VH : RecyclerView.ViewHolder> {
    protected val datas: ArrayList<T> = arrayListOf()

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindViewHolder(holder: VH, position: Int)

    abstract fun getItemViewType(): Int

    abstract fun isSameViewType(position: Int): Boolean

    internal fun onBindViewHolder_inner(holder: RecyclerView.ViewHolder, position: Int){
        onBindViewHolder(holder as VH, position)
    }

    fun getItemCount(): Int {
        return datas.size
    }

    fun updateData(data: List<T>){
        datas.clear()
        datas.addAll(data)
    }
}