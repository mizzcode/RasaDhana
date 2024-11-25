package com.rasadhana.ui.otp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.rasadhana.R
import com.rasadhana.databinding.ActivityOtpBinding
import com.rasadhana.ui.forgotpassword.CreateNewPasswordActivity

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val email = intent.getStringExtra(EXTRA_EMAIL) ?: ""

        val message = if (email.isNotEmpty()) {
            getString(R.string.we_sent_otp_to_your_email, email)
        } else {
            getString(R.string.we_sent_otp_to_your_email, "email@gmail.com")
        }

        val type = intent.getStringExtra(EXTRA_TYPE)

        binding.tvSentOtp.text = message

        binding.btnSent.isEnabled = false

        binding.otpView.setOtpCompletionListener {
            val otp = it

            binding.btnSent.isEnabled = otp.length == 5

            binding.btnSent.setOnClickListener {
                Log.d("OTP_CODE", "OTP: $otp")

                // TODO logic pencocokan otp pada server
                if (otp.isNotEmpty()) {
                    when (type) {
                        FORGOT_PASSWORD -> {
                            val intent = Intent(this@OtpActivity, CreateNewPasswordActivity::class.java)
                            startActivity(intent)
                        }
                        else -> {
                            showToast("Kamu siapa???")
                            finish()
                        }
                    }
                } else {
                    showToast("PIN OTP INVALID")
                }
            }
        }

        binding.otpView.addTextChangedListener {
            val otp = it

            binding.btnSent.isEnabled = otp?.length == 5
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_TYPE = "extra_type"
        const val FORGOT_PASSWORD = "forgot_password"
    }
}