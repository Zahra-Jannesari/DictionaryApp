package com.zarisa.dictionaryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zarisa.dictionaryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.iconSplashScreen.let {
            it.alpha = 0f
            it.animate().setDuration(3000).alpha(1f).withEndAction {
                binding.fragmentContainerView.visibility = View.VISIBLE
                binding.iconSplashScreen.visibility = View.GONE
                overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }
        }
    }
}