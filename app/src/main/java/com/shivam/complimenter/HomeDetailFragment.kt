package com.shivam.complimenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.shivam.complimenter.databinding.FragmentHomeDetailBinding

class HomeDetailFragment : Fragment() {

    private lateinit var binding: FragmentHomeDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_detail, container, false)

        val args = arguments?.let { HomeDetailFragmentArgs.fromBundle(it) }

        val message: String? = args?.message
        val reply: String? = args?.reply


        binding.homeDetailMessage.text = message
        binding.homeDetailReply.text = reply


        return binding.root
    }

}