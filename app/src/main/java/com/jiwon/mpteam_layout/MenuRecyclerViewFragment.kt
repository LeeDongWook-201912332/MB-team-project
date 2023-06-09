package com.jiwon.mpteam_layout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiwon.mpteam_layout.databinding.FragmentMenuRecyclerViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuRecyclerViewFragment : Fragment() {
    var binding: FragmentMenuRecyclerViewBinding ?= null

    lateinit var db: RestaurantDatabase
   // lateinit var db2: ReviewDatabase

    var recordset = ArrayList<RestaurantData>()
    var recordset2 = ArrayList<ReviewData>()

    var adapter = MenuAdapter(ArrayList<RestaurantData>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecyclerViewBinding.inflate(layoutInflater, container, false)

        db = RestaurantDatabase.getDatabase(requireContext())
     //   db2 = ReviewDatabase.getDatabase(requireContext())

        initRecyclerView()

        return binding!!.root
    }

    fun initRecyclerView() {
        var restaurantname = arguments?.getString("Restaurant")
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        adapter.itemClickListener = object : MenuAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                val intent = Intent(context, ReviewWritingActivity::class.java)
                intent.putExtra("Restaurant", adapter.items[position].RESTAURANT)
                intent.putExtra("Menu", adapter.items[position].MENU)
                startActivity(intent)
            }
        }
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        CoroutineScope(Dispatchers.IO).launch {
            getAllMenu(restaurantname!!)
        }
    }

    fun getAllMenu(restaurantname: String) {
        recordset = db.restaurantDao().getAllMenu(restaurantname) as ArrayList<RestaurantData>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }
}