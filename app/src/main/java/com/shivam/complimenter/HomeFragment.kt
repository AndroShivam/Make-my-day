package com.shivam.complimenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shivam.complimenter.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: FirestoreRecyclerAdapter<Post, PostViewHolder>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.homeFab.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_nav_home_to_newMessageFragment)
        }

        val query =
            FirebaseFirestore.getInstance().collection("Posts")


        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Post> =
            FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post::class.java)
                .build()

        adapter = HomeAdapter(options = firestoreRecyclerOptions, listener = this)

        binding.tempRv.setHasFixedSize(true)
        binding.tempRv.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {

        val message: String? = documentSnapshot.getString("message")
        val reply: String? = documentSnapshot.getString("reply")

        val action = HomeFragmentDirections.actionNavHomeToHomeDetailFragment(
            message = message,
            reply = reply
        )

        view?.findNavController()?.navigate(action)
    }

}

interface OnItemClickListener {
    fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
}