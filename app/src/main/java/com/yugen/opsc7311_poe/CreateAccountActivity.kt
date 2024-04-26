package com.yugen.opsc7311_poe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.databinding.CreateAccountPageBinding

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = CreateAccountPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textCreateAccount.setOnClickListener{
            openIntent(this,LoginPageActivity::class.java)}
    }
}