package com.shivam.makemyday

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class NotificationAdapter(
    val options: FirestoreRecyclerOptions<RepliedMessage>,
    val listener: OnItemClickListener
) :
    FirestoreRecyclerAdapter<RepliedMessage, RepliedViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepliedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_notification, parent, false)
        return RepliedViewHolder(view, options, listener)
    }

    override fun onBindViewHolder(holder: RepliedViewHolder, position: Int, model: RepliedMessage) {
        holder.userName.text =
            holder.context.applicationContext.getString(R.string.replied, model.username)
    }

}

class RepliedViewHolder(
    itemView: View,
    private val options: FirestoreRecyclerOptions<RepliedMessage>,
    val listener: OnItemClickListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val userName: TextView = itemView.findViewById(R.id.notification_username)
    val context: Context = itemView.context

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


data class RepliedMessage(
    val username: String = "username",
    val message: String = "message",
    val reply: String = "reply",
    val emoji: String = "emoji"
)