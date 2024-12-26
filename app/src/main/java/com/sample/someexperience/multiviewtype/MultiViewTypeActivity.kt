package com.sample.someexperience.multiviewtype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jt.arc.ui.recyclerview.DefaultItemAdaper
import com.jt.arc.ui.recyclerview.ItemAdapter
import com.jt.arc.ui.recyclerview.MultiItemTypeAdapter
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

        val adapter = MultiItemTypeAdapter<ValueBean>()
        adapter.registerItemAdapter(NormalItemAdapter())
        adapter.registerItemAdapter(HeaderItemAdapter())
        adapter.registerItemAdapter(FooterItemAdapter())
        adapter.registerItemAdapter(ADItemAdapter())
        adapter.updateData(genListData())
        recycler_view.adapter = adapter
    }

    class HeaderItemAdapter: ItemAdapter<ValueBean, HeaderItemAdapter.HeaderItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_multi_viewtype_header, parent, false)
            return HeaderItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HeaderItemViewHolder, position: Int) {
            holder.contentView.text = "this is header"
        }

        override fun getItemViewType(): Int {
            return R.layout.item_multi_viewtype_header
        }

        override fun isSameViewType(position: Int): Boolean {
            return datas[position] is HeaderWrapper
        }

        class HeaderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val contentView = itemView.findViewById<TextView>(R.id.content_view)
        }

    }

    class FooterItemAdapter: ItemAdapter<ValueBean, FooterItemAdapter.FooterItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterItemViewHolder {
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
            return datas[position] is FooterWrapper
        }

        class FooterItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val contentView = itemView.findViewById<TextView>(R.id.content_view)
        }

    }

    class ADItemAdapter: ItemAdapter<ValueBean, ADItemAdapter.ADItemViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ADItemViewHolder {
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
            return datas[position] is ADWrapper
        }

        class ADItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val contentView = itemView.findViewById<TextView>(R.id.content_view)
        }

    }

    class NormalItemAdapter: DefaultItemAdaper<ValueBean, NormalItemAdapter.NormalItemViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_multi_viewtype_layout_normal, parent, false)
            return NormalItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: NormalItemViewHolder, position: Int) {
            holder.contentView.text = "this is normal ${datas[position].content} data"
        }

        override fun getItemViewType(): Int {
            return R.layout.item_multi_viewtype_layout_normal
        }

        class NormalItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val contentView = itemView.findViewById<TextView>(R.id.content_view)
        }

    }

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