package com.yugen.opsc7311_poe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.helpers.openIntent
import com.yugen.opsc7311_poe.databinding.LoginPageBinding
import com.yugen.opsc7311_poe.helpers.DBHelper
import com.yugen.opsc7311_poe.objects.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LoginPageActivity : AppCompatActivity() {
    private var users = mutableListOf<User>()
    private val DBHelper = DBHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Create Account Text*/
        binding.textCreateAccount.setOnClickListener{
            openIntent(this,CreateAccountActivity::class.java)
        }

        /*Fetches Users From the Database*/
        runBlocking(Dispatchers.IO){
            users = DBHelper.getUsers().toMutableList()
        }

        /*Sign In Button*/
        binding.buttonSignin.setOnClickListener{
            val inputEmail =  binding.inputEmail.text.toString()
            val inputPassword = binding.inputPassword.text.toString()

            if (inputEmail.isEmpty()){ binding.inputEmail.setBackgroundResource(R.drawable.error_input_box_vector);}
            if (inputPassword.isEmpty()){ binding.inputPassword.setBackgroundResource(R.drawable.error_input_box_vector); }

            /*Checks for correct login details*/
            for(person in users)
            {
                if(inputEmail == person.email && inputPassword == person.password)
                {
                    UserHelper.loggedInUser = person
                    openIntent(this, MainActivity::class.java)
                    users.clear()
                    return@setOnClickListener
                }
            }
            Toast.makeText(this, "Invalid Email or Password, Please Try Again.", Toast.LENGTH_SHORT).show()

        }
    }
}