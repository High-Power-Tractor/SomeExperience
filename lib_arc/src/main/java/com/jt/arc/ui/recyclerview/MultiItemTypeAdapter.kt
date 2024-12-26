package com.jt.arc.ui.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 用于多ViewType列表显示。目的：让每个ViewType对应处理逻辑能相互隔离开。
 * 每个ViewType对应一个ItemAdapter，RecyclerView.Adapter中的onCreateViewHolder，onBindViewHolder等实现都指派给对应的ItemAdapter执行。
 */
class MultiItemTypeAdapter<T>(vararg itemAdapters: ItemAdapter<T, *>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val datas: ArrayList<T> = arrayListOf()
    private val itemAdapterMap = LinkedHashMap<Int, ItemAdapter<T, *>>()
    private val posAndViewTypeMap = HashMap<Int, Int>()

    init {
        for (item in itemAdapters) {
            registerItemAdapter(item)
        }
    }

    fun updateData(data: List<T>){
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    fun getData(): List<T>{
        return datas
    }

    fun registerItemAdapter(itemAdapter: ItemAdapter<T, *>){
        if(itemAdapterMap.containsKey(itemAdapter.getItemViewType())){
            throw Exception("already exist viewType ${itemAdapter.getItemViewType()}")
        }
        itemAdapterMap[itemAdapter.getItemViewType()] = itemAdapter
        itemAdapter.attach(this)

        //确保DefaultItemAdaper只注册一次，且排在最后面
        var defaultItemAdapterKey = -1
        var defaultItemAdapterSize = 0
        for (item in itemAdapterMap.entries) {
            if(item.value is DefaultItemAdaper) {
                defaultItemAdapterKey = item.key
                defaultItemAdapterSize++
            }
        }
        if(defaultItemAdapterSize > 1) throw Exception("DefaultItemAdaper register more than once")
        itemAdapterMap.remove(defaultItemAdapterKey)?.let { defaultItemAdapter ->
            itemAdapterMap[defaultItemAdapterKey] = defaultItemAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return itemAdapterMap[viewType]?.onCreateViewHolder(parent)
            ?: throw Exception("no exist viewType ${viewType}")
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType =  posAndViewTypeMap[position]?: throw Exception("no exist position ${position}")
        val itemAdapter = itemAdapterMap[viewType]?: throw Exception("no exist viewType ${viewType}")
        itemAdapter.onBindViewHolder_inner(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        for (item in itemAdapterMap.values) {
            if(item.isSameViewType(position)) {
                val viewType = item.getItemViewType()
                posAndViewTypeMap[position] = viewType
                return viewType
            }
        }
        return super.getItemViewType(position)
    }
}