package com.rasadhana.ui.setting

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rasadhana.R
import com.rasadhana.data.Result
import com.rasadhana.data.local.entity.UserEntity
import com.rasadhana.databinding.ActivitySettingBinding
import com.rasadhana.reduceFileImage
import com.rasadhana.uriToFile
import org.koin.android.ext.android.inject

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    private val settingViewModel: SettingViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 12 and above, use WindowInsetsController
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        } else {
            // For Android below Android 12, use the legacy method
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        }

        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@SettingActivity, R.color.blue)))
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.setting)
        }

        val user = intent.getParcelableExtra<UserEntity>(EXTRA_USER)

        if (user != null) {
            Log.d("userSetting", user.name)
            Log.d("userSetting", user.photoUrl)
            Log.d("userSetting", user.email)
            Log.d("userSetting", user.isLogin.toString())
        }

        if (user != null) {
            with(binding) {

                val uri = try {
                    Uri.parse(user.photoUrl)
                } catch (e: Exception) {
                    null
                }

                if (uri != null && ("content" == uri.scheme || "file" == uri.scheme)) {
                    Log.d("accountFragment", "MASUK KE URI ACCOUNT $uri")
                    Glide.with(this@SettingActivity)
                        .load(uri)
                        .error(R.drawable.baseline_account_box_24)
                        .into(profileImage)
                } else {
                    Log.d("accountFragment", "MASUK BUKAN URI")
                    Glide.with(this@SettingActivity)
                        .load(user.photoUrl)
                        .error(R.drawable.baseline_account_box_24)
                        .into(profileImage)
                }

                edtName.setText(user.name)
                edtEmail.setText(user.email)

                edtEmail.isEnabled = false

                fabEditProfile.setOnClickListener {
                    startGallery()
                }

                btnUpdateUser.setOnClickListener {
                    val name = edtName.text.toString()

                    if (name.isBlank()) {
                        showToast("Nama tidak boleh kosong!")
                        return@setOnClickListener
                    }

                    if (user.authType == "firebase") {
                        val firebaseUser = Firebase.auth.currentUser

                        if (firebaseUser != null) {
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(settingViewModel.currentImageUri)
                                .build()

                            firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("Firebase Update", "User profile updated.")

                                        val uri = settingViewModel.currentImageUri.toString()

                                        // Simpan data ke sesi
                                        settingViewModel.saveSession(
                                            UserEntity(
                                                id = user.id,
                                                name = name,
                                                email = user.email,
                                                token = user.token,
                                                isLogin = true,
                                                photoUrl = uri,
                                                expireToken = user.expireToken,
                                                authType = user.authType
                                            )
                                        )
                                        finish()
                                    } else {
                                        Log.e("Firebase Update", "Failed to update profile: ${task.exception}")
                                        showToast("profile update failed")
                                    }
                                }
                        } else {
                            showToast("no user sign in")
                        }
                    } else {
                        // Logika untuk backend Node.js
                        val imageFile = settingViewModel.currentImageUri?.let { uri ->
                            uriToFile(uri, this@SettingActivity).reduceFileImage()
                        }

                        settingViewModel.updateUserSetting(imageFile, name, user.id).observe(this@SettingActivity) { result ->
                            when (result) {
                                is Result.Error -> {
                                    showToast(result.error)
                                    showLoading(false)
                                }
                                Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    showLoading(false)

                                    val data = result.data

                                    settingViewModel.saveSession(
                                        UserEntity(
                                            id = user.id,
                                            name = data.user.name,
                                            email = user.email,
                                            token = user.token,
                                            isLogin = true,
                                            photoUrl = data.user.photoUrl,
                                            expireToken = user.expireToken,
                                            authType = user.authType
                                        )
                                    )

                                    showToast(data.message)
                                    finish()
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            settingViewModel.currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        settingViewModel.currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.profileImage.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}