package com.yugen.opsc7311_poe.helpers

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import com.yugen.opsc7311_poe.FragmentTimesheetEntry
import com.yugen.opsc7311_poe.R

fun openPopupMenu(activity: FragmentActivity) {
    // Creating a PopupMenu instance
    val popupMenu = PopupMenu(activity, activity.findViewById(R.id.button_add_attachment))

    // Inflating the menu resource
    popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

    // Setting menu item click listener
    popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
        when (item?.itemId) {
            R.id.menu_gallery -> {
                // Open gallery to select image
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activity.startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                true
            }
            R.id.menu_camera -> {
                // Open camera to capture image
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activity.startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                true
            }
            else -> false
        }
    }

    // Showing the popup menu
    popupMenu.show()
}

// Constants for request codes
const val GALLERY_REQUEST_CODE = 100
const val CAMERA_REQUEST_CODE = 101