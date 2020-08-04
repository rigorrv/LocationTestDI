package com.example.locationtestdi.model

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Class that provides current location
 * Return Lat and Lon
 */
class LocatorManager(val context: Context) {

    val REQ_LOCATION_PERMISSION = 2345 //random


    private var fusedLocationClient: FusedLocationProviderClient
    init {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)
    }

    fun getLastLocation(callback: (position: LatLng) -> Unit){

        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                //val latlng = LatLng(it.latitude, it.longitude)
                callback.invoke(LatLng(it.latitude, it.longitude))
        }
    }



    fun inflateMapView(pO : GoogleMap) {
        var googleMap = pO
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                REQ_LOCATION_PERMISSION)

            return
        }
        else
            googleMap.setMyLocationEnabled(true)
    }

     fun enableMyLocation(pO : GoogleMap) {
        var googleMap = pO

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true)
        } else {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQ_LOCATION_PERMISSION)
        }
    }

    fun requestPermissionChecker(pO : GoogleMap){
        var googleMap = pO
        val permissionsChecker = PermissionChecker
            .checkCallingOrSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if(permissionsChecker == PackageManager.PERMISSION_GRANTED)
            googleMap.isMyLocationEnabled = true
        else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(),
                REQ_LOCATION_PERMISSION)
        }
    }

    fun loadList() : List<JsonLocation>  {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://nonstopcode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiResturant::class.java)

        api.getDefinitions().enqueue(object : Callback<List<JsonLocation.JsonLocationItem>> {

            override fun onFailure(call: Call<List<JsonLocation.JsonLocationItem>>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<List<JsonLocation.JsonLocationItem>>,
                response: Response<List<JsonLocation.JsonLocationItem>>
            ) {

                Log.d(
                    "Data",
                    "Working" + GsonBuilder().setPrettyPrinting().create().toJson(response.body())
                )
                //progress.visibility = View.GONE
            }
        })
        return loadList()
    }



}