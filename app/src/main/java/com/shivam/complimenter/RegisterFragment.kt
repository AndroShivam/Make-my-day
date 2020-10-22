package com.shivam.complimenter

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.shivam.complimenter.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.registerButton.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            val confirmPassword = binding.registerConfirmPassword.text.toString()

            register(email, password, confirmPassword)
        }

        binding.regGotoLogin.setOnClickListener {
            navController.navigate(R.id.loginFragment)
        }
        return binding.root
    }

    private fun register(email: String, password: String, confirmPassword: String) {

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
                confirmPassword
            )
        ) {
            if (password == confirmPassword) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            view?.findNavController()?.navigate(R.id.action_registerFragment_to_mainActivity)
                        } else {
                            Snackbar.make(
                                requireView(),
                                "Something went wrong! :(",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
            }else{
                Toast.makeText(requireContext(), "Passwords doesn't match", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(), "fields can't be empty", Toast.LENGTH_SHORT).show()
        }
    }
}