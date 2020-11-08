package com.shivam.makemyday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.shivam.makemyday.databinding.ActivityBaseBinding

class BaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base)
    }
}