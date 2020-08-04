package com.example.locationtestdi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import com.example.locationtestdi.model.LocatorManager
import com.example.locationtestdi.viewmodel.LocationViewModel
import com.example.locationtestdi.viewmodel.LocationViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    val REQ_LOCATION_PERMISSION = 2345 //random
    val  MESSAGE_RATIONALE = "Need Location to Show you Awesome!"


    lateinit var viewModel : LocationViewModel


    @Inject
    lateinit var locatorManager: LocatorManager

    @Inject
    lateinit var viewModelProvider: LocationViewModelProvider

    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LocationApp.component.inject(this)
        maps.onCreate(savedInstanceState)

        maps.getMapAsync(this)

        viewModel = viewModelProvider.create(LocationViewModel::class.java)

        viewModel.getDataSet().observe(this,
        Observer {
            when(it){
               is LocationViewModel.AppState.Response->{
                   googleMap.moveCamera(
                       CameraUpdateFactory
                           .newLatLng(it.favoritePlaces[0])
                   )
               }
               is LocationViewModel.AppState.ErrorMessage->{

               }
               is LocationViewModel.AppState.InfoMessage->{

               }
            }
        })
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        when(requestCode){
            REQ_LOCATION_PERMISSION->{
                if(grantResults.size > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
                    viewModel.getEnableMyLocation(googleMap)
                else
                    requestRationale()
            }
        }
    }

    private fun requestRationale() {
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            MESSAGE_RATIONALE)
    }


    override fun onStart() {
        super.onStart()
        maps.onStart()
    }

    override fun onResume() {
        super.onResume()
        maps.onResume()
    }

    override fun onPause() {
        super.onPause()
        maps.onPause()
    }

    override fun onStop() {
        super.onStop()
        maps.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        maps.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        maps.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        maps.onLowMemory()
    }

    override fun onMapReady(p0: GoogleMap?) {

        Log.d("MainActivity", "Mapt TYpe${p0?.mapType}")
        p0?.let {
            googleMap = it

            // Add a marker in Sydney and move the camera
            val sydney = LatLng(-34.0, 151.0)
            googleMap.addMarker(
                MarkerOptions()
                    .position(sydney)
                    .title("Marker in Sydney")
            )
            googleMap.moveCamera(
                CameraUpdateFactory
                    .newLatLng(sydney)
            )
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)

            viewModel.getEnableMyLocation(googleMap)
        }
//
    }
}