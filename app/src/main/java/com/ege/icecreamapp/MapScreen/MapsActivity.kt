package com.ege.icecreamapp.MapScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ege.icecreamapp.R
import com.ege.icecreamapp.common.OnGetRoads
import com.ege.icecreamapp.common.models.BodyGetRoads
import com.ege.icecreamapp.common.models.ModelGEO

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.ege.icecreamapp.databinding.ActivityMapsBinding
import com.ege.icecreamapp.initRetrofit
import com.ege.icecreamapp.setTextWithEndText
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

    private val shopLocation = LatLng(55.76004644735091, 37.69011145019243)
    private val testUserLocation = LatLng(55.759465684270815,37.69607668304122)
    //private val testUserLocation = LatLng(55.72321132478113,37.44849806212857)

    val moscow = LatLng(55.7522, 37.6156)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        back.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(testUserLocation, 15f))

        mMap.addMarker(
            MarkerOptions()
                .position(shopLocation)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_market_coffee_location))
        )

        mMap.addMarker(
            MarkerOptions()
                .position(testUserLocation)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_market_user_location))
        )

        initRoads(testUserLocation, shopLocation)
    }

    private fun initRoads(start: LatLng, end: LatLng){
        getRoads(start, end, object: OnGetRoads{
            override fun onGet(modelGEO: ModelGEO) {
                val coordsLatLng = modelGEO.features[0].geometry.coordinates.map { LatLng(it[1], it[0]) }
                val minutes = (modelGEO.features[0].properties.summary.duration / 60).toInt()
                initBottomSheetBehavior(minutes)
                drawRoads(coordsLatLng)
            }
        })
    }

    private fun drawRoads(coordsLatLng: List<LatLng>){
        val polylineOptions = PolylineOptions()
        polylineOptions.color(ContextCompat.getColor(this@MapsActivity, R.color.colorBackground))
        polylineOptions.visible(true)
        polylineOptions.width(15f)
        polylineOptions.addAll(coordsLatLng)
        mMap.addPolyline(polylineOptions)
    }

    private fun initBottomSheetBehavior(deliveryMinutes: Int) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        hide_bottom_sheet.setOnClickListener {
            bottomSheetBehavior.state = STATE_COLLAPSED
        }
        bottomSheetBehavior.state = STATE_EXPANDED
        bottomSheetBehavior.addBottomSheetCallback(object:
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                hide_bottom_sheet.visibility = if (newState == STATE_COLLAPSED) View.GONE else View.VISIBLE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                hide_bottom_sheet.alpha = slideOffset
            }
        })
        geo_title.text = "Красноказарменная улица, 2к1"     //55.76004644735091, 37.69011145019243
        time_delivery.setTextWithEndText(deliveryMinutes.toString(), "min")
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
}