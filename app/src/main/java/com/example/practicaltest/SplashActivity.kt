package com.example.practicaltest

import android.os.AsyncTask
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.practicaltest.Models.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var handler: DatabaseHelper
    private lateinit var listUsers: MutableList<Employee>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        handler = DatabaseHelper(this)
        checkData()

        val logoView: ImageView = findViewById(R.id.logo)
        logoView.alpha = 0f
        logoView.animate().setDuration(1500).alpha(1f).withEndAction{
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }

    private fun checkData(){
        listUsers = ArrayList()

        GlobalScope.launch(Dispatchers.IO) {
            val result= handler.getAllUser()
            launch (Dispatchers.Main){
                listUsers.clear()
                listUsers.addAll(result!!)
                if(listUsers.size > 0) {
                    val myEmployee: Employee? = listUsers.find { it.login == 1 }
                    val logoView: ImageView = findViewById(R.id.logo)
                    logoView.alpha = 0f
                    if(myEmployee != null){
                        logoView.animate().setDuration(1500).alpha(1f).withEndAction{
                            val intent = Intent(applicationContext,HomeActivity::class.java)
                            intent.putExtra("Email",myEmployee.email)
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                            finish()
                        }
                    }else{
                        logoView.animate().setDuration(1500).alpha(1f).withEndAction{
                            val i = Intent(applicationContext,MainActivity::class.java)
                            startActivity(i)
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                            finish()
                        }
                    }
                }
            }
        }
    }
}