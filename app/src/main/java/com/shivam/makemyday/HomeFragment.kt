package com.shivam.makemyday

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shivam.makemyday.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: FirestoreRecyclerAdapter<Post, PostViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val query =
            FirebaseFirestore.getInstance().collection("Posts")
                .orderBy("date", Query.Direction.DESCENDING)

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Post> =
            FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post::class.java)
                .build()

        adapter = HomeAdapter(options = firestoreRecyclerOptions, listener = this)

        binding.tempRv.setHasFixedSize(true)
        binding.tempRv.adapter = adapter


        binding.homeFab.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_nav_home_to_newMessageFragment)
        }

        binding.homeSwipeRefresh.setOnRefreshListener {
            binding.homeSwipeRefresh.isRefreshing = true
            adapter.notifyDataSetChanged()
            binding.homeSwipeRefresh.isRefreshing = false
        }

        return binding.root
    }


    override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {

        val message: String? = documentSnapshot.getString("message")
        val reply: String? = documentSnapshot.getString("reply")
        val senderProfilePicture: String? = documentSnapshot.getString("senderProfilePicture")
        val replierProfilePicture: String? = documentSnapshot.getString("replierProfilePicture")

        val action = HomeFragmentDirections.actionNavHomeToHomeDetailFragment(
            message = message,
            reply = reply,
            senderProfilePicture = senderProfilePicture,
            replierProfilePicture = replierProfilePicture
        )

        view?.findNavController()?.navigate(action)
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

interface OnItemClickListener {
    fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
}