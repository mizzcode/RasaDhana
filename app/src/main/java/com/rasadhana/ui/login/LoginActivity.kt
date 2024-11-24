package com.rasadhana.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rasadhana.MainActivity
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
            loginViewModel.numberPhone.value = binding.tfEmail.editText?.text.toString()
            loginViewModel.password.value = binding.tfPassword.editText?.text.toString()

            if (loginViewModel.numberPhone.value.isNullOrEmpty() || loginViewModel.password.value.isNullOrEmpty()) {
                Toast.makeText(this, "Tolong isi email atau password yang kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.tvRegisterNow.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}