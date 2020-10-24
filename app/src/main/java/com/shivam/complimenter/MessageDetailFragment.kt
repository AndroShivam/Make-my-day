package com.shivam.complimenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shivam.complimenter.HomeFragment.Companion.REPLIED
import com.shivam.complimenter.HomeFragment.Companion.USERS
import com.shivam.complimenter.databinding.FragmentMessageDetailBinding

class MessageDetailFragment : Fragment() {

    private lateinit var binding: FragmentMessageDetailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_message_detail, container, false)


        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        val args = arguments?.let { MessageDetailFragmentArgs.fromBundle(it) }
        val message: String? = args?.message
        val sender: String? = args?.sender


        binding.messageDetailText.text = args?.message

        binding.messageDetailButton.setOnClickListener {
            val replyText = binding.messageDetailEditTxt.text.toString()
            reply(replyText, sender)
        }

        return binding.root
    }

    private fun reply(replyText: String, senderID: String?) {

        val map = hashMapOf("replied_text" to replyText)

        firebaseFirestore.collection(USERS).document(senderID!!)
            .collection(REPLIED).document().set(map)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Replied!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error : ${it.message.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }
}