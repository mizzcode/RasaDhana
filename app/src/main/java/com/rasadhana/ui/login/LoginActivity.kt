package com.rasadhana.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rasadhana.ui.main.MainActivity
import com.rasadhana.data.Result
import com.rasadhana.data.pref.UserModel
import com.rasadhana.databinding.ActivityLoginBinding
import com.rasadhana.ui.forgotpassword.ForgotPasswordActivity
import com.rasadhana.ui.register.RegisterActivity
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val loginViewModel: LoginViewModel by inject()

        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

//        sementara
        binding.btnLogin.setOnClickListener {
            val email = binding.tfEmail.editText?.text?.trim().toString()
            val password = binding.tfPassword.editText?.text?.trim().toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Tolong isi email atau password yang kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginViewModel.login(email, password).observe(this) { result ->
                if (result != null) {
                    when(result) {
                        is Result.Error -> {
                            showLoading(false)
                            showToast(result.error)
                        }
                        Result.Loading -> showLoading(true)
                        is Result.Success -> {

                            if (result.data.success) {
                                val response = result.data
                                val token = response.data

                                loginViewModel.getUserData(token).observe(this) { user ->
                                    if (user != null) {
                                        when (user) {
                                            is Result.Error -> {
                                                showLoading(false)
                                                showToast(user.error)
                                            }
                                            Result.Loading -> {}
                                            is Result.Success -> {
                                                showLoading(false)

                                                val id = user.data.id
                                                val name = user.data.name

                                                loginViewModel.saveSession(UserModel(id, name, email, token, true)) // simpan session user

                                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                                finish()
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                showToast(result.data.message)
                            }
                        }
                    }
                }
            }
        }

        binding.tvRegisterNow.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}