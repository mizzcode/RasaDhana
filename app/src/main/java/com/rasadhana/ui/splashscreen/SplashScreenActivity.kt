package com.rasadhana.ui.splashscreen

import  android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rasadhana.MainActivity
import com.rasadhana.R
import com.rasadhana.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

//        TODO
//        logic user udah login apa belum
        if (true) {
            moveTo(LoginActivity::class.java)
        } else {
            moveTo(MainActivity::class.java)
        }
    }

    private fun moveTo(cls: Class<*>) {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, cls)
            startActivity(intent)
            finish()
        }, 2000)
    }
}