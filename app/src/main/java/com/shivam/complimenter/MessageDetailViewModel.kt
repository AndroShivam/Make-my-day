package com.shivam.complimenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageDetailViewModel : ViewModel() {

    lateinit var isClicked: MutableLiveData<Boolean>

    init {
        isClicked.value = false
    }

}