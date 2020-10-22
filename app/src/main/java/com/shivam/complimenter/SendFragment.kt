package com.shivam.complimenter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.shivam.complimenter.databinding.FragmentSendBinding
import kotlin.random.Random
import kotlin.random.asJavaRandom


class SendFragment : Fragment() {

    companion object {
         const val USERS = "Users"
         const val SENT = "Sent"
         const val RECEIVED = "Received"
        private const val TAG = "SendFragment"
    }

    private lateinit var binding: FragmentSendBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUserID: String
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        currentUserID = firebaseAuth.currentUser?.uid.toString()

        binding.sentButton.setOnClickListener {
            val message = binding.sendMessage.text.toString()
            storeToFireStore(message)
        }
        return binding.root
    }

    private fun storeToFireStore(message: String) {

        val map = hashMapOf("message" to message)
        val field: Map<String, Any> = hashMapOf("user_id" to currentUserID)

        firebaseFirestore.collection(USERS).document(currentUserID).collection(SENT).document()
            .set(map).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseFirestore.collection(USERS).document(currentUserID).set(field)
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

                val map: HashMap<String, Any> = HashMap()
                map["message"] = message
                map["sent_by"] = currentUserID



                firebaseFirestore.collection(USERS).document(list[random]).collection(RECEIVED)
                    .document().set(map)

            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "document : ${it.message.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

}

data class Message(
    val text: String? = null
)