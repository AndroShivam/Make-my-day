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
import com.shivam.complimenter.NewMessageFragment.Companion.POSTS
import com.shivam.complimenter.NewMessageFragment.Companion.REPLIED
import com.shivam.complimenter.NewMessageFragment.Companion.USERS
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


        binding.messageText.text = args?.message

        binding.messageDetailButton.setOnClickListener {
            val reply = binding.messageDetailEditTxt.text.toString()
            reply(message, reply, sender)
        }

        return binding.root
    }

    private fun reply(messageText: String?, replyText: String, senderID: String?) {

        val map = hashMapOf(
            "reply_text" to replyText,
            "message_text" to messageText
        )

        firebaseFirestore.collection(USERS).document(senderID!!)
            .collection(REPLIED).document().set(map)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseFirestore.collection(POSTS).document().set(map)
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