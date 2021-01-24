package com.fahimshahrierrasel.syncacross.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.fahimshahrierrasel.syncacross.R
import com.fahimshahrierrasel.syncacross.config.FirebaseConfig


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val loginActivityIntent = Intent(this, LoginActivity::class.java)

        if (FirebaseConfig.auth.currentUser == null) {
            startActivity(loginActivityIntent)
        } else {
            startActivity(mainActivityIntent)
        }
        finish()
    }
}