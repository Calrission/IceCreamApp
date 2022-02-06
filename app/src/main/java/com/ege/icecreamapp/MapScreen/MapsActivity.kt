package com.ege.icecreamapp.MapScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ege.icecreamapp.R
import com.ege.icecreamapp.common.OnGetRoads
import com.ege.icecreamapp.common.models.BodyGetRoads
import com.ege.icecreamapp.common.models.ModelGEO
import com.ege.icecreamapp.databinding.ActivityMapsBinding
import com.ege.icecreamapp.initRetrofit
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.bottom_sheet_map_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private var markerUser: Marker? = null
    private var roadsLine: Polyline? = null

    private val shopLocation = LatLng(55.76004644735091, 37.69011145019243)
    private val testUserLocation = LatLng(55.759465684270815,37.69607668304122)
    //private val testUserLocation = LatLng(55.72321132478113,37.44849806212857)

    val moscow = LatLng(55.7522, 37.6156)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheetBehavior = createBottomSheetBehavior()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        back.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(
            MarkerOptions()
                .position(shopLocation)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_market_coffee_location))
        )
        cameraToLatLng(shopLocation)
        initLocationUserAndRoads()
    }

    private fun cameraToLatLng(latLng: LatLng){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun initRoads(start: LatLng, end: LatLng){
        getRoads(start, end, object: OnGetRoads{
            override fun onGet(modelGEO: ModelGEO) {
                modelGeoToView(modelGEO)
            }
        })
    }

    private fun modelGeoToView(modelGEO: ModelGEO){
        val coordsLatLng = modelGEO.features[0].geometry.coordinates.map { LatLng(it[1], it[0]) }
        prepareBottomSheetBehavior(modelGEO.features[0].properties.summary.duration.toInt())
        drawRoads(coordsLatLng)
    }

    private fun drawRoads(coordsLatLng: List<LatLng>){
        val roadsLine = PolylineOptions()
        roadsLine.color(ContextCompat.getColor(this@MapsActivity, R.color.colorBackground))
        roadsLine.visible(true)
        roadsLine.width(15f)
        roadsLine.addAll(coordsLatLng)
        this.roadsLine = mMap.addPolyline(roadsLine)
    }

    private fun createBottomSheetBehavior(): BottomSheetBehavior<LinearLayout>{
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        hide_bottom_sheet.setOnClickListener {
            bottomSheetBehavior.state = STATE_COLLAPSED
        }
        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                hide_bottom_sheet.visibility = if (newState == STATE_COLLAPSED) View.GONE else View.VISIBLE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                hide_bottom_sheet.alpha = slideOffset
            }
        })
        return bottomSheetBehavior
    }

    private fun prepareBottomSheetBehavior(deliverySeconds: Int) {
        bottomSheetBehavior.state = STATE_EXPANDED
        geo_title.text = "Красноказарменная улица, 2к1"     //55.76004644735091, 37.69011145019243
        time_delivery.text = parseDeliveryTime(deliverySeconds)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun getRoads(start: LatLng, end: LatLng, onGetRoads: OnGetRoads){
        val body = BodyGetRoads(coordinates = arrayListOf(
            arrayListOf(start.longitude, start.latitude),
            arrayListOf(end.longitude, end.latitude)
        ))
        Log.e("get-roads", "prepare coords")
        initRetrofit().roads(body = body).enqueue(object: Callback<ModelGEO>{
            override fun onResponse(call: Call<ModelGEO>, response: Response<ModelGEO>) {
                if (response.isSuccessful && response.body() != null){
                    onGetRoads.onGet(response.body()!!)
                    Log.e("get-roads", "good retrofit")
                }else{
                    Toast.makeText(this@MapsActivity, "Fail get route between two points. See logs.", Toast.LENGTH_LONG).show()
                    Log.e("get-roads", "${response.code()}-${response.raw().request().url().url()}")
                }
            }

            override fun onFailure(call: Call<ModelGEO>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "Fail get route between two points. See logs.", Toast.LENGTH_LONG).show()
                Log.e("get-roads", t.message!!)
            }
        })
    }

    private fun parseDeliveryTime(seconds: Int): String{
        if (seconds < 60)
            return "$seconds sec"
        if (seconds < 60*60)
            return "${(seconds / 60).toInt()} min"
        return "${(seconds / 60 / 60).toInt()} hour"
    }

    private fun initLocationUserAndRoads(){
        reqPer()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            reqPer()
        }
        val locationRequest = LocationRequest()
        locationRequest.fastestInterval = 5000
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        var firstCameraToUser = true
        fusedLocationClient.requestLocationUpdates(locationRequest, object: LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val location = p0.lastLocation
                updateUserLocation(location, firstCameraToUser)
                if (firstCameraToUser)
                    firstCameraToUser = false
            }
        }, Looper.getMainLooper())
    }

    private fun createMarkerUser(latLng: LatLng): Marker {
        return mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_market_user_location))
        )!!
    }

    private fun reqPer() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
    }

    private fun updateUserLocation(newLocation: Location, cameraTo: Boolean){
        if (markerUser != null)
            markerUser!!.remove()
        val userLatLng = LatLng(newLocation.latitude, newLocation.longitude)
        markerUser = createMarkerUser(userLatLng)
        if (cameraTo)
            cameraToLatLng(userLatLng)
        getRoads(shopLocation, userLatLng, object: OnGetRoads{
            override fun onGet(modelGEO: ModelGEO) {
                if (roadsLine != null)
                    roadsLine!!.remove()
                modelGeoToView(modelGEO)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            reqPer()
        }
    }
}