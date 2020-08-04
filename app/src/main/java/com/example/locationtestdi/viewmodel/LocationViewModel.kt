package com.example.locationtestdi.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.locationtestdi.model.LocatorManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

/**
 * Data container for Lat and Lon
 * Future-> Favorites Places
 */
class LocationViewModel(val context: Context, val locatorManager: LocatorManager)
    : ViewModel() {

    //Add the Context for Permission settings.

    private val dataSet = MutableLiveData<AppState>()

    private val dataSetErrorMessage = MutableLiveData<String>()
    private val dataSetInfoMessage = MutableLiveData<String>()

   sealed class AppState{
       data class ErrorMessage(val errorMessage: String): AppState()

       data class Response(val favoritePlaces: List<LatLng>) : AppState()

       data class InfoMessage(val infoMessage: String) : AppState()
   }

    fun getDataSet() : LiveData<AppState>{
        return dataSet
    }

    /**
     * Create a LatLong from Maps
     */
    fun getLocationFromUI(lat: String, lon: String){
        val latlng = LatLng(lat.toDouble(), lon.toDouble())
        with(latlng){
            dataSet.value = AppState.Response(listOf(this))
        }
        //
//        LatLng(lat.toDouble(), lon.toDouble()).apply {
//            dataSet.value = AppState.Response(listOf(this))
//        }
        //
    }

    fun getLocationFromUI(){
        locatorManager.getLastLocation {ggg->
            dataSet.value = AppState.Response(listOf(ggg))
        }
    }

    /**
     * Store in Room the Favorite
     */
    fun saveFavoritePlaces(position: LatLng){

    }

    /**
     * Query Room to get Lat/Lng
     */
    private fun getAllFavoritePlaces(){

    }

    fun getDataFromRepository(dataSet: List<LatLng>){
        this.dataSet.value = AppState.Response(dataSet)
    }

    fun getInflateMapView(pO : GoogleMap){
        locatorManager.inflateMapView(pO)
    }
    fun getEnableMyLocation(pO : GoogleMap){
        locatorManager.enableMyLocation(pO)
    }
    fun getRequestPermissionChecker(pO : GoogleMap){
        locatorManager.requestPermissionChecker(pO)
    }

}