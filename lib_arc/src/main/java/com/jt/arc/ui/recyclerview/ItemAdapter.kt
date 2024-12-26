package com.jt.arc.ui.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class ItemAdapter<T> {
    protected val datas: ArrayList<T> = arrayListOf()

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    abstract fun getItemViewType(): Int

    abstract fun isSameViewType(position: Int): Boolean

    fun getItemCount(): Int {
        return datas.size
    }

    fun updateData(data: List<T>){
        datas.clear()
        datas.addAll(data)
    }
}