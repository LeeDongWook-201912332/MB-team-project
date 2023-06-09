package com.jiwon.mpteam_layout

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jiwon.mpteam_layout.ReviewData
import com.jiwon.mpteam_layout.databinding.ActivityReviewWritingBinding
import java.text.SimpleDateFormat
import java.util.*

class ReviewWritingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewWritingBinding
    private lateinit var rdb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase Realtime Database의 "Reviews" 참조 가져오기
        rdb = Firebase.database.reference.child("Reviews")
        val query = rdb.limitToLast(50)
        val options = FirebaseRecyclerOptions.Builder<ReviewData>()
            .setQuery(query, ReviewData::class.java)
            .build()

        // 작성 완료 버튼 클릭 시 리뷰 데이터 저장
        val submitButton: Button = binding.addBtn
        submitButton.setOnClickListener {
            saveReview()
        }
    }

    private fun saveReview() {
        // 사용자가 입력한 데이터 가져오기
        val date: String = getCurrentDate()
        val restaurant : String = binding.restaurantName.text.toString().trim()
        val menu: String = binding.menuName.text.toString().trim()
        val rating : String = binding.ratingSpinner.selectedItem.toString()
        val content: String = binding.contentEdit.text.toString().trim()
        // Firebase Realtime Database에 리뷰 데이터 저장
        val reviewData = ReviewData(restaurant ,date, menu, rating, content)
        val reviewKey = rdb.push().key
        if (reviewKey != null) {
            rdb.child(reviewKey).setValue(reviewData)
                .addOnSuccessListener {
                    // 리뷰 저장 성공
                    finish()
                }
                .addOnFailureListener {
                    // 리뷰 저장 실패
                    // 실패 처리 로직 추가
                }
        }
    }

    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
