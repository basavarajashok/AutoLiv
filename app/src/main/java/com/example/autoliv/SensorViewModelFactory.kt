package com.example.autoliv

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class SensorViewModelFactory(val application: Application,private val respository: SensorRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SensorAndroidViewModel::class.java)){
            return SensorAndroidViewModel(application,respository) as T
        }
        throw IllegalArgumentException("Unkown View Model class")
    }
}