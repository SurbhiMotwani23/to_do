package com.surbhi.mytodo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.logging.Handler

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        val i = Intent(this@SplashActivity, MainActivity::class.java)
         Handler().postDelayed(Runnable {
             startActivity(i)
             finish()
         }
        }, 2000)
    }
}