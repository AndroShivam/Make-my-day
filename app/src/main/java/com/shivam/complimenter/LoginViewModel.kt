package com.shivam.complimenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel(){
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTH
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if(user != null)
            AuthenticationState.AUTHENTICATED
        else
            AuthenticationState.UNAUTHENTICATED
    }
}