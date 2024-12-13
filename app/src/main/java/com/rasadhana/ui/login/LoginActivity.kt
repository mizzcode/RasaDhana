package com.rasadhana.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rasadhana.BuildConfig
import com.rasadhana.ui.main.MainActivity
import com.rasadhana.data.Result
import com.rasadhana.data.local.entity.UserEntity
import com.rasadhana.databinding.ActivityLoginBinding
import com.rasadhana.ui.forgotpassword.ForgotPasswordActivity
import com.rasadhana.ui.register.RegisterActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private val loginViewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.btnLoginGoogle.setOnClickListener {
            signInWithGoogle()
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tfEmail.editText?.text?.trim().toString()
            val password = binding.tfPassword.editText?.text?.trim().toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Tolong isi email atau password yang kosong")
                return@setOnClickListener
            }

            loginViewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                    Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        if (result.data.success) {
                            val token = result.data.token

                            loginViewModel.getUserData(token).observe(this) { userResult ->
                                when (userResult) {
                                    is Result.Error -> {
                                        showLoading(false)
                                        showToast(userResult.error)
                                    }
                                    Result.Loading -> {}
                                    is Result.Success -> {
                                        Log.d("LoginActivity", "User data: ${userResult.data}")
                                        showLoading(false)
                                        saveSessionUserAndNavigate(userResult.data, token)
                                    }
                                }
                            }
                        } else {
                            showToast(result.data.message)
                        }
                    }
                }
            }
        }

        binding.tvRegisterNow.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun signInWithGoogle() {
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(this@LoginActivity, request)
                handleSignInResult(result)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Google Sign-In failed: ${e.message}", e)
            }
        }
    }

    private fun handleSignInResult(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Invalid Google ID Token", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected credential type: ${credential.type}")
                }
            }
            else -> Log.e(TAG, "Unrecognized credential type")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Sign-in with Google succeeded")

                val user = auth.currentUser

                val expireToken = Date(System.currentTimeMillis() + 86400000L)  // 86400000 ms = 1 hari

                // Format tanggal dalam ISO 8601
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")

                val expireTokenFormatted = dateFormat.format(expireToken)

                user?.apply {
                    // Menyimpan data user ke session
                    val userEntity = UserEntity(
                        id = this.uid,
                        name = this.displayName ?: "",
                        email = this.email ?: "",
                        photoUrl = this.photoUrl?.toString() ?: "",
                        isLogin = true,
                        token = idToken,
                        expireToken = expireTokenFormatted,
                        authType = "firebase",
                    )
                    loginViewModel.saveSession(userEntity)
                }

                updateUI(user)
            } else {
                Log.e(TAG, "Sign-in with Google failed", task.exception)
                updateUI(null)
            }
        }
    }


    private fun saveSessionUserAndNavigate(user: UserEntity, token: String) {
        loginViewModel.saveSession(
            UserEntity(
                id = user.id,
                name = user.name,
                email = user.email,
                token = token,
                photoUrl = user.photoUrl,
                isLogin = true,
                expireToken = user.expireToken,
                authType = user.authType.ifEmpty { "nodejs" }
            )
        )
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
