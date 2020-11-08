package com.shivam.makemyday

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.shivam.makemyday.databinding.FragmentSettingsBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private var profileImageURI: Uri = Uri.EMPTY

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUserID: String

    companion object {
        private var STORAGE_PERMISSION_CODE = 123
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()
        currentUserID = firebaseAuth.currentUser?.uid.toString()


        firebaseFirestore.collection("Users").document(currentUserID).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result?.exists()!!) {

                        val userName = task.result!!.getString("user_name")
                        val userProfilePicture = task.result!!.getString("image_url")

                        binding.settingsUsername.setText(userName)

                        val placeHolderRequest = RequestOptions()
                        placeHolderRequest.placeholder(R.drawable.profile_picture)
                        Glide.with(this).setDefaultRequestOptions(placeHolderRequest)
                            .load(userProfilePicture).diskCacheStrategy(
                                DiskCacheStrategy.ALL
                            )
                            .into(binding.settingsProfilePicture)
                    }
                }
            }

        binding.settingsProfilePicture.setOnClickListener {
            checkPermission()
        }

        binding.settingsSaveBtn.setOnClickListener {
            val profileName = binding.settingsUsername.text.toString()
            saveToFirestore(profileName)
        }

        binding.settingsLogout.setOnClickListener {
            firebaseAuth.signOut()
            requireActivity().finish()
        }


        return binding.root
    }

    private fun saveToFirestore(profileName: String) {
        if (!TextUtils.isEmpty(profileName) && Uri.EMPTY != profileImageURI) {
            saveUserName(profileName)
            saveProfilePicture()
            Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
        } else if (!TextUtils.isEmpty(profileName) && Uri.EMPTY == profileImageURI)
            saveUserName(profileName)
        else if (TextUtils.isEmpty(profileName) && Uri.EMPTY != profileImageURI)
            Toast.makeText(requireContext(), "Please enter your name", Toast.LENGTH_SHORT).show()
    }

    private fun saveProfilePicture() {
        val imagePath = storageReference.child("Profile_Images").child("$currentUserID.jpg")

        imagePath.putFile(profileImageURI).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imagePath.downloadUrl.addOnSuccessListener { uri ->
                    val field =
                        hashMapOf("image_url" to uri.toString())
                    firebaseFirestore.collection(NewMessageFragment.USERS)
                        .document(currentUserID)
                        .set(field, SetOptions.merge())
                }
            } else {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun saveUserName(profileName: String) {
        val field =
            hashMapOf("user_name" to profileName)
        firebaseFirestore.collection(NewMessageFragment.USERS)
            .document(currentUserID)
            .set(field, SetOptions.merge())
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun openGallery() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .start(requireContext(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                profileImageURI = result.uri
                binding.settingsProfilePicture.setImageURI(profileImageURI)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(requireContext(), "Error : ${result.error}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openGallery()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}