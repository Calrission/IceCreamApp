package com.ege.icecreamapp.common

import com.ege.icecreamapp.common.models.BodyGetRoads
import com.ege.icecreamapp.common.models.ModelGEO
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.http.*

interface API {
    @POST("geojson")
    fun roads(@Header("Authorization") key: String = "5b3ce3597851110001cf6248727569936c1848ab8289753891ef0081", @Body body: BodyGetRoads): Call<ModelGEO>
}