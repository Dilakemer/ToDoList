package com.example.todoapp

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.databinding.ActivitySplashBinding
import android.animation.AnimatorListenerAdapter

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lottieView.playAnimation()

        binding.lottieView.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                navigateToMain()
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing) {
                navigateToMain()
            }
        }, 5000)
    }

    private fun navigateToMain() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
