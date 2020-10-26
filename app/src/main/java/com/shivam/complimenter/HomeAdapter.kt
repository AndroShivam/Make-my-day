package com.shivam.complimenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.textview.MaterialTextView

class HomeAdapter(val options: FirestoreRecyclerOptions<Post>) :
    FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_home, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.user_message.text = model.message_text
        holder.user_reply.text = model.reply_text
    }


}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val user_message: MaterialTextView = itemView.findViewById(R.id.user_message)
    val user_reply: MaterialTextView = itemView.findViewById(R.id.user_reply)
}

data class Post(
    var message_text: String = "message",
    var reply_text: String = "reply"
)

