package com.bosandroidapp.aopaykit.ui.slideshow.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bosandroidapp.aopaykit.databinding.SplashMainBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage


class SplashScreen : AppCompatActivity() {
    lateinit var binding: SplashMainBinding
    lateinit var preference: SharedPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        super.onCreate(savedInstanceState)

        binding = SplashMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference = SharedPreference(this)


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }


        binding.uattext.visibility= View.VISIBLE


        Handler(Looper.getMainLooper()).postDelayed({
            // Code to run after delay
            if (preference.getBoolanValue(ConstantClass.LoggedIn, false)) {
                val mainIntent = Intent(this@SplashScreen, DashBoard::class.java)
                startActivity(mainIntent)
                finish()
            }
            else {
                val mainIntent = Intent(this@SplashScreen, ChooseYourRolePage::class.java)
                startActivity(mainIntent)
                finish()
            }


        }, 3000)


    }





}