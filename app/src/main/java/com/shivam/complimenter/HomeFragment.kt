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
import com.shivam.complimenter.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.random.Random


class HomeFragment : Fragment(), View.OnClickListener {

    companion object {
        const val USERS = "Users"
        const val SENT = "Sent"
        const val RECEIVED = "Received"
        const val REPLIED = "Replied"
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUserID: String
    private lateinit var firebaseFirestore: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        currentUserID = firebaseAuth.currentUser?.uid.toString()

        binding.makeMyDay.setOnClickListener {
            val message = binding.homeMessage.text.toString()
            storeToFireStore(message)
        }


        binding.smiling.setOnClickListener(this)
        binding.blinking.setOnClickListener(this)
        binding.bigSmile.setOnClickListener(this)
        binding.blank.setOnClickListener(this)
        binding.sad.setOnClickListener(this)
        binding.reallySad.setOnClickListener(this)
        binding.crying.setOnClickListener(this)
        binding.angry.setOnClickListener(this)

        return binding.root
    }


    private fun storeToFireStore(message: String) {

        val map = hashMapOf("message" to message)

        firebaseFirestore.collection(USERS).document(currentUserID).collection(SENT).document()
            .set(map).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setReceive(message)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error : ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setReceive(message: String) {

        val list: ArrayList<String> = ArrayList()

        firebaseFirestore.collection(USERS).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userID = document.getString("user_id")
                    list.add(userID.toString())
                }

                val random = Random.nextInt(documents.size())

                val map = hashMapOf("message" to message, "sender" to currentUserID)

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
        when (view?.id) {
            R.id.smiling -> binding.smiling.setImageResource(R.drawable.smiling_filled)
            R.id.blinking -> binding.smiling.setImageResource(R.drawable.blinking_filled)
            R.id.big_smile -> binding.smiling.setImageResource(R.drawable.big_smile_filled)
            R.id.blank -> binding.smiling.setImageResource(R.drawable.blank_filled)
            R.id.sad -> binding.smiling.setImageResource(R.drawable.sad_filled)
            R.id.really_sad -> binding.smiling.setImageResource(R.drawable.really_sad_filled)
            R.id.crying -> binding.smiling.setImageResource(R.drawable.crying_filled_new)
            R.id.angry -> binding.smiling.setImageResource(R.drawable.angry_filled)
        }
    }

}