package com.shivam.complimenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot


class MessagesAdapter(
    val options: FirestoreRecyclerOptions<ReceivedMessage>,
    val listener: OnItemClickListener
) :
    FirestoreRecyclerAdapter<ReceivedMessage, UserViewHolder>(options) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_messages, parent, false)
        return UserViewHolder(view, listener, options)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: ReceivedMessage) {
        holder.message.text = model.message
        holder.sender.text = model.sender
    }
}

class UserViewHolder(
    itemView: View,
    val listener: OnItemClickListener,
    private val options: FirestoreRecyclerOptions<ReceivedMessage>
) :
    RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var message: TextView = itemView.findViewById(R.id.message)
    var sender: TextView = itemView.findViewById(R.id.sender_name)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        listener.onItemClick(
            documentSnapshot = options.snapshots.getSnapshot(adapterPosition),
            position = adapterPosition
        )
    }
}


interface OnItemClickListener {
    fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
}

data class ReceivedMessage(
    var message: String = "message",
    var sender: String = "sender"
)