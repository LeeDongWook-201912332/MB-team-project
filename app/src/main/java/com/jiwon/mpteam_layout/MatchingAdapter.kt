package com.jiwon.mpteam_layout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jiwon.mpteam_layout.databinding.FragmentMatchingBinding
import com.jiwon.mpteam_layout.databinding.ItemMatchingBinding

class MatchingAdapter(private var itemList: ArrayList<MatchingData>): RecyclerView.Adapter<MatchingAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMatchingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(matchingData: MatchingData){
            binding.itemMatchingIdTv.text = matchingData.id
            binding.itemMatchingContentTv.text = matchingData.content
            binding.itemMatchingNumTv.text = matchingData.num
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMatchingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}