package com.jiwon.mpteam_layout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jiwon.mpteam_layout.databinding.MenuRowBinding


class MenuAdapter(var items: ArrayList<RestaurantData>): RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }

    var itemClickListener: OnItemClickListener?=null

    inner class MyViewHolder(val binding: MenuRowBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.row.setOnClickListener {
                itemClickListener?.OnItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MenuRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.menuName.text = items[position].MENU
        holder.binding.price.text = items[position].PRICE
    }
}