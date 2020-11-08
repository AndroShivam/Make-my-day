package com.shivam.makemyday

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.shivam.makemyday.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<UserViewModel>()
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            login(email, password)
        }

        binding.loginGotoReg.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    private fun login(email: String, password: String) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            binding.loginProgressbar.visibility = View.VISIBLE

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        view?.findNavController()
                            ?.navigate(R.id.action_loginFragment_to_mainActivity)
                    } else {
                        binding.loginProgressbar.visibility = View.INVISIBLE
                        Toast.makeText(
                            requireContext(),
                            "Error ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } else {
            Snackbar.make(requireView(), "Please fill all the fields", Snackbar.LENGTH_LONG).show()
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel.authenticationState.observe(viewLifecycleOwner, { authenticationState ->
//            when (authenticationState) {
//                UserViewModel.AuthenticationState.AUTHENTICATED -> view.findNavController()
//                    .navigate(R.id.mainActivity)
//            }
//        })
//    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null)
            view?.findNavController()?.navigate(R.id.mainActivity)
    }
}