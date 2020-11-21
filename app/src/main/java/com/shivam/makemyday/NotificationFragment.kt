package com.shivam.makemyday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.shivam.makemyday.FirebaseUser.Companion.currentUserID
import com.shivam.makemyday.FirebaseUser.Companion.firebaseFirestore
import com.shivam.makemyday.NewMessageFragment.Companion.REPLIED
import com.shivam.makemyday.NewMessageFragment.Companion.USERS
import com.shivam.makemyday.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var adapter: FirestoreRecyclerAdapter<RepliedMessage, RepliedViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)


        val query: Query =
            firebaseFirestore.collection(USERS).document(currentUserID.toString())
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