package com.example.parkcar


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkcar.model.Place

private const val TAG = "MyAdapter"
class MyAdapter(private val itemList: MutableList<Place>, val dbHelper: SQLiteDBHelper, val onClickListener: OnClickListener) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        interface  OnClickListener{
            fun onItemClick(position: Int)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]

        holder.itemName.text = item.title
        holder.itemDate.text = item.datewhen
        holder.itemDescription.text = item.description

        holder.itemView.setOnClickListener {
            Log.i(TAG, "Item position $position")
            onClickListener.onItemClick(position)
        }

        holder.btnDelete.setOnClickListener {
            val idPlace: Long = itemList[position].id
            if(dbHelper.deletePlace(idPlace)>0){
                itemList.removeAt(position)
                notifyItemRemoved(position)
            }
            else{
                Log.i(TAG, "Item position $position not deleted!")
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // ViewHolder class
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemDate: TextView = itemView.findViewById(R.id.itemDate)
        val itemDescription: TextView = itemView.findViewById(R.id.itemDescription)
//        val itemPic: ImageView = itemView.findViewById(R.id.itemPic)
//        val itemPic2: ImageView = itemView.findViewById(R.id.itemPic2)
        val btnDelete: ImageView = itemView.findViewById(R.id.buttonDelete)
    }
}