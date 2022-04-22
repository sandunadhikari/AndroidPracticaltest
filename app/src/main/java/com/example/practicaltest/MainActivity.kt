package com.example.practicaltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import com.example.practicaltest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        showHome()

        binding.registrationLayout.login.setOnClickListener {
            showLoginIN()
        }

        binding.loginLayout.registration.setOnClickListener {
            showRegistration()
        }
    }

    private fun showRegistration(){
        binding.registrationLayout.root.visibility = View.VISIBLE
        binding.loginLayout.root.visibility = View.GONE
    }

    private fun showLoginIN(){
        binding.registrationLayout.root.visibility = View.GONE
        binding.loginLayout.root.visibility = View.VISIBLE
    }

    private fun showHome(){
        binding.registrationLayout.root.visibility = View.GONE
        binding.loginLayout.root.visibility = View.VISIBLE
    }
}