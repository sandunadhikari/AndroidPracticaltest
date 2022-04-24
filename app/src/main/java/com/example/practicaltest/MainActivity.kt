package com.example.practicaltest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.practicaltest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var handler: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        handler = DatabaseHelper(this)

        showHome()

        binding.registrationLayout.login.setOnClickListener {
            showLoginIN()
        }

        binding.loginLayout.registration.setOnClickListener {
            showRegistration()
        }

        binding.registrationLayout.saveButton.setOnClickListener {
            insertDataToDatabase()
        }

        binding.loginLayout.loginButton.setOnClickListener {
            checkData()
        }
    }

    private fun checkData(){
        var userEmail = binding.loginLayout.liginEmail.text.toString().trim()
        var userPassword = binding.loginLayout.loginPassword.text.toString().trim()
        if (inputCheckLogin(userEmail,userPassword)){
            if(handler.userPresent(userEmail,userPassword)){
                Toast.makeText(this, "Successfully logged!", Toast.LENGTH_LONG).show()
                var Intent = Intent(this,HomeActivity::class.java)
                startActivity(Intent)
            }else{
                Toast.makeText(this,"Email or Password is incorrect",Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(this,"Please fill out all fields",Toast.LENGTH_LONG).show()
        }
    }

    private fun insertDataToDatabase(){
        var userName = binding.registrationLayout.nameText.text.toString().trim()
        var userEmail = binding.registrationLayout.emailText.text.toString().trim()
        var userPassword = binding.registrationLayout.passwordText.text.toString().trim()

        if(inputCheckRegister(userName,userEmail,userPassword)){
            if(!handler.userExsit(userEmail)) {
                handler.insertUserData(userName, userEmail, userPassword)
                Toast.makeText(this, "Successfully added!", Toast.LENGTH_LONG).show()
                binding.registrationLayout.nameText.text.clear()
                binding.registrationLayout.emailText.text.clear()
                binding.registrationLayout.passwordText.text.clear()
                showLoginIN()
            }else{
                Toast.makeText(this,"Email already exist",Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this,"Please fill out all fields",Toast.LENGTH_LONG).show()
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

    private fun inputCheckRegister(username: String,useremail: String,userpassword: String):Boolean{
        return !(TextUtils.isEmpty(username) && TextUtils.isEmpty(useremail) && TextUtils.isEmpty(userpassword))
    }

    private fun inputCheckLogin(useremail: String,userpassword: String):Boolean{
        return !(TextUtils.isEmpty(useremail) && TextUtils.isEmpty(userpassword))
    }
}