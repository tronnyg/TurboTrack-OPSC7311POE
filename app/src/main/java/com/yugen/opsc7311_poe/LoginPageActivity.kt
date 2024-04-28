package com.yugen.opsc7311_poe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.Helpers.openIntent
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
            val inputEmail =  binding.inputEmail.text.toString()
            val inputPassword = binding.inputPassword.text.toString()

            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                // Display a toast message indicating invalid email/password
                Toast.makeText(this, "Invalid Email or Password, Please Try Again.", Toast.LENGTH_SHORT).show()
            }
            else if(inputEmail == "admin@gmail.com" && inputPassword == "admin")
            {
                openIntent(this, MainActivity::class.java)
            }
            else
            {
                Toast.makeText(this, "Invalid Email or Password, Please Try Again.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}