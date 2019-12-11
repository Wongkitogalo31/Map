package com.example.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val permission: Array<String> = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val permissionCode = 1100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Cari.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var search = query.toString()
                var address: List<Address> = emptyList()

                val geocoder = Geocoder(this@MapsActivity)
                address = geocoder.getFromLocationName(search, 1)

                val hasil = address.get(0)
                val latLng = LatLng(hasil.latitude, hasil.longitude)

                mMap.addMarker(
                    MarkerOptions().position(latLng)
                        .title(search)
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun verifyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(
                    this.applicationContext, permission[0]
                )
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this.applicationContext, permission[1]
                )
                == PackageManager.PERMISSION_GRANTED
            ) else {
                ActivityCompat.requestPermissions(this, permission, permissionCode)
            }
        }
        val uty = LatLng(-7.747033, 110.355398)
        val zoomSize = 17.0f
        mMap.addMarker(MarkerOptions().position(uty).title("Universitas Teknologi Yogyakarta"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uty, zoomSize))
        mMap.isMyLocationEnabled = true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        verifyPermission()
    }
}
