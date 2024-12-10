package com.rasadhana.ui.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rasadhana.data.Result
import com.rasadhana.databinding.ActivityForgotPasswordBinding
import com.rasadhana.ui.otp.OtpActivity
import com.rasadhana.ui.otp.OtpActivity.Companion.EXTRA_EMAIL
import com.rasadhana.ui.otp.OtpActivity.Companion.EXTRA_OTP
import com.rasadhana.ui.otp.OtpActivity.Companion.EXTRA_TYPE
import org.koin.android.ext.android.inject

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnSent.setOnClickListener {
            val email = binding.tfEmail.editText?.text.toString().trim()

            if (email.isEmpty()) {
                binding.tfEmail.error = "Email tidak boleh kosong"
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tfEmail.error = "Email tidak valid"
                return@setOnClickListener
            }

            val forgotPasswordViewModel: ForgotPasswordViewModel by inject()

            forgotPasswordViewModel.getOtpForgotPassword(email).observe(this) { result ->
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

                                val otp = response.otp

                                val intent = Intent(this@ForgotPasswordActivity, OtpActivity::class.java)
                                intent.putExtra(EXTRA_EMAIL, email)
                                intent.putExtra(EXTRA_OTP, otp)
                                intent.putExtra(EXTRA_TYPE, OtpActivity.FORGOT_PASSWORD)
                                startActivity(intent)
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