package com.yugen.opsc7311_poe

import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.helpers.openIntent
import com.yugen.opsc7311_poe.databinding.CreateAccountPageBinding
import com.yugen.opsc7311_poe.helpers.DBHelper
import com.yugen.opsc7311_poe.helpers.ValidationUtils
import com.yugen.opsc7311_poe.objects.Medals
import com.yugen.opsc7311_poe.objects.User

class CreateAccountActivity : AppCompatActivity() {
    lateinit var binding: CreateAccountPageBinding
    val DBHelper = DBHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAccountPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Button Create Account*/
        binding.textCreateAccount.setOnClickListener{
            openIntent(this,LoginPageActivity::class.java)
        }

        /*Button Sign Up*/
        binding.buttonSignup.setOnClickListener {
            /*Get Inputs from EditText*/
            val inputEmail =  binding.inputEmail.text.toString()
            val inputPassword = binding.inputPassword.text.toString()
            val inputConfirmPassword = binding.inputConfirmpassword.text.toString()
            val inputFirstName = binding.inputFirstName.text.toString()
            val inputLastName = binding.inputLastName.text.toString()

            /*Validate Empty Inputs & Sets Background*/
            validateAndSetBackground(inputEmail, binding.inputEmail);
            validateAndSetBackground(inputPassword, binding.inputPassword);
            validateAndSetBackground(inputConfirmPassword, binding.inputConfirmpassword);
            validateAndSetBackground(inputConfirmPassword, binding.inputFirstName);
            validateAndSetBackground(inputConfirmPassword, binding.inputLastName);

            if(ValidationUtils.isValidEmail(inputEmail) && ValidationUtils.isValidPassword(inputPassword) && inputPassword == inputConfirmPassword)
            {
                /*Adds New User to Database*/
                val newUser = User("",inputFirstName,inputLastName,inputPassword,inputEmail,"","")
                val defaultMedals = Medals(0,0,0,0,0)
                DBHelper.createNewUser(newUser,defaultMedals)
                /*Opens Login Page*/
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

    private fun validateAndSetBackground(input: String, view: View){
        if (input.isEmpty()){
            view.setBackgroundResource(R.drawable.error_input_box_vector);
        }
    }
}