package com.yugen.opsc7311_poe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.helpers.openIntent
import com.yugen.opsc7311_poe.databinding.LoginPageBinding
import com.yugen.opsc7311_poe.objects.Category
import com.yugen.opsc7311_poe.objects.User



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

            UserHelper.loggedInUser = User("user1","admin","USR001","admin@gmail.com")

            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                // Display a toast message indicating invalid email/password
                Toast.makeText(this, "Invalid Email or Password, Please Try Again.", Toast.LENGTH_SHORT).show()
            }
            else if(inputEmail == UserHelper.loggedInUser!!.email && inputPassword == UserHelper.loggedInUser!!.password)
            {
                UserHelper.loggedInUser!!.categoryList.add(Category("Personal",0))
                UserHelper.loggedInUser!!.categoryList.add(Category("Individual",0))
                UserHelper.loggedInUser!!.categoryList.add(Category("Group Work",0))
                UserHelper.loggedInUser!!.categoryList.add(Category("Other",0))
                openIntent(this, MainActivity::class.java)
            }
            else
            {
                Toast.makeText(this, "Invalid Email or Password, Please Try Again.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}