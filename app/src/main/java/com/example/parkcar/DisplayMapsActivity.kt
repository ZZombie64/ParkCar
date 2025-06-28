package com.example.parkcar

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.parkcar.databinding.ActivityDisplayMapsBinding
import com.example.parkcar.model.Place

private const val TAG = "DisplayMapsActivity"
class DisplayMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDisplayMapsBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var currPlace: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisplayMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_display_maps)

        //currPlace = intent.getSerializableExtra("PLACE") as Place
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            currPlace = intent.getSerializableExtra("PLACE", Place::class.java)!!
        else
            currPlace = intent.getSerializableExtra("PLACE") as Place

        supportActionBar?.title = currPlace.title
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapPlaces) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.i(TAG, "Item select: ${currPlace.title}")

        // Add a marker in Sydney and move the camera
        val coordinate = LatLng(currPlace.latitude, currPlace.longitude)
        mMap.addMarker(MarkerOptions().position(coordinate).title(currPlace.title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 10f))

//        mapFragment.view?.let {
//            Snackbar.make(it, "Press to return to List",
//                Snackbar.LENGTH_INDEFINITE
//            ).setAction("OK", {
//                finish()
//            }).setActionTextColor(ContextCompat.getColor(this, android.R.color.white)).show()
//        }
    }

//    fun <T : Serializable?> getSerializable(intent: Intent, key: String, m_class: Class<T>): T {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//            intent.getSerializableExtra(key, m_class)!!
//        else
//            intent.getSerializableExtra(key) as T
//    }
}