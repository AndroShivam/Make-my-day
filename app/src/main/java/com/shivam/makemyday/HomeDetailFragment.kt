package com.shivam.makemyday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.shivam.makemyday.databinding.FragmentHomeDetailBinding

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
        val senderProfilePicture: String? = args?.senderProfilePicture
        val replierProfilePicture: String? = args?.replierProfilePicture

        binding.homeDetailMessage.text = message
        binding.homeDetailReply.text = reply


        Glide.with(this).load(senderProfilePicture).into(binding.homeDetailMessageImage)
        Glide.with(this).load(replierProfilePicture).into(binding.homeDetailReplyImage)
        return binding.root
    }

}