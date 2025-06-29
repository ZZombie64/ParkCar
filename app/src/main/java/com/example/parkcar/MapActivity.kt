package com.example.parkcar

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.example.parkcar.databinding.ActivityMapBinding
import com.example.parkcar.model.Place
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.arrayOf
import kotlin.let
import kotlin.text.isEmpty
import kotlin.text.trim
import kotlin.toString

private const val TAG = "MapActivity"
class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation:Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMapBinding

    private  var currMarker: Marker?=null

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title="New parking"

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragment.view?.let {
            Snackbar.make(it, "Tap on map to add a marker",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("OK", {

            }).setActionTextColor(ContextCompat.getColor(this, android.R.color.white)).show()
        }
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled=true
        mMap.setOnMarkerClickListener(this)
        setUpMap()

        mMap.setOnMapClickListener {latLon->
            Log.i(TAG, "click OnMapClickListener")
            showPositionDialog(latLon)
        }
    }

    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)
//            return
        }
        mMap.isMyLocationEnabled=true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {location ->
            if(location!=null){
                lastLocation = location
                var currLatLon = LatLng(location.latitude, location.longitude)
//                placeMarkerOnMap(currLatLon)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLon, 15f))
            }
        }
    }

//    private fun placeMarkerOnMap(currLatLon: LatLng) {
//        val marker = MarkerOptions().position(currLatLon)
//        marker.title("You are here")
//        mMap.addMarker(marker)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showPositionDialog(latLon: LatLng) {
        val placeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_place, null)
        val dialog = AlertDialog.Builder(this).setTitle("Create a new parking").setView(placeFormView).setNegativeButton ("Cancel", null).setPositiveButton("OK", null).show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = placeFormView.findViewById<EditText>(R.id.etTitle).text.toString()
            val description = placeFormView.findViewById<EditText>(R.id.etDescription).text.toString()
            if(title.trim().isEmpty() || description.trim().isEmpty()){
                Toast.makeText(this, "Empty title or description!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            currMarker = mMap.addMarker(MarkerOptions().position(latLon).title(title).snippet(description))

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val currentDate = LocalDateTime.now().format(formatter)

            val currPlace = Place(0, currMarker?.title.toString(), currentDate, currMarker?.snippet.toString(),
                currMarker?.position?.latitude!!, currMarker?.position?.longitude!! )

            val data = Intent()
            data.putExtra(NEW_PARKING, currPlace)
            setResult(RESULT_OK, data)
            dialog.dismiss()
            finish()
        }

    }

    override fun onMarkerClick(p0: Marker)= false
}