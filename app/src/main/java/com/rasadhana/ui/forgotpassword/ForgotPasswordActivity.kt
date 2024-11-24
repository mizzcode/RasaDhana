package com.rasadhana.ui.forgotpassword

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rasadhana.databinding.ActivityForgotPasswordBinding
import com.rasadhana.ui.otp.OtpActivity
import com.rasadhana.ui.otp.OtpActivity.Companion.EXTRA_EMAIL
import com.rasadhana.ui.otp.OtpActivity.Companion.EXTRA_TYPE

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnSent.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, OtpActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, binding.tfEmail.editText?.text.toString())
            intent.putExtra(EXTRA_TYPE, "forgot_password")
            startActivity(intent)
        }
    }
}