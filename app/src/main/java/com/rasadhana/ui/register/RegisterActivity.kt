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
import com.rasadhana.ui.login.LoginActivity
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

                                showToast(response.message)

                                val intent = Intent(this, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
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