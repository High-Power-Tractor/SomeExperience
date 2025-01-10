package com.sample.someexperience

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sample.someexperience.dataProvider.DataProviderTestActivity
import com.sample.someexperience.livedata.LiveDataTestActivity
import com.sample.someexperience.multiviewtype.MultiViewTypeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addTextView("RecyclerView中有多个ViewType") {
            startActivity(Intent(this, MultiViewTypeActivity::class.java))
        }
        addTextView("SingleLiveData测试") {
            startActivity(Intent(this, LiveDataTestActivity::class.java))
        }
        addTextView("DataProvider测试") {
            startActivity(Intent(this, DataProviderTestActivity::class.java))
        }
    }


    private fun addTextView(content: String, clickListener: View.OnClickListener){
        val textView = LayoutInflater.from(this).inflate(R.layout.layout_main_item, root_container, false) as TextView
        root_container.addView(textView)
        textView.text = content
        textView.setOnClickListener(clickListener)
        hashCode()
    }
}