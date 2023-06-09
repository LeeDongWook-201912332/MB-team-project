package com.jiwon.mpteam_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jiwon.mpteam_layout.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {
    private var binding: FragmentReviewBinding? = null
    private lateinit var adapter: ReviewAdapter
    private lateinit var rdb: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initRecyclerView() {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerView?.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        rdb = Firebase.database.getReference("Reviews")
        val query = rdb.limitToLast(50)
        val options = FirebaseRecyclerOptions.Builder<ReviewData>()
            .setQuery(query, ReviewData::class.java)
            .build()
        adapter = ReviewAdapter(options)
        binding?.recyclerView?.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
