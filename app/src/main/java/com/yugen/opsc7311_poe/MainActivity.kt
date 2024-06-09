package com.yugen.opsc7311_poe

import FragmentCalendar
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yugen.opsc7311_poe.databinding.MainPageBinding
import com.yugen.opsc7311_poe.helpers.CAMERA_REQUEST_CODE
import com.yugen.opsc7311_poe.helpers.GALLERY_REQUEST_CODE

class MainActivity : AppCompatActivity() {

    private lateinit var binding : MainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(FragmentHomePage())
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home -> replaceFragment(FragmentHomePage())
                R.id.calendar -> replaceFragment(FragmentCalendar())
                R.id.sessions -> replaceFragment(FragmentTimesheet())
                R.id.timer -> replaceFragment(FragmentTimer())
                R.id.reports -> replaceFragment(FragmentReports())
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URI from the gallery intent
            selectedImageUri = data.data
            // Load the image and convert it to a bitmap
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            val attachedImageView = findViewById<ImageView>(R.id.attached_image)
            attachedImageView?.setImageBitmap(bitmap)

        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Get the captured image from the camera intent
            val imageBitmap = data.extras?.get("data") as Bitmap
            val attachedImageView = findViewById<ImageView>(R.id.attached_image)
            attachedImageView?.setImageBitmap(imageBitmap)
        }
    }
}


