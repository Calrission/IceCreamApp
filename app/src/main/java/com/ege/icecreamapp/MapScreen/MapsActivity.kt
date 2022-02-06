package com.ege.icecreamapp.MapScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.ege.icecreamapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ege.icecreamapp.databinding.ActivityMapsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.bottom_sheet_map_screen.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initBottomSheetBehavior()

        back.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val moscow = LatLng(55.7522, 37.6156)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moscow, 10f))
    }

    private fun initBottomSheetBehavior() {
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
    }
}