package com.example.autoliv

import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.autoliv.databinding.ActivityMainBinding

/**
 * Created by Basavaraj A. on 09-04-22.
 */

class SensorActivity : AppCompatActivity() {
    private val WRITE_LOCATION_RQ = 101
    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorAndroidViewModel: SensorAndroidViewModel
    private lateinit var sansMedium: Typeface
    private lateinit var railwayMediumFont: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val repository = SensorRepository()
        val factory = SensorViewModelFactory(application,repository)
        sensorAndroidViewModel = ViewModelProvider(this,factory).get(SensorAndroidViewModel::class.java)
        binding.sensorViewModel = sensorAndroidViewModel
        binding.lifecycleOwner = this
        checkForPremissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,"write",WRITE_LOCATION_RQ)
        setFontProperty()

        sensorAndroidViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Write permission not granted", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setFontProperty(){
        sansMedium = ResourcesCompat.getFont(this,R.font.sans_medium)!!
        railwayMediumFont = ResourcesCompat.getFont(this,R.font.raleway_medium)!!

        binding.accelometerMainTitle.typeface = sansMedium
        binding.accelometerXData.typeface = sansMedium
        binding.accelometerYData.typeface = sansMedium
        binding.accelometerZData.typeface = sansMedium
        binding.accelometerXValue.typeface = sansMedium
        binding.accelometerYValue.typeface = sansMedium
        binding.accelometerZValue.typeface = sansMedium

        binding.gyroscopeMainTitle.typeface = sansMedium
        binding.gyroscopeXData.typeface = sansMedium
        binding.gyroscopeYData.typeface = sansMedium
        binding.gyroscopeZData.typeface = sansMedium
        binding.gyroscopeXValue.typeface = sansMedium
        binding.gyroscopeYValue.typeface = sansMedium
        binding.gyroscopeZValue.typeface = sansMedium

        binding.acceloRecordStopButton.typeface = railwayMediumFont
        binding.gyroscopeRecordStopButton.typeface = railwayMediumFont
        binding.note.typeface = railwayMediumFont
        binding.fileStroageInfo.typeface = railwayMediumFont
    }

    private fun checkForPremissions(permission: String,name: String,requestCode: Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name permission granted",Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission,name,requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission),requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isEmpty() || grantResults[0]!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(applicationContext,"permission refused",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(applicationContext,"Permission granted",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog(permission: String,name: String,requestCode: Int){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("Ok"){dialog,which ->
                ActivityCompat.requestPermissions(this@SensorActivity, arrayOf(permission),requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        sensorAndroidViewModel.registerSensors()
    }

    override fun onPause() {
        super.onPause()
        sensorAndroidViewModel.unRegisterSensors()
    }

}