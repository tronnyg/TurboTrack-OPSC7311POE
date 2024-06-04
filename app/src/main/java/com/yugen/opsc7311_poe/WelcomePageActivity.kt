package com.yugen.opsc7311_poe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.helpers.openIntent
import com.yugen.opsc7311_poe.databinding.WelcomePageBinding
import com.yugen.opsc7311_poe.helpers.DBHelper
import com.yugen.opsc7311_poe.objects.Category
import com.yugen.opsc7311_poe.objects.Person


class WelcomePageActivity : AppCompatActivity() {
    private lateinit var DBHelper : DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = WelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DBHelper = DBHelper()
        binding.btnLogin.setOnClickListener{
            DBHelper.savePerson(Person("1","Yugen","Moodley","admin"))
            openIntent(this,LoginPageActivity::class.java)
        }
        binding.btnRegister.setOnClickListener{
            openIntent(this,CreateAccountActivity::class.java)
        }
    }
}