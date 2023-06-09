package com.jiwon.mpteam_layout

import android.database.sqlite.SQLiteOpenHelper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jiwon.mpteam_layout.databinding.RestaurantRowBinding


class RestaurantAdapter(var items: ArrayList<RestaurantData>): RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }

    var itemClickListener: OnItemClickListener?=null

    inner class MyViewHolder(val binding: RestaurantRowBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.row.setOnClickListener {
                itemClickListener?.OnItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = RestaurantRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.restaurantName.text = items[position].RESTAURANT
        holder.binding.category.text = items[position].CATEGORY
        holder.binding.location.text = items[position].LOCATION
    }
}