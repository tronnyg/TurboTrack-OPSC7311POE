package com.yugen.opsc7311_poe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yugen.opsc7311_poe.databinding.MainPageBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding : MainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home -> replaceFragment(HomePageFragment())
                R.id.sessions -> replaceFragment(FragmentTimesheet())
                R.id.timer  -> replaceFragment(FragmentTimesheetEntry())
                R.id.timer -> replaceFragment(FragmentTimesheetEntry())

                else ->{}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}


