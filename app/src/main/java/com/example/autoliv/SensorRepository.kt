package com.example.autoliv

import android.util.Log
import com.example.autoliv.Utility.getDirectoryPath
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception

/**
 * Created by Basavaraj A. on 09-04-22.
 */

class SensorRepository {
    var mAcceloMeterWriter: CSVWriter?= null
    var mGyroscopeWriter: CSVWriter?= null

    fun openDataToWriter(fileName : String){
        try {
            if (fileName == Utility.ACCELOMETER_CSV_FILE) {
                var path = File(getDirectoryPath(fileName).toString())
                mAcceloMeterWriter = CSVWriter(FileWriter(path))
                mAcceloMeterWriter?.writeNext(arrayOf("Date","Time","X","Y","Z"))
            } else if (fileName == Utility.GYROSCOPE_CSV_FILE) {
                var path = File(getDirectoryPath(fileName).toString())
                mGyroscopeWriter = CSVWriter(FileWriter(path))
                mGyroscopeWriter?.writeNext(arrayOf("Date","Time","X","Y","Z"))
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun writeData(fileName: String,sensorData: SensorData){
        try {
            if (fileName == Utility.ACCELOMETER_CSV_FILE) {
                mAcceloMeterWriter?.writeNext(
                    arrayOf(
                        sensorData.date,
                        sensorData.time,
                        sensorData.X,
                        sensorData.Y,
                        sensorData.Z
                    )
                )
            }else if(fileName == Utility.GYROSCOPE_CSV_FILE){
                mGyroscopeWriter?.writeNext(
                    arrayOf(
                        sensorData.date,
                        sensorData.time,
                        sensorData.X,
                        sensorData.Y,
                        sensorData.Z
                    )
                )
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun closeAccleroMeterWriter(){
        try {

            mAcceloMeterWriter?.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun closeGyroscopeWriter(){
        try {
            mGyroscopeWriter?.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}