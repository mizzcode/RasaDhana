package com.rasadhana.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rasadhana.data.Result
import com.rasadhana.databinding.ActivityRegisterBinding
import com.rasadhana.ui.otp.OtpActivity
import org.koin.android.ext.android.inject

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val registerViewModel: RegisterViewModel by inject()

        binding.btnRegister.setOnClickListener {

            val fullName = binding.tfName.editText?.text?.trim().toString()
            val email = binding.tfEmail.editText?.text?.trim().toString()
            val password = binding.tfPassword.editText?.text?.trim().toString()
            val confirmPassword = binding.tfConfirmPassword.editText?.text?.trim().toString()
            
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Semua field harus diisi")
                return@setOnClickListener
            } else if (password != confirmPassword) {
                showToast("Password dan konfirmasi password harus sama")
                return@setOnClickListener
            }

            registerViewModel.register(fullName, email, password).observe(this) { result ->
                if (result != null) {
                    Log.d("RegisterActivity", "Result: $result")
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)

                            if (result.data.success) {
                                val response = result.data
                                val otp = response.otp

                                Log.d("RegisterActivity", "OTP: $otp")
                                Log.d("RegisterActivity", "DATA: $response")

                                showToast(response.message)

                                val intent = Intent(this, OtpActivity::class.java)
                                intent.putExtra(OtpActivity.EXTRA_EMAIL, email)
                                intent.putExtra(OtpActivity.EXTRA_OTP, otp)
                                intent.putExtra(OtpActivity.EXTRA_PASSWORD, password)
                                intent.putExtra(OtpActivity.EXTRA_TYPE, OtpActivity.REGISTER)
                                startActivity(intent)
                            } else {
                                showToast(result.data.message)
                            }
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showToast(result.error)
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