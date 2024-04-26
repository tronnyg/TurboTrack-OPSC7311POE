package com.yugen.opsc7311_poe

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.yugen.opsc7311_poe.databinding.MainPageBinding
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}


