package com.shivam.makemyday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.shivam.makemyday.NewMessageFragment.Companion.POSTS
import com.shivam.makemyday.NewMessageFragment.Companion.REPLIED
import com.shivam.makemyday.NewMessageFragment.Companion.USERS
import com.shivam.makemyday.databinding.FragmentMessageDetailBinding
import java.text.DateFormat
import java.util.*

class MessageDetailFragment : Fragment() {

    private lateinit var binding: FragmentMessageDetailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var currentUserID: String
    private lateinit var senderProfilePicture: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_message_detail, container, false)


        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        currentUserID = firebaseAuth.currentUser?.uid.toString()

        val args = arguments?.let { MessageDetailFragmentArgs.fromBundle(it) }
        val senderUserName: String? = args?.senderUserName
        val senderUserID: String? = args?.senderUserID
        val senderMessage: String? = args?.senderMessage
        val senderEmoji: String? = args?.senderEmoji


        binding.messageText.text = args?.senderMessage


        firebaseFirestore.collection(USERS).document(senderUserID.toString()).get()
            .addOnSuccessListener { documentSnapshot ->
                senderProfilePicture = documentSnapshot.getString("image_url").toString()
            }


        binding.messageDetailButton.setOnClickListener {
            val reply = binding.messageDetailEditTxt.text.toString()
            reply(senderUserName!!, senderUserID, senderMessage!!, senderEmoji!!, reply)
        }

        return binding.root
    }

    private fun reply(
        userName: String,
        senderID: String?,
        messageText: String?,
        emoji: String?,
        replyText: String,
    ) {

        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance().format(calendar.time)


        val map = hashMapOf(
            "username" to userName,
            "message" to messageText,
            "senderID" to senderID,
            "senderProfilePicture" to senderProfilePicture,
            "reply" to replyText,
            "replierID" to currentUserID,
            "emoji" to emoji,
            "created" to currentDate
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