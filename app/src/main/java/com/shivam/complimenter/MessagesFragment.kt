package com.shivam.complimenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shivam.complimenter.SendFragment.Companion.RECEIVED
import com.shivam.complimenter.SendFragment.Companion.USERS
import com.shivam.complimenter.databinding.FragmentMessagesBinding
import kotlinx.android.synthetic.main.items_messages.view.*
import org.w3c.dom.Text
import com.shivam.complimenter.MessagesFragment.UserViewHolder as UserViewHolder


class MessagesFragment : Fragment() {

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

        val query: Query = firebaseFirestore.collection(USERS).document(currentUserID).collection(RECEIVED)

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<ReceivedMessage> =
            FirestoreRecyclerOptions.Builder<ReceivedMessage>()
                .setQuery(query, ReceivedMessage::class.java)
                .build()

        adapter = object :
            FirestoreRecyclerAdapter<ReceivedMessage, UserViewHolder>(firestoreRecyclerOptions) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.items_messages, parent, false)
                return UserViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: UserViewHolder,
                position: Int,
                model: ReceivedMessage
            ) {

                holder.message.text = model.message
                holder.sent_by.text = model.sent_by
            }

        }

        binding.messagesRv.setHasFixedSize(true)
        binding.messagesRv.adapter = adapter

        return binding.root
    }

    private class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.message)
        var sent_by: TextView = itemView.findViewById(R.id.sent_by)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}

data class ReceivedMessage(
    var message: String = "message",
    var sent_by: String = "sent_by"
)