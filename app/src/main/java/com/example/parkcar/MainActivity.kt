package com.example.parkcar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkcar.model.Place
import com.example.parkcar.R.id.btnAddPlace

private const val TAG = "MainActivity"
const val NEW_PARKING = "NEW_PARKING"
private const val REQUEST_CODE = 1000
class MainActivity : AppCompatActivity() {

    private lateinit var itemList: MutableList<Place>
    private lateinit var myAdapter: MyAdapter
    private lateinit var dbHelper: SQLiteDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = SQLiteDBHelper(this)

        itemList = dbHelper.getAllPlace()
//        itemList = ArrayList()
//        itemList.add(Place(1, "Gates CS building", "May 25, 2025", "Many long nights in this basement",
//                37.430, -122.173))
//        itemList.add(Place(2, "Branner Hall", "May 23, 2025", "Best dorm at Stanford",
//            37.426, -122.163))

        val btnAddPlace = findViewById<ImageView>(btnAddPlace)
        btnAddPlace.setOnClickListener {
            Log.i(TAG, "click btnAddPlace")

            val intent = Intent(this@MainActivity, MapActivity::class.java)
            intent.putExtra(NEW_PARKING, "New parking")
            startActivityForResult(intent, REQUEST_CODE)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Set LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Set Adapter
        myAdapter = MyAdapter(itemList, dbHelper, object: MyAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                Log.i(TAG, "Item select on position $position")
                val intent = Intent(this@MainActivity, DisplayMapsActivity::class.java)
                //intent.setPackage("com.mobile.myparkinglots")
                intent.putExtra("PLACE", itemList[position])
                startActivity(intent)
            }
        } )
        recyclerView.adapter = myAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "onActivityResult")
        if(requestCode == REQUEST_CODE && resultCode==RESULT_OK){
            var newPlace = data?.getSerializableExtra(NEW_PARKING) as Place
            Log.i(TAG, "New parking in ${newPlace.title}")
            newPlace.id = dbHelper.addPlace(newPlace)
            if(newPlace.id>0) {
                itemList.add(newPlace)
                myAdapter.notifyItemInserted(itemList.size - 1)
                Log.i(TAG, "New parking in ${newPlace.title} added")
            }else{
                Log.i(TAG, "New parking in ${newPlace.title} not added!")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}