package com.yugen.opsc7311_poe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.helpers.openIntent
import com.yugen.opsc7311_poe.databinding.CreateAccountPageBinding
import com.yugen.opsc7311_poe.helpers.ValidationUtils

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

            if (inputEmail.isEmpty()){
                binding.inputEmail.setBackgroundResource(R.drawable.error_input_box_vector);
            }
            else
            {
                binding.inputEmail.setBackgroundResource(R.drawable.input_box_vector)
            }

            if (inputPassword.isEmpty()){
                binding.inputPassword.setBackgroundResource(R.drawable.error_input_box_vector);
            }
            else
            {
                binding.inputPassword.setBackgroundResource(R.drawable.input_box_vector)
            }
            if (inputConfirmPassword.isEmpty())
            {
                binding.inputConfirmpassword.setBackgroundResource(R.drawable.error_input_box_vector);
            }
            else
            {
                binding.inputConfirmpassword.setBackgroundResource(R.drawable.input_box_vector);
            }


            if(ValidationUtils.isValidEmail(inputEmail) && ValidationUtils.isValidPassword(inputPassword) && inputPassword == inputConfirmPassword)
            {
                // add new user to firebase - will be done in part 3
                openIntent(this, LoginPageActivity::class.java)
            }
            else if (!ValidationUtils.isValidPassword(inputPassword))
            {
                binding.inputPassword.setBackgroundResource(R.drawable.error_input_box_vector);
                binding.inputConfirmpassword.setBackgroundResource(R.drawable.error_input_box_vector);
                Toast.makeText(this, "Password Error: 8 characters+, uppercase letter, number and special character", Toast.LENGTH_LONG).show()
            }
            else if (!ValidationUtils.isValidEmail(inputEmail))
            {
                binding.inputEmail.setBackgroundResource(R.drawable.error_input_box_vector);
                Toast.makeText(this, "Invalid Email Format, Please Try Again.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                binding.inputPassword.setBackgroundResource(R.drawable.error_input_box_vector);
                binding.inputConfirmpassword.setBackgroundResource(R.drawable.error_input_box_vector);
                Toast.makeText(this, "Passwords Don't Match, Please Try Again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}