package com.rasadhana.ui.otp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.rasadhana.R
import com.rasadhana.data.Result
import com.rasadhana.databinding.ActivityOtpBinding
import com.rasadhana.ui.forgotpassword.CreateNewPasswordActivity
import com.rasadhana.ui.login.LoginActivity
import org.koin.android.ext.android.inject

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val otpViewModel: OtpViewModel by inject()

        val email = intent.getStringExtra(EXTRA_EMAIL)
        val password = intent.getStringExtra(EXTRA_PASSWORD)

        val message = getString(R.string.we_sent_otp_to_your_email, email)

        val type = intent.getStringExtra(EXTRA_TYPE)
        val otpFromServer = intent.getStringExtra(EXTRA_OTP)

        binding.tvSentOtp.text = message
        binding.btnSent.isEnabled = false

        binding.otpView.setOtpCompletionListener {
            val currentOtp = it

            binding.btnSent.isEnabled = currentOtp.length == 5

            binding.btnSent.setOnClickListener {
                Log.d("OTP_CODE", "OTP: $currentOtp")
                Log.d("OTP_CODE_FROM_SERVER", "OTP: $otpFromServer")

                if (currentOtp == otpFromServer) {
                    when (type) {
                        FORGOT_PASSWORD -> {
                            val intent = Intent(this@OtpActivity, CreateNewPasswordActivity::class.java)
                            intent.putExtra(EXTRA_OTP, currentOtp)
                            startActivity(intent)
                        }
                        REGISTER -> {
                            Log.d("OTP REGISTER", "OTP: $currentOtp, $email, $password")
                            otpViewModel.userVerify(email.toString(), currentOtp).observe(this) { result ->
                                if (result != null) {
                                    when (result) {
                                        is Result.Error -> {
                                            showToast(result.error)
                                        }
                                        Result.Loading -> {}
                                        is Result.Success -> {
                                            if (result.data.success) {
                                                Log.d("OTP REGISTER SUKSES", "${result.data}")
                                                showToast(result.data.message)
                                                val intent = Intent(this@OtpActivity, LoginActivity::class.java)
                                                startActivity(intent)
                                            } else {
                                                showToast(result.data.message)
                                                Log.d("OTP REGISTER FAILED", "${result.data}")
                                            }
                                        }
                                    }
                                }
                            }
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
            val currentOtp = it

            binding.btnSent.isEnabled = currentOtp?.length == 5
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_OTP = "extra_otp"
        const val EXTRA_PASSWORD = "extra_password"
        const val FORGOT_PASSWORD = "forgot_password"
        const val REGISTER = "register"
    }
}