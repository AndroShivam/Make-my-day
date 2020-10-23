package com.shivam.complimenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class MessagesAdapter(
    options: FirestoreRecyclerOptions<ReceivedMessage>,
    listener: OnItemClickListener
) :
    FirestoreRecyclerAdapter<ReceivedMessage, UserViewHolder>(options) {

    val listener: OnItemClickListener = listener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_messages, parent, false)
        return UserViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: ReceivedMessage) {
        holder.message.text = model.message
        holder.sender.text = model.sender
    }
}

class UserViewHolder(itemView: View, listener: OnItemClickListener) :
    RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var message: TextView = itemView.findViewById(R.id.message)
    var sender: TextView = itemView.findViewById(R.id.sender_name)

    private var listener: OnItemClickListener


    init {
        itemView.setOnClickListener(this)
        this.listener = listener
    }

    override fun onClick(v: View?) {
        listener.onItemClick(position = adapterPosition)
    }
}


interface OnItemClickListener {
    fun onItemClick(position: Int)
}

data class ReceivedMessage(
    var message: String = "message",
    var sender: String = "sender"
)