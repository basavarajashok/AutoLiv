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

    fun getDateTimeFromMilliseconds(currentMilliSeconds: Long): Array<String> {
        val dateFormat = SimpleDateFormat("dd/M/yyyy")
        val timeFormat = SimpleDateFormat("hh:mm:ss")
        val date = Date(currentMilliSeconds)
        return arrayOf(dateFormat.format(date),timeFormat.format(date))
    }

}