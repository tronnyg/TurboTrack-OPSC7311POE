package com.yugen.opsc7311_poe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.yugen.opsc7311_poe.helpers.CAMERA_REQUEST_CODE
import com.yugen.opsc7311_poe.helpers.GALLERY_REQUEST_CODE
import com.yugen.opsc7311_poe.helpers.openPopupMenu

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
// Declare a variable to store the selected image URI
var selectedImageUri: Uri? = null
/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTimesheetEntry.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTimesheetEntry : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timesheet_entry, container, false)

        // Find the button by its ID
        val addAttachmentButton = view.findViewById<Button>(R.id.button_add_attachment)

        // Set OnClickListener for the button
        addAttachmentButton.setOnClickListener {
            openPopupMenu(requireActivity())
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentTimesheetEntry.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTimesheetEntry().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}