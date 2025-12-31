package com.refactoring_android.math_skill.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.refactoring_android.math_skill.MainActivity
import com.refactoring_android.math_skill.R
import com.refactoring_android.math_skill.extension.TokenDataStore
import com.refactoring_android.math_skill.login.LoginActivity
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {//토큰이 있다면 바로 메인 화면으로 이동
                val token = TokenDataStore.getToken(this@SplashActivity)
                if (token != null) {
                    startMainActivity()

                }
                else{
                    startLoginActivity()
                }
            }
            finish()
        }, 2000) // 2초 후 메인으로 이동


        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splashScreen)) { v, insets ->
            v.setPadding(0,0,0,0)
            insets
        }

    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}