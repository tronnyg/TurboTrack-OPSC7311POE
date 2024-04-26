package com.yugen.opsc7311_poe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.databinding.LoginPageBinding
class LoginPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textCreateAccount.setOnClickListener{
            openIntent(this,CreateAccountActivity::class.java)
        }
        binding.buttonSignin.setOnClickListener{
            openIntent(this, MainActivity::class.java)
        }
    }
}