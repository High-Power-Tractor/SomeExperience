package com.jt.arc.ui.recyclerview

import android.view.ViewGroup
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
    private val position_viewTypeMap = HashMap<Int, Int>()

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
     * 注册SubAdapter，每个viewtype对应一个SubAdapter实例。
     */
    fun registerSubAdapter(subAdapter: SubAdapter<T, *>){
        if(viewType_subAdapterMap.containsKey(subAdapter.getItemViewType())){
            throw Exception("already exist viewType ${subAdapter.getItemViewType()}")
        }
        viewType_subAdapterMap[subAdapter.getItemViewType()] = subAdapter
        subAdapter.attach(this)

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
        val viewType =  position_viewTypeMap[position]?: throw Exception("no exist position ${position}")
        val subAdapter = viewType_subAdapterMap[viewType]?: throw Exception("no exist viewType ${viewType}")
        subAdapter.onBindViewHolder_inner(holder, position) //具体任务指派给对应的subAdapter执行
    }

    override fun getItemViewType(position: Int): Int {
        for (item in viewType_subAdapterMap.values) {
            if(item.isSameViewType(position)) {
                val viewType = item.getItemViewType()
                position_viewTypeMap[position] = viewType
                return viewType
            }
        }
        return super.getItemViewType(position)
    }
}