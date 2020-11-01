package com.shivam.makemyday

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageDetailViewModel : ViewModel() {

    lateinit var isClicked: MutableLiveData<Boolean>

    init {
        isClicked.value = false
    }

}