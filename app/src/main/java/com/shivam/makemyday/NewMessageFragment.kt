package com.shivam.makemyday

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shivam.makemyday.databinding.FragmentNewMessageBinding
import kotlin.random.Random


class NewMessageFragment : Fragment(), View.OnClickListener {

    companion object {
        const val USERS = "Users"
        const val SENT = "Sent"
        const val RECEIVED = "Received"
        const val REPLIED = "Replied"
        const val POSTS = "Posts"
    }

    private lateinit var binding: FragmentNewMessageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUserID: String
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var selectedEmoji: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_message, container, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val userName = sharedPreferences.getString("userName", "Friend")
        binding.newMessageWelcome.text = userName

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        currentUserID = firebaseAuth.currentUser?.uid.toString()

        binding.newMessageButton.setOnClickListener {
            val message = binding.newMessageEditText.text.toString()

            if (!TextUtils.isEmpty(message)) // check emoji null here
                storeToFireStore(message, userName!!, selectedEmoji!!)
            else {
                Toast.makeText(requireContext(), "message field can't be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        binding.smiling.setOnClickListener(this)
        binding.winking.setOnClickListener(this)
        binding.hugeSmile.setOnClickListener(this)
        binding.neutral.setOnClickListener(this)
        binding.sad.setOnClickListener(this)
        binding.reallySad.setOnClickListener(this)
        binding.crying.setOnClickListener(this)
        binding.angry.setOnClickListener(this)

        return binding.root
    }


    private fun storeToFireStore(message: String, userName: String, emoji: Int) {

        val map = hashMapOf(
            "userName" to userName,
            "message" to message,
            "emoji" to emoji
        )

        firebaseFirestore.collection(USERS).document(currentUserID).collection(SENT).document()
            .set(map).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setReceive(userName, message, emoji)
                    view?.findNavController()?.navigate(R.id.action_newMessageFragment_to_nav_home)
                    Toast.makeText(requireContext(), "Message Sent!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error : ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setReceive(userName: String, message: String, emoji: Int) {

        val list: ArrayList<String> = ArrayList()

        firebaseFirestore.collection(USERS).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userID = document.getString("user_id")
                    list.add(userID.toString())
                }

                val random = Random.nextInt(documents.size())

                val map = hashMapOf(
                    "senderUserName" to userName,
                    "senderUserID" to currentUserID,
                    "senderMessage" to message,
                    "senderEmoji" to emoji
                )

                firebaseFirestore.collection(USERS).document(list[random]).collection(RECEIVED)
                    .document().set(map)

            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error : ${it.message.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.smiling -> {
                selectImage(binding.smiling)
                selectedEmoji = 0x1F642
            }
            R.id.winking -> {
                selectImage(binding.winking)
                selectedEmoji = 0x1F609
            }
            R.id.huge_smile -> {
                selectImage(binding.hugeSmile)
                selectedEmoji = 0x1F604
            }
            R.id.neutral -> {
                selectImage(binding.neutral)
                selectedEmoji = 0x1F610
            }
            R.id.sad -> {
                selectImage(binding.sad)
                selectedEmoji = 0x1F641
            }
            R.id.really_sad -> {
                selectImage(binding.reallySad)
                selectedEmoji = 0x2639
            }
            R.id.crying -> {
                selectImage(binding.crying)
                selectedEmoji = 0x1F622
            }
            R.id.angry -> {
                selectImage(binding.angry)
                selectedEmoji = 0x1F620 // 	U+1F621
            }
            else -> Toast.makeText(requireContext(), "idk", Toast.LENGTH_SHORT).show()
        }
    }


    private fun selectImage(image: ImageView) {

        when (image.id) {
            R.id.smiling -> {
                binding.smiling.setImageResource(R.drawable.smiling_filled)
                binding.winking.setImageResource(R.drawable.winking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.neutral.setImageResource(R.drawable.neutral)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.winking -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.winking.setImageResource(R.drawable.winking_filled)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.neutral.setImageResource(R.drawable.neutral)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.huge_smile -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.winking.setImageResource(R.drawable.winking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile_filled)
                binding.neutral.setImageResource(R.drawable.neutral)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.neutral -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.winking.setImageResource(R.drawable.winking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.neutral.setImageResource(R.drawable.blank_filled)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.sad -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.winking.setImageResource(R.drawable.winking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.neutral.setImageResource(R.drawable.neutral)
                binding.sad.setImageResource(R.drawable.sad_filled)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }
            R.id.really_sad -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.winking.setImageResource(R.drawable.winking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.neutral.setImageResource(R.drawable.neutral)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad_filled)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.crying -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.winking.setImageResource(R.drawable.winking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.neutral.setImageResource(R.drawable.neutral)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying_filled_new)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.angry -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.winking.setImageResource(R.drawable.winking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.neutral.setImageResource(R.drawable.neutral)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry_filled)
            }
        }
    }

}