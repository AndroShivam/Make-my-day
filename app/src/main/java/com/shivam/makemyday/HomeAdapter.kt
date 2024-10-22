package com.shivam.makemyday

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.FirebaseFirestore
import com.mikhaellopez.circularimageview.CircularImageView
import java.sql.Timestamp
import java.util.*

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
        holder.emoji.text = getEmojiFromUnicode(model.emoji)
        holder.dateTime.text = model.date

        Glide.with(holder.itemView).load(model.senderProfilePicture).into(holder.userProfilePicture)
    }
}

class PostViewHolder(
    itemView: View,
    private val options: FirestoreRecyclerOptions<Post>,
    val listener: OnItemClickListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val userName: MaterialTextView = itemView.findViewById(R.id.home_username)
    val userMessage: MaterialTextView = itemView.findViewById(R.id.home_user_message)
    val userProfilePicture: CircularImageView = itemView.findViewById(R.id.home_image)
    val emoji: MaterialTextView = itemView.findViewById(R.id.home_emoji)
    val dateTime: MaterialTextView = itemView.findViewById(R.id.home_time)

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

private fun getEmojiFromUnicode(unicode: String): String {
    val unicodeInt: Int = Integer.parseInt(unicode)
    return String(Character.toChars(unicodeInt))
}

data class Post(
    var username: String = "username",
    var message: String = "message",
    var senderID: String = "senderID",
    var senderProfilePicture: String = "senderProfilePicture",
    var reply: String = "reply",
    var replierID: String = "replierID",
    var emoji: String = "emoji",
    var date: String? = "date"
)

