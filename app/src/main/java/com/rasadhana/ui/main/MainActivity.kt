package com.rasadhana.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rasadhana.R
import com.rasadhana.databinding.ActivityMainBinding
import com.rasadhana.ui.login.LoginActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_photo,
                R.id.navigation_favorite,
                R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin || isSessionExpired(user.expireToken)) {

                if (user.authType == "firebase") {
                    // logout firebase signIn with google
                    Firebase.auth.signOut()
                    Log.d("Logout", "Firebase sign-out successful")

                    // Hapus kredensial aktif firebase
                    clearCredentialState()
                }

                navigateToLogin()

                Toast.makeText(this, "Sesi habis, silahkan login ulang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearCredentialState() {
        val credentialManager = CredentialManager.create(this)

        // Hapus sesi kredensial
        lifecycleScope.launch {
            try {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                Log.d("Logout", "Credential state cleared successfully")
            } catch (e: Exception) {
                Log.e("Logout", "Failed to clear credential state: ${e.message}")
            }
        }
    }

    private fun isSessionExpired(expireToken: String): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val expireDate = format.parse(expireToken)
        val currentDate = Date()
        return expireDate?.before(currentDate) ?: true
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun setBottomNavVisibility(visible: Boolean) {
        binding.navCard.visibility = if (visible) View.VISIBLE else View.GONE
    }
}