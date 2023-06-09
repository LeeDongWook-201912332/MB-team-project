package com.jiwon.mpteam_layout

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiwon.mpteam_layout.databinding.FragmentRestaurantBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.nio.file.Files.delete

class RestaurantFragment : Fragment() {
    var binding: FragmentRestaurantBinding ?= null

    lateinit var db: RestaurantDatabase
    var recordset = ArrayList<RestaurantData>()
    var adapter = RestaurantAdapter(ArrayList<RestaurantData>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRestaurantBinding.inflate(layoutInflater, container, false)

        db = RestaurantDatabase.getDatabase(requireContext())

        initDB()
        init()
        initRecyclerView()

        return binding!!.root
    }

    private fun initDB() {
        val dbfile = requireContext().getDatabasePath("restaurantdb")
        if(!dbfile.parentFile.exists()) {
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()) {
            val file = resources.openRawResource(R.raw.restaurantdb)
            val fileSize = file.available()
            val buffer = ByteArray(fileSize)
            file.read(buffer)
            file.close()
            dbfile.createNewFile()
            val output = FileOutputStream(dbfile)
            output.write(buffer)
            output.close()
        }
    }

    fun init() {
        binding!!.searchBtn.setOnClickListener {
            var restaurantname = binding!!.searchText.text.toString()
            restaurantname = restaurantname + "%"
            CoroutineScope(Dispatchers.IO).launch {
                findRestaurant(restaurantname)
            }
        }
        binding!!.apply {
            menuSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val location = locationSpinner.selectedItem.toString()
                    val category = menuSpinner.selectedItem.toString()
                    if(location == "전체" && category == "전체") {
                        CoroutineScope(Dispatchers.IO).launch {
                            sortByRestaurant()
                        }
                    } else if(location == "전체" && category != "전체") {
                        CoroutineScope(Dispatchers.IO).launch {
                            findRestaurantByCategory(category)
                        }
                    } else if(location != "전체" && category != "전체") {
                        CoroutineScope(Dispatchers.IO).launch {
                            findRestaurantByLocationAndCategory(location, category)
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

            locationSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val location = locationSpinner.selectedItem.toString()
                    val category = menuSpinner.selectedItem.toString()
                    if(location == "전체" && category == "전체") {
                        CoroutineScope(Dispatchers.IO).launch {
                            sortByRestaurant()
                        }
                    } else if(location != "전체" && category == "전체") {
                        CoroutineScope(Dispatchers.IO).launch {
                            findRestaurantByLocation(location)
                        }
                    } else if(location != "전체" && category != "전체") {
                        CoroutineScope(Dispatchers.IO).launch {
                            findRestaurantByLocationAndCategory(location, category)
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

//            insertBtn.setOnClickListener {
//                val restaurant = pNameEdit.text.toString()
//                val address = pAddressEdit.text.toString()
//                val operateHour = pOperateHourEdit.text.toString()
//                val breakTime = pBreakEdit.text.toString()
//                val lastOrder = pLastOrderEdit.text.toString()
//                val menu = pMenuEdit.text.toString()
//                val price = pPriceEdit.text.toString()
//                val location = pLocationEdit.text.toString()
//                val category = pCategoryEdit.text.toString()
//                val rid = pRidEdit.text.toString().toInt()
//                val latitude = pLatitudeEdit.text.toString()
//                val longitude = pLongitudeEdit.text.toString()
//                val rating = pLongitudeEdit.text.toString().toInt()
//                val product = RestaurantData(0, restaurant, address, operateHour, breakTime, lastOrder, menu, price, location, category, rid, latitude, longitude, rating)
//                CoroutineScope(Dispatchers.IO).launch {
//                    insert(product)
//                }
//                clearEditText()
//            }
//            deleteBtn.setOnClickListener {
//                val restaurant = pNameEdit.text.toString()
//                val address = pAddressEdit.text.toString()
//                val operateHour = pOperateHourEdit.text.toString()
//                val breakTime = pBreakEdit.text.toString()
//                val lastOrder = pLastOrderEdit.text.toString()
//                val menu = pMenuEdit.text.toString()
//                val price = pPriceEdit.text.toString()
//                val location = pLocationEdit.text.toString()
//                val category = pCategoryEdit.text.toString()
//                val rid = pRidEdit.text.toString().toInt()
//                val latitude = pLatitudeEdit.text.toString()
//                val longitude = pLongitudeEdit.text.toString()
//                val product = RestaurantData(0, restaurant, address, operateHour, breakTime, lastOrder, menu, price, location, category, rid, latitude, longitude)
//                CoroutineScope(Dispatchers.IO).launch {
//                    delete(product)
//                }
//                clearEditText()
//            }
        }
    }

    fun initRecyclerView() {
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        adapter.itemClickListener = object : RestaurantAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {

                val intent = Intent(context, DetailRestaurantActivity::class.java)
                intent.putExtra("Restaurant", adapter.items[position].RESTAURANT)
                startActivity(intent)
            }
        }
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        CoroutineScope(Dispatchers.IO).launch {
            sortByRestaurant()
        }
    }

    fun findRestaurant(restaurantname: String) {
        recordset = db.restaurantDao().findRestaurant(restaurantname) as ArrayList<RestaurantData>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    fun findRestaurantByCategory(menutype: String) {
        recordset = db.restaurantDao().findRestaurantByMenuType(menutype) as ArrayList<RestaurantData>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    fun findRestaurantByLocation(location: String) {
        recordset = db.restaurantDao().findRestaurantByLocation(location) as ArrayList<RestaurantData>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    fun findRestaurantByLocationAndCategory(location: String, category: String) {
        recordset = db.restaurantDao().findRestaurantByLocationAndCategory(location, category) as ArrayList<RestaurantData>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

//    fun insert(product: RestaurantData) {
//        db.restaurantDao().insertProduct(product)
//        sortByRestaurant()
//    }
//
//    fun delete(product: RestaurantData) {
//        db.restaurantDao().deleteProduct(product)
//        sortByRestaurant()
//    }

    fun sortByRestaurant() {
        recordset = db.restaurantDao().sortByRestaurant() as ArrayList<RestaurantData>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }

    }

//    fun clearEditText() {
//        binding!!.apply {
//            pIdEdit.text.clear()
//            pNameEdit.text.clear()
//            pAddressEdit.text.clear()
//            pOperateHourEdit.text.clear()
//            pBreakEdit.text.clear()
//            pLastOrderEdit.text.clear()
//            pMenuEdit.text.clear()
//            pPriceEdit.text.clear()
//            pLocationEdit.text.clear()
//            pCategoryEdit.text.clear()
//        }
//    }
}