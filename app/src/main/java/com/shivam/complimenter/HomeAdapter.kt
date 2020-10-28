package com.shivam.complimenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.textview.MaterialTextView

class HomeAdapter(
    val options: FirestoreRecyclerOptions<Post>,
    val listener: OnItemClickListener
) :
    FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_home, parent, false)
        return PostViewHolder(view, options, listener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.userName.text = model.username
        holder.userMessage.text = model.message
    }
}

class PostViewHolder(
    itemView: View,
    private val options: FirestoreRecyclerOptions<Post>,
    val listener: OnItemClickListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val userName: MaterialTextView = itemView.findViewById(R.id.home_username)
    val userMessage: MaterialTextView = itemView.findViewById(R.id.home_user_message)

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

data class Post(
    var username: String = "username",
    var message: String = "message",
    var reply: String = "reply",
    var emoji: String = "emoji"
)

