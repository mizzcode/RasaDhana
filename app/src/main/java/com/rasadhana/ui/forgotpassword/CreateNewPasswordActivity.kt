package com.rasadhana.ui.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rasadhana.data.Result
import com.rasadhana.databinding.ActivityCreateNewPasswordBinding
import com.rasadhana.ui.login.LoginActivity
import com.rasadhana.ui.otp.OtpActivity.Companion.EXTRA_OTP
import org.koin.android.ext.android.inject

class CreateNewPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val otp = intent.getStringExtra(EXTRA_OTP)

        if (otp != null) {
            binding.btnResetPassword.setOnClickListener {
                val newPassword = binding.tfNewPassword.editText?.text.toString().trim()
                val confirmPassword = binding.tfConfirmNewPassword.editText?.text.toString().trim()

                if (newPassword.isEmpty()) {
                    binding.tfNewPassword.error = "Password tidak boleh kosong"
                    return@setOnClickListener
                }

                if (confirmPassword.isEmpty()) {
                    binding.tfConfirmNewPassword.error = "Konfirmasi password tidak boleh kosong"
                    return@setOnClickListener
                }

                if (newPassword != confirmPassword) {
                    binding.tfConfirmNewPassword.error = "Password dan konfirmasi password harus sama"
                    return@setOnClickListener
                }

                val createNewPasswordViewModel: CreateNewPasswordViewModel by inject()

                createNewPasswordViewModel.updatePassword(otp, newPassword).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Error -> {
                                showLoading(false)
                                showToast(result.error)
                            }
                            Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)

                                if (result.data.success) {
                                    val response = result.data

                                    showToast(response.message)

                                    val intent = Intent(this@CreateNewPasswordActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}