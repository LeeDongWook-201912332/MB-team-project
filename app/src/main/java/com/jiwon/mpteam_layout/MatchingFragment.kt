package com.jiwon.mpteam_layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiwon.mpteam_layout.databinding.FragmentMatchingBinding

class MatchingFragment : Fragment() {
    lateinit var binding: FragmentMatchingBinding
    var matchingList = ArrayList<MatchingData>()

    var matchingAdapter: MatchingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMatchingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDummyData()

        matchingAdapter = MatchingAdapter(matchingList)
        binding.matchingRv.adapter = matchingAdapter
        binding.matchingRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


    }
    private fun initDummyData() {
        matchingList = arrayListOf(
            MatchingData(
                "exId",
                "exContent",
                "1"
            ), MatchingData(
                "exId2",
                "exContent2",
                "0"
            )
        )

//        matchingList.addAll(matchingList)
    }


}