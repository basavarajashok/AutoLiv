package com.example.autoliv

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

/**
* Created by Basavaraj A. on 09-04-22.
*/

class SensorAndroidViewModel(application: Application, private val repository: SensorRepository) :
    AndroidViewModel(application),
    Observable, SensorEventListener {

    private val ACCE_FILTER_DATA_MIN_TIME = 1000
    private var lastSavedAccelerator = System.currentTimeMillis()
    private var lastSavedGyscrope = System.currentTimeMillis()
    private lateinit var mSensorManager: SensorManager
    private var mAccelerometer: Sensor? = null
    private var mGyscrope: Sensor? = null
    private var app: Application
    private var mWriteAccelometertoCSV = false
    private var mWriteGyroscopetoCSV = false
    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
    get() = statusMessage

    @Bindable
    val acceleratorX = MutableLiveData<String>()
    @Bindable
    val acceleratorY = MutableLiveData<String>()
    @Bindable
    val acceleratorZ = MutableLiveData<String>()
    @Bindable
    val gyroscopeX = MutableLiveData<String>()
    @Bindable
    val gyroscopeY = MutableLiveData<String>()
    @Bindable
    val gyroscopeZ = MutableLiveData<String>()
    @Bindable
    val accelometerButtonText = MutableLiveData<String>()
    @Bindable
    val gyroscopeButtonText = MutableLiveData<String>()


    init {
        this.app = application
        accelometerButtonText.value = "Record Data in CSV"
        gyroscopeButtonText.value = "Record Data in CSV"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    fun writeAccelometerDatatoCSV(){
        if(accelometerButtonText.value == "Record Data in CSV") {
            repository.openDataToWriter(Utility.ACCELOMETER_CSV_FILE)
            mWriteAccelometertoCSV = true
            accelometerButtonText.value = "Stop"
        }else if(accelometerButtonText.value == "Stop") {
            mWriteAccelometertoCSV = false
            accelometerButtonText.value = "Record Data in CSV"
            repository.closeAccleroMeterWriter()
            statusMessage.value = Event("Accelometer data recorded successfully")
        }
    }

    fun writeGyroscopeDatatoCSV(){
        if(gyroscopeButtonText.value == "Record Data in CSV") {
            repository.openDataToWriter(Utility.GYROSCOPE_CSV_FILE)
            mWriteGyroscopetoCSV = true
            gyroscopeButtonText.value = "Stop"
        }else if(gyroscopeButtonText.value == "Stop") {
            mWriteGyroscopetoCSV = false
            gyroscopeButtonText.value = "Record Data in CSV"
            repository.closeGyroscopeWriter()
            statusMessage.value = Event("Gyroscope data recorded successfully")
        }

    }

    fun registerSensors() {
        mSensorManager = app.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            mAccelerometer = it
        }
        mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            mGyscrope = it
        }
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyscrope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            if ((System.currentTimeMillis() - lastSavedAccelerator) > ACCE_FILTER_DATA_MIN_TIME) {
                lastSavedAccelerator = System.currentTimeMillis();
                val dateAndTime = Utility.getDateTimeFromMilliseconds(lastSavedAccelerator)
                Log.d("MYDATA MILI",lastSavedAccelerator.toString())
                val sensorData = SensorData(
                    dateAndTime[0],
                    dateAndTime[1],
                    event.values[0].toString(),
                    event.values[1].toString(),
                    event.values[2].toString()
                )
                acceleratorX.value = sensorData.X
                acceleratorY.value = sensorData.Y
                acceleratorZ.value = sensorData.Z
                if(mWriteAccelometertoCSV)
                    writeData(Utility.ACCELOMETER_CSV_FILE,sensorData)
            }
        }

        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            if ((System.currentTimeMillis() - lastSavedGyscrope) > ACCE_FILTER_DATA_MIN_TIME) {
                lastSavedGyscrope = System.currentTimeMillis();
                val dateAndTime = Utility.getDateTimeFromMilliseconds(lastSavedGyscrope)
                val sensorData = SensorData(
                    dateAndTime[0],
                    dateAndTime[1],
                    event.values[0].toString(),
                    event.values[1].toString(),
                    event.values[2].toString()
                )
                gyroscopeX.value = sensorData.X
                gyroscopeY.value = sensorData.Y
                gyroscopeZ.value = sensorData.Z
                if(mWriteGyroscopetoCSV)
                    writeData(Utility.GYROSCOPE_CSV_FILE,sensorData)
            }
        }
    }

    private fun writeData(fileName: String,sensorData: SensorData) =
            repository.writeData(fileName,sensorData)



    override fun onAccuracyChanged(event: Sensor?, p1: Int) {
    }

    fun unRegisterSensors() {
        mSensorManager.unregisterListener(this)

        repository.closeAccleroMeterWriter()
        repository.closeGyroscopeWriter()
    }
}