package com.sample.someexperience.dataProvider

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jt.arc.dataprovider.DataProvider

class DataProviderTestViewModel : ViewModel(){
    val inputDataProvider = DataProvider<String>()

    val showToastEvent = MutableLiveData<String>()

    fun click(){
        inputDataProvider.getData()?.let { inputStr ->
            if(!TextUtils.isEmpty(inputStr)){
                showToastEvent.postValue(inputStr)
            }
        }
    }
}