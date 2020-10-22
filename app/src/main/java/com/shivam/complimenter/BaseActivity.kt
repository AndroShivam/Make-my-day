package com.shivam.complimenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.shivam.complimenter.databinding.ActivityBaseBinding

class BaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBaseBinding
    private var currentNavController : LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_base)
    }


}