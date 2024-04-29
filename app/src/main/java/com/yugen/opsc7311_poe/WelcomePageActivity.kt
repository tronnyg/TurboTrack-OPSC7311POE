package com.yugen.opsc7311_poe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.databinding.WelcomePageBinding
import com.yugen.opsc7311_poe.helpers.openIntent

class WelcomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = WelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener{
            openIntent(this,LoginPageActivity::class.java)
        }
        binding.btnRegister.setOnClickListener{
            openIntent(this,CreateAccountActivity::class.java)
        }
    }
}