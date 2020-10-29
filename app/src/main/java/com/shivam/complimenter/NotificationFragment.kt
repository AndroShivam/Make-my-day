package com.shivam.complimenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shivam.complimenter.NewMessageFragment.Companion.REPLIED
import com.shivam.complimenter.NewMessageFragment.Companion.USERS
import com.shivam.complimenter.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var adapter: FirestoreRecyclerAdapter<RepliedMessage, RepliedViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        val currentUserID: String = firebaseAuth.currentUser?.uid.toString()

        val query: Query =
            firebaseFirestore.collection(USERS).document(currentUserID)
                .collection(REPLIED)

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<RepliedMessage> =
            FirestoreRecyclerOptions.Builder<RepliedMessage>()
                .setQuery(query, RepliedMessage::class.java)
                .build()

        adapter = NotificationAdapter(options = firestoreRecyclerOptions, listener = this)

        binding.notificationRv.setHasFixedSize(true)
        binding.notificationRv.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {
//        val username: String = "username",
//        val message: String = "message",
//        val reply: String = "reply",
//        val emoji: String = "emoji"

        val message: String? = documentSnapshot.getString("message")
        val reply: String? = documentSnapshot.getString("reply")

        val action =
            NotificationFragmentDirections.actionNavNotificationsToNotificationDetailFragment(
                message = message,
                reply = reply
            )

        view?.findNavController()?.navigate(action)
    }
}