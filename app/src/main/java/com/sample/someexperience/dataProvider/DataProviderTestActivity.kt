package com.sample.someexperience.dataProvider

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sample.someexperience.R
import kotlinx.android.synthetic.main.activity_data_provider.confirm_view
import kotlinx.android.synthetic.main.activity_data_provider.input_view

//展示ViewModel怎么向activity/fragment主动获取数据
class DataProviderTestActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_provider)
        initViewModel()
    }

    private fun initViewModel(){
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DataProviderTestViewModel::class.java)
        viewModel.showToastEvent.observe(this){
            Toast.makeText(this, "输入内容：${it}", Toast.LENGTH_SHORT).show()
        }

        //向Viewmodel提供数据
        viewModel.inputDataProvider.provide(this) {
            input_view.text.toString()
        }


        confirm_view.setOnClickListener {
            viewModel.click()
        }
    }
}