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


class LoginPageActivity : AppCompatActivity() {
    private var users = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.inputEmail.setText("mark@gmail.com")
        binding.inputPassword.setText("P@ssw0rd")
        binding.textCreateAccount.setOnClickListener{
            openIntent(this,CreateAccountActivity::class.java)
        }
        val DBHelper = DBHelper()
        CoroutineScope(Dispatchers.IO).launch{
            users = DBHelper.getUsers().toMutableList()
        }

        binding.buttonSignin.setOnClickListener{

            val inputEmail =  binding.inputEmail.text.toString()
            val inputPassword = binding.inputPassword.text.toString()

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

            for(person in users)
            {
                if(inputEmail == person.email && inputPassword == person.password)
                {
                    UserHelper.loggedInUser = person
                    openIntent(this, MainActivity::class.java)
                    return@setOnClickListener
                }
            }
            Toast.makeText(this, "Invalid Email or Password, Please Try Again.", Toast.LENGTH_SHORT).show()

        }
    }
}