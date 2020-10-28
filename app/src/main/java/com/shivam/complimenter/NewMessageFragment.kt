package com.shivam.complimenter

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
import com.shivam.complimenter.databinding.FragmentNewMessageBinding
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
    private var selectedEmoji: String? = null


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

            if (!TextUtils.isEmpty(message) && !selectedEmoji.isNullOrEmpty())
                storeToFireStore(message, userName!!, selectedEmoji!!)
            else if (TextUtils.isEmpty(message)) {
                Toast.makeText(requireContext(), "message field can't be empty", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedEmoji.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "please select an emoji", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "please select an emoji and enter message",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }



        binding.smiling.setOnClickListener(this)
        binding.blinking.setOnClickListener(this)
        binding.hugeSmile.setOnClickListener(this)
        binding.blank.setOnClickListener(this)
        binding.sad.setOnClickListener(this)
        binding.reallySad.setOnClickListener(this)
        binding.crying.setOnClickListener(this)
        binding.angry.setOnClickListener(this)

        return binding.root
    }


    private fun storeToFireStore(message: String, userName: String, emoji: String) {

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

    private fun setReceive(userName: String, message: String, emoji: String) {

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
                selectedEmoji = "smiling"
            }
            R.id.blinking -> {
                selectImage(binding.blinking)
                selectedEmoji = "blinking"
            }
            R.id.huge_smile -> {
                selectImage(binding.hugeSmile)
                selectedEmoji = "huge_smile"
            }
            R.id.blank -> {
                selectImage(binding.blank)
                selectedEmoji = "blank"
            }
            R.id.sad -> {
                selectImage(binding.sad)
                selectedEmoji = "sad"
            }
            R.id.really_sad -> {
                selectImage(binding.reallySad)
                selectedEmoji = "really_sad"
            }
            R.id.crying -> {
                selectImage(binding.crying)
                selectedEmoji = "crying"
            }
            R.id.angry -> {
                selectImage(binding.angry)
                selectedEmoji = "angry"
            }
            else -> Toast.makeText(requireContext(), "idk", Toast.LENGTH_SHORT).show()
        }
    }


    private fun selectImage(image: ImageView) {

        when (image.id) {
            R.id.smiling -> {
                binding.smiling.setImageResource(R.drawable.smiling_filled)
                binding.blinking.setImageResource(R.drawable.blinking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.blank.setImageResource(R.drawable.blank)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.blinking -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.blinking.setImageResource(R.drawable.blinking_filled)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.blank.setImageResource(R.drawable.blank)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.huge_smile -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.blinking.setImageResource(R.drawable.blinking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile_filled)
                binding.blank.setImageResource(R.drawable.blank)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.blank -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.blinking.setImageResource(R.drawable.blinking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.blank.setImageResource(R.drawable.blank_filled)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.sad -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.blinking.setImageResource(R.drawable.blinking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.blank.setImageResource(R.drawable.blank)
                binding.sad.setImageResource(R.drawable.sad_filled)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }
            R.id.really_sad -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.blinking.setImageResource(R.drawable.blinking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.blank.setImageResource(R.drawable.blank)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad_filled)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.crying -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.blinking.setImageResource(R.drawable.blinking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.blank.setImageResource(R.drawable.blank)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying_filled_new)
                binding.angry.setImageResource(R.drawable.angry)
            }

            R.id.angry -> {
                binding.smiling.setImageResource(R.drawable.smiling)
                binding.blinking.setImageResource(R.drawable.blinking)
                binding.hugeSmile.setImageResource(R.drawable.huge_smile)
                binding.blank.setImageResource(R.drawable.blank)
                binding.sad.setImageResource(R.drawable.sad)
                binding.reallySad.setImageResource(R.drawable.really_sad)
                binding.crying.setImageResource(R.drawable.crying)
                binding.angry.setImageResource(R.drawable.angry_filled)
            }
        }
    }

}