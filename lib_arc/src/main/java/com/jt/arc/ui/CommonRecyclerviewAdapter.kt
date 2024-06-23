package com.jt.arc.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class CommonRecyclerviewAdapter<T>(val recyclerView: RecyclerView): RecyclerView.Adapter<CommonViewHolder>() {
    private val viewTypeMap = HashMap<Int, ViewHolderConfig>()
    private val positionMap = HashMap<Int, ViewHolderConfig>()

    init {
        (recyclerView.layoutManager as? GridLayoutManager)?.let {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return getSpanSizeConfig(position)
                }
            }
        }
    }

    abstract fun onCreateViewHolderConfig(position: Int): ViewHolderConfig

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return viewTypeMap[viewType]?.createTask?.invoke(parent)?:throw Exception("config error")
    }

    final override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bindData(position)
    }

    final  override fun getItemViewType(position: Int): Int {
        return getConfigByPostion(position).viewType
    }

    private fun getSpanSizeConfig(position: Int): Int{
        return getConfigByPostion(position).spanSize
    }

    private fun getConfigByPostion(position: Int): ViewHolderConfig {
        return positionMap[position]?: onCreateViewHolderConfig(position).also {
            if(!viewTypeMap.containsKey(it.viewType)) {
                viewTypeMap[it.viewType] = it
            }
            if(!positionMap.containsKey(position)) {
                positionMap[position] = it
            }
        }
    }
}

abstract class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    constructor(layoutId: Int, parent: ViewGroup)
            : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    abstract fun bindData(position: Int)
}

class ViewHolderConfig(val viewType: Int, val createTask: (parent: ViewGroup)-> CommonViewHolder, val spanSize: Int = 1)

