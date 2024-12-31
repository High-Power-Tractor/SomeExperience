package com.sample.someexperience.livedata

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jt.arc.livedata.SingleLiveData
import com.jt.arc.livedata.SingleLiveDataSticky
import com.sample.someexperience.R
import kotlinx.android.synthetic.main.activity_live_data_test.send_sticky_view
import kotlinx.android.synthetic.main.activity_live_data_test.send_view
import kotlinx.android.synthetic.main.activity_live_data_test.subscribe_sticky_view
import kotlinx.android.synthetic.main.activity_live_data_test.subscribe_view

class LiveDataTestActivity: AppCompatActivity() {
    private val TAG = "LiveDataTestActivity"
    val singleLiveData = SingleLiveData<String>()
    val singleLiveDataSticky = SingleLiveDataSticky<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_test)
        initView()
        singleLiveData.setValue("订阅之前的数据")
        singleLiveDataSticky.setValue("订阅之前的数据-sticky")
        Log.e(TAG, "onCreate end")
    }

    private fun initView(){
        subscribe_view.setOnClickListener {
            observeData()
            subscribe_view.isClickable = false //注释掉 可以模拟多个订阅者
            Toast.makeText(this, "已订阅", Toast.LENGTH_SHORT).show()
        }
        send_view.setOnClickListener {
            singleLiveData.setValue("订阅之后的数据 ${System.currentTimeMillis()}")
        }



        subscribe_sticky_view.setOnClickListener {
            observeDataSticky()
            subscribe_sticky_view.isClickable = false //注释掉 模拟同一个订阅位置订阅多次，不会分发老数据
            Toast.makeText(this, "已订阅", Toast.LENGTH_SHORT).show()
        }
        send_sticky_view.setOnClickListener {
            singleLiveDataSticky.setValue("订阅之后的数据-sticky ${System.currentTimeMillis()}")
        }
    }


    private fun observeData(){
        singleLiveData.observe(this) {
            Log.e(TAG, "received data: ${it}")
        }
    }

    private fun observeDataSticky(){
        singleLiveDataSticky.observe(this, 1) {
            Log.e(TAG, "received data-sticky: ${it}")
        }
    }
}