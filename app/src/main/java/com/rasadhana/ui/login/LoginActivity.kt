package com.rasadhana.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rasadhana.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val loginViewModel =
            ViewModelProvider(this)[LoginViewModel::class.java]

//        sementara
        binding.btnLogin.setOnClickListener {
            loginViewModel.numberPhone.value = binding.tfNumberPhone.editText?.text.toString()
            loginViewModel.password.value = binding.tfPassword.editText?.text.toString()

            Toast.makeText(this@LoginActivity, "berhasil login dengan nomor ${loginViewModel.numberPhone.value}", Toast.LENGTH_SHORT).show()
        }

    }
}