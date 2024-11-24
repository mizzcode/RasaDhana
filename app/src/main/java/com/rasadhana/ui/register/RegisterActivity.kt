package com.rasadhana.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rasadhana.databinding.ActivityRegisterBinding
import com.rasadhana.ui.otp.OtpActivity
import com.rasadhana.ui.otp.OtpActivity.Companion.EXTRA_EMAIL
import com.rasadhana.ui.otp.OtpActivity.Companion.EXTRA_TYPE

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, binding.tfEmail.editText?.text.toString())
            intent.putExtra(EXTRA_TYPE, "register")
            startActivity(intent)
        }
    }
}