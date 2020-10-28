package com.shivam.complimenter

import android.content.Intent
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
import com.shivam.complimenter.NewMessageFragment.Companion.RECEIVED
import com.shivam.complimenter.NewMessageFragment.Companion.USERS
import com.shivam.complimenter.databinding.FragmentMessagesBinding


class MessagesFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentMessagesBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var adapter: FirestoreRecyclerAdapter<ReceivedMessage, UserViewHolder>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUserID = firebaseAuth.currentUser?.uid.toString()

        val query: Query =
            firebaseFirestore.collection(USERS).document(currentUserID).collection(RECEIVED)

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<ReceivedMessage> =
            FirestoreRecyclerOptions.Builder<ReceivedMessage>()
                .setQuery(query, ReceivedMessage::class.java)
                .build()

        adapter = MessagesAdapter(options = firestoreRecyclerOptions, listener = this)

        binding.messagesRv.setHasFixedSize(true)
        binding.messagesRv.adapter = adapter

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

        val senderUserName: String? = documentSnapshot.getString("senderUserName")
        val senderMessage: String? = documentSnapshot.getString("senderMessage")
        val senderUserID: String? = documentSnapshot.getString("senderUserID")
        val senderEmoji: String? = documentSnapshot.getString("senderEmoji")


        val action = MessagesFragmentDirections.actionNavMessagesToMessageDetailFragment(
            position = position,
            senderUserName = senderUserName,
            senderUserID = senderUserID,
            senderMessage = senderMessage,
            senderEmoji = senderEmoji
        )
        view?.findNavController()?.navigate(action)
    }

}

