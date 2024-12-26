package com.jt.arc.ui.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MultiItemTypeAdapter<T>: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val datas: ArrayList<T> = arrayListOf()
    private val itemAdapterMap = LinkedHashMap<Int, ItemAdapter<T, *>>()
    private val posAndViewTypeMap = HashMap<Int, Int>()

    fun updateData(data: List<T>){
        datas.clear()
        datas.addAll(data)
        for (item in itemAdapterMap.values) {
            item.updateData(datas)
        }
        notifyDataSetChanged()
    }

    fun registerItemAdapter(itemAdapter: ItemAdapter<T, *>){
        if(itemAdapterMap.containsKey(itemAdapter.getItemViewType())){
            throw Exception("already exist viewType ${itemAdapter.getItemViewType()}")
        }
        itemAdapterMap[itemAdapter.getItemViewType()] = itemAdapter

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
        return itemAdapterMap[viewType]?.onCreateViewHolder(parent, viewType)
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