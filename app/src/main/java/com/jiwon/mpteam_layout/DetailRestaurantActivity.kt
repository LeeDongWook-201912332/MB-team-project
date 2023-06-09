package com.jiwon.mpteam_layout

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jiwon.mpteam_layout.databinding.ActivityDetailRestaurantBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailRestaurantActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityDetailRestaurantBinding

    lateinit var db: RestaurantDatabase
    var recordset = ArrayList<RestaurantData>()
    var adapter = MenuAdapter(ArrayList<RestaurantData>())

//    lateinit var db2: ReviewDatabase
    var recordset2 = ArrayList<ReviewData>()

    val fragmentMenuRecyclerView = MenuRecyclerViewFragment()
    val fragmentReview = ReviewFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = RestaurantDatabase.getDatabase(this)
//        db2 = ReviewDatabase.getDatabase(this)

        val fm = supportFragmentManager
        var mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

        initLayout()
//        initRecyclerView()
    }

    private fun initLayout() {
        var restaurantname = intent.getStringExtra("Restaurant")
        var restaurantArray = arrayListOf<String>()  // 가게명을 담는 배열
        var categoryArray = arrayListOf<String>()  // 카테고리를 담는 배열
        var locationArray = arrayListOf<String>()  // 위치를 담는 배열
        var addressArray = arrayListOf<String>()  // 주소를 담는 배열
        var operateHourArray = arrayListOf<String>()  // 영업 시간을 담는 배열
        var breakTimeArray = arrayListOf<String?>()  // 브레이크 타임을 담는 배열
        var lastOrderArray = arrayListOf<String?>()  // 라스트오더를 담는 배열
        var ratingArray = arrayListOf<Int>()  // 평점을 담는 배열
        val DBjob = CoroutineScope(Dispatchers.IO).async {
            findRestaurant(restaurantname!!)

//            for(i in 0 until recordset.size) {
//                var restaurant = recordset[i].RESTAURANT
//                var category = recordset[i].CATEGORY
//                var location = recordset[i].LOCATION
//                var address = recordset[i].ADDRESS
//                var operateHour = recordset[i].OPERATEHOUR
//                var breakTime = recordset[i].BREAK
//                var lastOrder = recordset[i].LASTORDER
//
//                restaurantArray.add(restaurant)
//                categoryArray.add(category)
//                locationArray.add(location)
//                addressArray.add(address)
//                operateHourArray.add(operateHour)
//                breakTimeArray.add(breakTime)
//                lastOrderArray.add(lastOrder)
//            }

            restaurantArray.add(recordset[0].RESTAURANT)
            categoryArray.add(recordset[0].CATEGORY)
            locationArray.add(recordset[0].LOCATION)
            addressArray.add(recordset[0].ADDRESS)
            operateHourArray.add(recordset[0].OPERATEHOUR)
            breakTimeArray.add(recordset[0].BREAK)
            lastOrderArray.add(recordset[0].LASTORDER)

            Log.i("평점 테스트", "평점 테스트")
            for(i in 0 until recordset2.size) {
  //              ratingArray.add(recordset2[i].RATING)
  //              Log.i("평점 테스트", recordset2[i].RATING.toString())
            }
        }
        suspend fun setInformatin() {
            DBjob.await()

//            for(i in 0 until restaurantArray.size) {
//                var restaurant = restaurantArray[i]
//                var category = categoryArray[i]
//                var location = locationArray[i]
//                var address = addressArray[i]
//                var operateHour = operateHourArray[i]
//                var breakTime = breakTimeArray[i]
//                var lastOrder = lastOrderArray[i]
//                binding.apply {
//                    pTitle.setText(restaurant)
//                    pCategory.setText(category)
//                    pLocation.setText(location)
//                    pAddress.setText(address)
//                    pOperateHour.setText(operateHour)
//                    pBreak.setText(breakTime)
//                    pLastOrder.setText(lastOrder)
//                }
//            }

            var rating = 0.0f
            for(i in 0 until ratingArray.size) {
                var tempRating = ratingArray[i]
                rating += tempRating
            }

            rating = rating / recordset2.size

            binding.apply {
                pTitle.text = restaurantArray[0]
                pCategory.text = categoryArray[0]
                pLocation.text = locationArray[0]
                pAddress.text = addressArray[0]
                pOperateHour.text = operateHourArray[0]
                pBreak.text = breakTimeArray[0]
                pLastOrder.text = lastOrderArray[0]
                pRating.text = String.format("%.2f", rating)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            setInformatin()
        }

        var bundle = Bundle()
        bundle.putString("Restaurant", restaurantname)
        fragmentMenuRecyclerView.arguments = bundle
        fragmentReview.arguments = bundle

        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.detail_restaurant_frm, fragmentMenuRecyclerView)
        fragment.commit()
        binding.apply {
            menuBtn.setOnClickListener {
                if(!fragmentMenuRecyclerView.isVisible) {
                    val fragment = supportFragmentManager.beginTransaction()
                    fragment.replace(R.id.detail_restaurant_frm, fragmentMenuRecyclerView)
                    fragment.commit()
                }
            }
            reviewBtn.setOnClickListener {
                if(!fragmentReview.isVisible) {
                    val fragment = supportFragmentManager.beginTransaction()
                    fragment.replace(R.id.detail_restaurant_frm, fragmentReview)
                    fragment.commit()
                }
            }
        }
    }

    fun findRestaurant(restaurantname: String) {
        recordset = db.restaurantDao().findRestaurant(restaurantname) as ArrayList<RestaurantData>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
      //  recordset2 = db2.reviewDao().findProduct(restaurantname) as ArrayList<ReviewData>
    }

    override fun onMapReady(naverMap: NaverMap) {

        var restaurantname = intent.getStringExtra("Restaurant")
        var namearray = arrayListOf<String>()  // 가게명을 담을 배열
        var latlngarray = arrayListOf<LatLng>()  // 가게의 위도와 경도를 담을 배열

        val DBjob = CoroutineScope(Dispatchers.IO).async {

            recordset = db.restaurantDao().findRestaurant(restaurantname!!) as ArrayList<RestaurantData>

            var coord = LatLng(recordset[0].LATITUDE.toDouble(), recordset[0].LONGITUDE.toDouble())
            var name = recordset[0].RESTAURANT

            namearray.add(name)
            latlngarray.add(coord)
        }

        suspend fun genMarer() {  // 지도에 마커를 표시하는 메소드
            DBjob.await()  // 데이터베이스 정보를 모두 가져올 때까지 wait

            var coord = latlngarray[0]
            val cameraPosition = CameraPosition(coord, 16.0)
            naverMap.cameraPosition = cameraPosition

            var marker = Marker()
            marker.captionText = namearray[0]
            marker.captionRequestedWidth = 200
            marker.setCaptionAligns(Align.Top)
            marker.position = coord
            marker.map = naverMap
        }

        CoroutineScope(Dispatchers.Main).launch {
            genMarer()
        }
    }
}