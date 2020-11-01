package com.shivam.makemyday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


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
        holder.senderUserName.text = model.senderUserName
        holder.senderMessage.text = model.senderMessage
    }
}

class UserViewHolder(
    itemView: View,
    val listener: OnItemClickListener,
    private val options: FirestoreRecyclerOptions<ReceivedMessage>
) :
    RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    var senderUserName: TextView = itemView.findViewById(R.id.sender_name)
    var senderMessage: TextView = itemView.findViewById(R.id.sender_message)


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

data class ReceivedMessage(
    var senderUserName: String = "sender_username",
    var senderUserID: String = "sender_user_id",
    var senderMessage: String = "message"
)