package com.jt.arc.ui.recyclerview

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView


/**
 * 用于多ViewType列表显示。目的：让每个ViewType对应处理逻辑能相互隔离开。
 * 每个ViewType对应一个SubAdapter实例，RecyclerView.Adapter中的onCreateViewHolder，onBindViewHolder等实现都指派给对应的SubAdapter执行。
 * Params:
 *  subAdapters - 构造函数中传入需要注册的SubAdapter
 */
class MultiItemTypeAdapter<T>(vararg subAdapters: SubAdapter<T, *>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val datas: ArrayList<T> = arrayListOf()
    private val viewType_subAdapterMap = LinkedHashMap<Int, SubAdapter<T, *>>()
    private val position_subAdapterMap = HashMap<Int, SubAdapter<T, *>>()
    private val existingSubAdapterClasses: HashSet<Class<*>> = HashSet()


    init {
        for (item in subAdapters) {
            registerSubAdapter(item)
        }
    }

    /**
     * 同步数据到Adapter，之后调用者还需要关注notifayxxxx()方法的调用
     */
    fun syncData(data: List<T>){
        datas.clear()
        datas.addAll(data)
    }

    fun getData(): List<T>{
        return datas
    }

    /**
     * 设置GridLayoutManager的Spansize；需要继承SubAdapter实现getSpanSize方法
     */
    fun configSpanSizeLookup(layoutManager: GridLayoutManager){
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val subAdapter = findSubAdapter(position)
                return subAdapter.getSpanSize(position)
            }
        }
    }

    /**
     * 注册SubAdapter，每个viewtype对应一个SubAdapter实例。
     */
    fun registerSubAdapter(subAdapter: SubAdapter<T, *>){
        if(existingSubAdapterClasses.contains(subAdapter.javaClass)){
            throw Exception("one subAdapter register more than once!")
        }
        existingSubAdapterClasses.add(subAdapter.javaClass)
        val newViewTypeId = existingSubAdapterClasses.size //每个SubAdapter只能注册一次，所以使用当前SubAdapter个数作为ViewType ID值。
        subAdapter.attach(this, newViewTypeId)
        viewType_subAdapterMap[newViewTypeId] = subAdapter

        //确保DefaultSubAdapter只注册一次，且排在最后面
        var defaultSubAdapterKey = -1
        var defaultSubAdapterSize = 0
        for (item in viewType_subAdapterMap.entries) {
            if(item.value is DefaultSubAdaper) {
                defaultSubAdapterKey = item.key
                defaultSubAdapterSize++
            }
        }
        if(defaultSubAdapterSize > 1) throw Exception("DefaultSubAdapter register more than once")
        viewType_subAdapterMap.remove(defaultSubAdapterKey)?.let { defaultSubAdapter ->
            viewType_subAdapterMap[defaultSubAdapterKey] = defaultSubAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return viewType_subAdapterMap[viewType]?.onCreateViewHolder(parent) //具体任务指派给对应的subAdapter执行
            ?: throw Exception("no exist viewType ${viewType}")
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val subAdapter =  position_subAdapterMap[position]
            ?: throw Exception("onBindViewHolder() not found subadpter at pos ${position}")
        subAdapter.onBindViewHolder_inner(holder, position) //具体任务指派给对应的subAdapter执行
    }

    override fun getItemViewType(position: Int): Int {
        val subAdapter = findSubAdapter(position)
        position_subAdapterMap[position] = subAdapter
        return subAdapter.getItemViewType()
    }

    private fun findSubAdapter(position: Int): SubAdapter<T, *>{
        for (item in viewType_subAdapterMap.values) {
            if(item.isSameViewType(position)) {
                return item
            }
        }
        throw Exception("not found subadpter at pos ${position}")
    }
}