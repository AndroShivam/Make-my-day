package com.shivam.complimenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class NotificationAdapter(val options: FirestoreRecyclerOptions<RepliedMessage>) :
    FirestoreRecyclerAdapter<RepliedMessage, RepliedViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepliedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_notification, parent, false)
        return RepliedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepliedViewHolder, position: Int, model: RepliedMessage) {
        holder.repliedMessage.text = model.replied_text
    }

}

class RepliedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val repliedMessage: TextView = itemView.findViewById(R.id.notification_text)
}


data class RepliedMessage(
    val replied_text: String = "replied"
)