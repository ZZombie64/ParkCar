package com.example.parkcar

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.parkcar.model.Place
import kotlin.Int

private const val TAG = "SQLiteDBHelper"
class SQLiteDBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME ( " +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$TITLE_COL TEXT NOT NULL," +
                "$DATE_COL TEXT NOT NULL," +
                "$DESCRIPTION_COL TEXT NOT NULL," +
                "$LATITUDE_COL REAL NOT NULL,"+
                "$LONGITUDE_COL REAL NOT NULL" +
                ")"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun getAllPlace(): MutableList<Place> {
        val res: MutableList<Place> = ArrayList<Place>()
        val cursor = readableDatabase.rawQuery(SQL_SELECT_ENTRIES, null)
        if (cursor.moveToFirst())
            do{
                val id = cursor.getLong(0)
                val title = cursor.getString(1)
                val date = cursor.getString(2)
                val desc = cursor.getString(3)
                val lat = cursor.getString(4)
                val lon = cursor.getString(5)

                var place: Place = Place(id, title, date, desc, lat.toDouble(), lon.toDouble())
                res.add(place)
            }
            while (cursor.moveToNext())
        cursor.close()
        //db.close()
        return res
    }

    fun addPlace(place: Place): Long {
        val values = ContentValues().apply {
            put(TITLE_COL, place.title)
            put(DATE_COL, place.datewhen)
            put(DESCRIPTION_COL, place.description)
            put(LATITUDE_COL, place.latitude)
            put(LONGITUDE_COL, place.longitude)
        }
        val id: Long
        writableDatabase.use { db ->
            id = db.insert(TABLE_NAME, null, values)
        }
        return id
    }

    fun deletePlace(idPlace: Long): Int {
        val count: Int
        writableDatabase.use { db ->
            count = db.delete(TABLE_NAME, "id=?", arrayOf("" + idPlace))
        }
        return count
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ParkCar.db"
        const val TABLE_NAME = "PLACES"

        const val ID_COL = "id"
        const val TITLE_COL = "title"
        const val DATE_COL = "datewhen"
        const val DESCRIPTION_COL = "description"
        const val LATITUDE_COL = "latitude"
        const val LONGITUDE_COL = "longitude"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
        const val SQL_SELECT_ENTRIES = "SELECT $ID_COL,$TITLE_COL, $DATE_COL, $DESCRIPTION_COL, $LATITUDE_COL, $LONGITUDE_COL FROM $TABLE_NAME ORDER BY $DATE_COL DESC"

    }
}