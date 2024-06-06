package com.yugen.opsc7311_poe


import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle

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
import com.yugen.opsc7311_poe.helpers.openPopupMenu

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yugen.opsc7311_poe.helpers.DBHelper
import com.yugen.opsc7311_poe.helpers.UserHelper
import java.util.Calendar
import com.yugen.opsc7311_poe.objects.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.io.encoding.ExperimentalEncodingApi


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// Declare a variable to store the selected image URI
var selectedImageUri: Uri? = null
lateinit var taskName: String
lateinit var taskDesc: String
lateinit var category: String
lateinit var dateString: String
lateinit var startTimeString: String
lateinit var endTimeString: String
val DBHelper = DBHelper()
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

    @OptIn(ExperimentalEncodingApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timesheet_entry, container, false)
        val categories = UserHelper.categoryList
        // Convert the list of Category objects to a list of category names
        val categoryNames = categories.map { it }

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

        val btnStartTime = view.findViewById<TextView>(R.id.btn_start_time)
        val btnEndTime = view.findViewById<TextView>(R.id.btn_end_time)
        val btnSelectDate = view.findViewById<TextView>(R.id.btn_select_date)
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
            dateString = (view.findViewById<TextView>(R.id.btn_select_date).text.toString())
            startTimeString =(view.findViewById<TextView>(R.id.btn_start_time).text.toString())
            endTimeString = (view.findViewById<TextView>(R.id.btn_end_time).text.toString())
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val date: Date = format.parse(dateString) ?: Date()
            val startTime: LocalTime = LocalTime.parse(startTimeString, timeFormatter)
            val endTime: LocalTime = LocalTime.parse(endTimeString, timeFormatter)
            val duration = (endTime.hour - startTime.hour) * 60 + (endTime.minute - startTime.minute)

            val imageView = view.findViewById<ImageView>(R.id.attached_image)
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val base64Image: String = android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT)

            val tempTask = Task(taskName, taskDesc, category,date, startTime, endTime, base64Image, duration, false);
            UserHelper.TaskList.add(tempTask)

            CoroutineScope(Dispatchers.IO).launch {
                DBHelper.updatePersonTask(UserHelper.TaskList, UserHelper.loggedInUser)
            }

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

    private fun showTimePicker(button: TextView) {
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

    private fun showDatePicker(button: TextView) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                cal.set(selectedYear, selectedMonth, selectedDay)
                // Get the Date object
                val date: Date = cal.time
                // Format the date as a string and set it as the button text
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                button.text = format.format(date)
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