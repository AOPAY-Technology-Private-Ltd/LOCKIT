package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.databinding.ActivityMapBinding
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : BaseActivity() ,OnMapReadyCallback{

    lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap

    companion object{
        var lattitude : Double = 0.0
        var longitude : Double = 0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.back.setOnClickListener {
            finish()
        }



    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val zoomLevel = 18f

        val location = LatLng(lattitude, longitude)
        var address = ConstantClass.getAddressFromLatLng(this,lattitude,longitude)

        binding.address.text = "${address}"


                // Add marker
        mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(address)
        )

        // Move & zoom camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))

    }

}