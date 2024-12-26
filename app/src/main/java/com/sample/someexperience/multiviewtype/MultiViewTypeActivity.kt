package com.sample.someexperience.multiviewtype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jt.arc.ui.recyclerview.DefaultSubAdaper
import com.jt.arc.ui.recyclerview.MultiItemTypeAdapter
import com.jt.arc.ui.recyclerview.SubAdapter
import com.sample.someexperience.R
import kotlinx.android.synthetic.main.activity_multi_view_type.*

class MultiViewTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_view_type)
        initView()
    }

    private fun initView(){
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val adapter = MultiItemTypeAdapter<ValueBean>(NormalSubAdapter(), HeaderSubAdapter()) //构造函数中注册
        adapter.registerSubAdapter(ADSubAdapter()) //调用方法注册
        adapter.registerSubAdapter(FooterSubAdapter())
        adapter.syncData(genListData())
        recycler_view.adapter = adapter
    }

    //Header布局相关的所有逻辑都封装到这个独立的类中。
    class HeaderSubAdapter: SubAdapter<ValueBean, HeaderSubAdapter.HeaderItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup): HeaderItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_multi_viewtype_header, parent, false)
            return HeaderItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HeaderItemViewHolder, position: Int) {
            holder.contentView.text = "this is header"
        }

        //返回ViewType id，需求确保不同的SubAdapter对应的ID也必须不同；
        //建议使用布局ID 或 自定义ID资源，可以保证不重复；
        override fun getItemViewType(): Int {
            return R.layout.item_multi_viewtype_header
        }

        //判断列表中position这个位置是否Header item
        override fun isSameViewType(position: Int): Boolean {
            val listData = getData() //SubAdapter中可以通过getData()方法获取列表数据
            return listData[position] is HeaderWrapper
        }

        //Header布局对应的ViewHolder
        class HeaderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val contentView = itemView.findViewById<TextView>(R.id.content_view)
        }

    }

    //Footer布局
    class FooterSubAdapter: SubAdapter<ValueBean, FooterSubAdapter.FooterItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup): FooterItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_multi_viewtype_footer, parent, false)
            return FooterItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: FooterItemViewHolder, position: Int) {
            holder.contentView.text = "this is Footer"
        }

        override fun getItemViewType(): Int {
            return R.layout.item_multi_viewtype_footer
        }

        override fun isSameViewType(position: Int): Boolean {
            return getData()[position] is FooterWrapper
        }

        class FooterItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val contentView = itemView.findViewById<TextView>(R.id.content_view)
        }

    }

    //广告布局
    class ADSubAdapter: SubAdapter<ValueBean, ADSubAdapter.ADItemViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup):ADItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_multi_viewtype_ad, parent, false)
            return ADItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ADItemViewHolder, position: Int) {
            holder.contentView.text = "this is AD Unit"
        }

        override fun getItemViewType(): Int {
            return R.layout.item_multi_viewtype_ad
        }

        override fun isSameViewType(position: Int): Boolean {
            return getData()[position] is ADWrapper
        }

        class ADItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val contentView = itemView.findViewById<TextView>(R.id.content_view)
        }

    }

    //常规布局
    //DefaultSubAdapter默认可以匹配任何一个position，所以不需要实现isSameViewType()方法;
    //匹配规则：优先匹配其他SubAdapter，都匹配不上在用这个兜底。
    //通常用于其他ViewType都有详细的匹配规则，但是自己的匹配规则不明确或繁杂（先把其他的都排除，剩下的就是自己的）
    class NormalSubAdapter: DefaultSubAdaper<ValueBean, NormalSubAdapter.NormalItemViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup): NormalItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_multi_viewtype_layout_normal, parent, false)
            return NormalItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: NormalItemViewHolder, position: Int) {
            holder.contentView.text = "this is normal ${getData()[position].content} data"
        }

        override fun getItemViewType(): Int {
            return R.layout.item_multi_viewtype_layout_normal
        }

        class NormalItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val contentView = itemView.findViewById<TextView>(R.id.content_view)
        }

    }

    //模拟列表数据
    private fun genListData(): List<ValueBean>{
        val result = arrayListOf<ValueBean>()
        result.add(HeaderWrapper())
        result.add(ValueBean("aaa"))
        result.add(ValueBean("bbb"))
        result.add(ValueBean("ccc"))
        result.add(ADWrapper())
        result.add(ValueBean("ddd"))
        result.add(ValueBean("eee"))
        result.add(ValueBean("fff"))
        result.add(ValueBean("ggg"))
        result.add(ValueBean("hhh"))
        result.add(FooterWrapper())
        return result
    }

}