package com.sample.someexperience.multiviewtype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jt.arc.ui.CommonRecyclerviewAdapter
import com.jt.arc.ui.CommonViewHolder
import com.jt.arc.ui.ViewHolderConfig
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
        recycler_view.adapter = ContentAdapter(recycler_view)
    }

    class ContentAdapter(recyclerView: RecyclerView)
        : CommonRecyclerviewAdapter<CommonViewHolder>(recyclerView) {
        val VIEW_TYPE01 = 1
        val VIEW_TYPE02 = 2
        val VIEW_TYPE03 = 3
        val VIEW_TYPE_NORMAL = 4

        override fun onCreateViewHolderConfig(position: Int): ViewHolderConfig {
            if(position == 0) return ViewHolderConfig(VIEW_TYPE01, {
                ViewHolder01(it)
            })
            if(position == 3) return ViewHolderConfig(VIEW_TYPE02, {
                ViewHolder02(it)
            })
            if(position == 5) return ViewHolderConfig(VIEW_TYPE03, {
                ViewHolder03(it)
            })
            return ViewHolderConfig(VIEW_TYPE_NORMAL, {
                ViewHolderNormal(it)
            })
        }

        override fun getItemCount(): Int {
            return 20
        }

    }



    class ViewHolder01(parent: ViewGroup) : CommonViewHolder(R.layout.item_multi_viewtype_layout01, parent) {
        val contentView = itemView.findViewById<TextView>(R.id.content_view)

        override fun bindData(position: Int) {
            contentView.text = "this is special ${position} data"
        }
    }

    class ViewHolder02(parent: ViewGroup) : CommonViewHolder(R.layout.item_multi_viewtype_layout02, parent) {
        val contentView = itemView.findViewById<TextView>(R.id.content_view)

        override fun bindData(position: Int) {
            contentView.text = "this is special ${position} data"
        }
    }

    class ViewHolder03(parent: ViewGroup) : CommonViewHolder(R.layout.item_multi_viewtype_layout03, parent) {
        val contentView = itemView.findViewById<TextView>(R.id.content_view)

        override fun bindData(position: Int) {
            contentView.text = "this is special ${position} data"
        }
    }

    class ViewHolderNormal(parent: ViewGroup) : CommonViewHolder(R.layout.item_multi_viewtype_layout_normal, parent) {
        val contentView = itemView.findViewById<TextView>(R.id.content_view)

        override fun bindData(position: Int) {
            contentView.text = "this is normal ${position} data"
        }
    }
}