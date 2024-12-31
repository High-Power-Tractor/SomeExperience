package com.sample.someexperience.livedata

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jt.arc.livedata.SingleLiveData
import com.sample.someexperience.R
import kotlinx.android.synthetic.main.activity_live_data_test.send_view
import kotlinx.android.synthetic.main.activity_live_data_test.subscribe_view

class LiveDataTestActivity: AppCompatActivity() {
    private val TAG = "LiveDataTestActivity"
    val singleLiveData = SingleLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_test)
        initView()
        singleLiveData.setValue("订阅之前的数据")
        Log.e(TAG, "onCreate end")
    }

    private fun initView(){
        subscribe_view.setOnClickListener {
            observeData()
            subscribe_view.isClickable = false
            Toast.makeText(this, "已订阅", Toast.LENGTH_SHORT).show()
        }

        send_view.setOnClickListener {
            singleLiveData.setValue("订阅之后的数据 ${System.currentTimeMillis()}")
        }
    }


    private fun observeData(){
        singleLiveData.observe(this) {
            Log.e(TAG, "received data: ${it}")
        }
    }
}