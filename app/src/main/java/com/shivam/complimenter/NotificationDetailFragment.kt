package com.shivam.complimenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.shivam.complimenter.databinding.FragmentNotificationDetailBinding


class NotificationDetailFragment : Fragment() {

    private lateinit var binding: FragmentNotificationDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notification_detail,
            container,
            false
        )

        val args = arguments?.let { NotificationDetailFragmentArgs.fromBundle(it) }

        val message: String? = args?.message
        val reply: String? = args?.reply

        binding.notificationDetailMessage.text = message
        binding.notificationDetailReply.text = reply

        return binding.root
    }
}