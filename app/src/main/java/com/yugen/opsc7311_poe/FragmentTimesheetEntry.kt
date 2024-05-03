package com.yugen.opsc7311_poe


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.drawable.BitmapDrawable
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.yugen.opsc7311_poe.helpers.CAMERA_REQUEST_CODE
import com.yugen.opsc7311_poe.helpers.GALLERY_REQUEST_CODE
import com.yugen.opsc7311_poe.helpers.openPopupMenu

import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.yugen.opsc7311_poe.helpers.SessionsListHelper
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.helpers.UserHelper.loggedInUser
import java.util.Calendar
import com.yugen.opsc7311_poe.objects.Session


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// Declare a variable to store the selected image URI
var selectedImageUri: Uri? = null
lateinit var taskName: String
lateinit var taskDesc: String
lateinit var category: String
lateinit var date: String
lateinit var startTime: String
lateinit var endTime: String
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
        val categories = UserHelper.loggedInUser?.categoryList ?: mutableListOf()

        // Convert the list of Category objects to a list of category names
        val categoryNames = categories.map { it.categoryName }

        try {
            val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryNames)
            view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1).setAdapter(arrayAdapter)
        } catch (e: Exception) {
            Log.e("ArrayAdapter", "Error setting up ArrayAdapter: ${e.message}")
        }

        // Find the button by its ID
        val addAttachmentButton = view.findViewById<Button>(R.id.button_add_attachment)

        // Set OnClickListener for the button
        addAttachmentButton.setOnClickListener {
            openPopupMenu(requireActivity())
        }

        val btnStartTime = view.findViewById<Button>(R.id.btn_start_time)
        val btnEndTime = view.findViewById<Button>(R.id.btn_end_time)
        val btnSelectDate = view.findViewById<Button>(R.id.btn_select_date)
        val btnAddTimeEntry = view.findViewById<Button>(R.id.button_add_time_entry)

        btnStartTime.setOnClickListener {
            showTimePicker(btnStartTime)
        }

        btnEndTime.setOnClickListener {
            showTimePicker(btnEndTime)
        }

        btnSelectDate.setOnClickListener {
            showDatePicker(btnSelectDate)
        }

        btnAddTimeEntry.setOnClickListener {

            taskName = (view.findViewById<EditText>(R.id.input_task_name).text.toString())
            taskDesc = (view.findViewById<EditText>(R.id.input_description).text.toString())
            category = (view.findViewById<EditText>(R.id.autoCompleteTextView1).text.toString())
            date = (view.findViewById<Button>(R.id.btn_select_date).text.toString())
            startTime =(view.findViewById<Button>(R.id.btn_start_time).text.toString())
            endTime = (view.findViewById<Button>(R.id.btn_end_time).text.toString())

            val imageView = view.findViewById<ImageView>(R.id.attached_image)
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap

            val tempSession = Session(taskName, taskDesc, category, date, startTime, endTime,
                bitmap)
            UserHelper.loggedInUser?.addSession(tempSession)

            val sessionFromList = UserHelper.loggedInUser?.sessionList?.get(0)

            UserHelper.loggedInUser!!.categoryList.forEach { category ->  category.updateHours(SessionsListHelper.calculateTotalHoursInCategory(UserHelper.loggedInUser!!.sessionList, category.categoryName) ) }
            Toast.makeText(requireContext(), "Entry Successfully Captured.", Toast.LENGTH_SHORT).show()

            val timesheetFragment = FragmentTimesheet.newInstance("a","a")
            fragmentManager?.beginTransaction()?.replace(R.id.frame_layout, timesheetFragment)?.commit()
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun showTimePicker(button: Button) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                button.text = String.format("%02d:%02d", selectedHour, selectedMinute)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()

    }

    private fun showDatePicker(button: Button) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                button.text = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
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