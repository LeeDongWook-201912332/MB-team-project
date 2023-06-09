package com.jiwon.mpteam_layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ReviewAdapter(options: FirebaseRecyclerOptions<ReviewData>) :
    FirebaseRecyclerAdapter<ReviewData, ReviewAdapter.ReviewViewHolder>(options) {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_row, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int, model: ReviewData) {
        holder.bind(model)
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val date: TextView = itemView.findViewById(R.id.date)
        private val menu: TextView = itemView.findViewById(R.id.menu)
        private val rating: TextView = itemView.findViewById(R.id.rating)
        private val content: TextView = itemView.findViewById(R.id.content)
        private val restaurant : TextView = itemView.findViewById(R.id.restaurantName)

        fun bind(product: ReviewData) {
            restaurant.text = product.restaurant
            date.text = product.date
            menu.text = product.menu
            rating.text = product.rating
            content.text = product.content

            itemView.setOnClickListener {
                itemClickListener?.onItemClick(bindingAdapterPosition)
            }
        }
    }
}
