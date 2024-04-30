package com.yugen.opsc7311_poe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.Helpers.UserHelper
import com.yugen.opsc7311_poe.Helpers.openIntent
import com.yugen.opsc7311_poe.databinding.CreateAccountPageBinding
import com.yugen.opsc7311_poe.helpers.openIntent

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = CreateAccountPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textCreateAccount.setOnClickListener{
            openIntent(this,LoginPageActivity::class.java)
        }

        binding.buttonSignup.setOnClickListener {
            val inputEmail =  binding.inputEmail.text.toString()
            val inputPassword = binding.inputPassword.text.toString()
            val inputConfirmPassword = binding.inputConfirmpassword.text.toString()

            if (inputEmail.isEmpty() || inputPassword.isEmpty() || inputConfirmPassword.isEmpty()) {
                // Display a toast message indicating invalid email/password
                Toast.makeText(this, "Invalid Email or Password, Please Try Again.", Toast.LENGTH_SHORT).show()
            }
            else if( inputPassword == inputConfirmPassword)
            {
                // add new user to firebase - will be done in part 3
                openIntent(this, LoginPageActivity::class.java)
            }
            else
            {
                Toast.makeText(this, "Invalid Email or Password, Please Try Again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}