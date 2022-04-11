package com.example.autoliv

import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Basavaraj A. on 09-04-22.
 */

object Utility {
    private val mFolderName = "AutoLiv"
    private val FILE_SEPARATOR = "/"
    val ACCELOMETER_CSV_FILE = "Accelometer.csv"
    val GYROSCOPE_CSV_FILE = "Gyroscope.csv"

    fun getDirectoryPath(filename : String) : File {
        val f = File(Environment.getExternalStorageDirectory(), mFolderName);
        if (!f.exists()) {
            f.mkdirs()
            Log.d("MYTAG","Folder created successfully")
        }
        return File(f,FILE_SEPARATOR + filename)
    }

    fun getDateTimeFromMilliseconds(currentMilliSeconds: Long): String {
        var format = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return format.format(Date(currentMilliSeconds))
    }

}