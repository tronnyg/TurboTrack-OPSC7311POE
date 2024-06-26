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
import com.google.type.DateTime
import com.yugen.opsc7311_poe.helpers.DBHelper
import com.yugen.opsc7311_poe.helpers.UserHelper
import java.util.Calendar
import com.yugen.opsc7311_poe.objects.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.Duration
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
var startTime: String = ""
var endTime: String = ""
var duration = 0
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
        val taskStatus  = listOf("In Progress", "Completed")
        // Convert the list of Category objects to a list of category names
        val categoryNames = categories.map { it }

        val arrayAdapterCategory = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryNames)
        val arrayAdapterStatus = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, taskStatus)
        view.findViewById<AutoCompleteTextView>(R.id.selectCategoryDropdown).setAdapter(arrayAdapterCategory)
        view.findViewById<TextView>(R.id.btn_start_time).visibility = View.GONE
        view.findViewById<TextView>(R.id.btn_end_time).visibility = View.GONE
        view.findViewById<TextView>(R.id.lbl_time).visibility = View.GONE
        val statusDropdown = view.findViewById<AutoCompleteTextView>(R.id.selectStatusDropdown)
        statusDropdown.setAdapter(arrayAdapterStatus)

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

        statusDropdown.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            if (selectedItem == "Completed") {
                view.findViewById<TextView>(R.id.btn_start_time).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.btn_end_time).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.lbl_time).visibility = View.VISIBLE
            } else {
                view.findViewById<TextView>(R.id.btn_start_time).visibility = View.GONE
                view.findViewById<TextView>(R.id.btn_end_time).visibility = View.GONE
                view.findViewById<TextView>(R.id.lbl_time).visibility = View.GONE
            }
        }

        btnAddTimeEntry.setOnClickListener {

            taskName = (view.findViewById<EditText>(R.id.input_task_name).text.toString())
            taskDesc = (view.findViewById<EditText>(R.id.input_description).text.toString())
            category = (view.findViewById<EditText>(R.id.selectCategoryDropdown).text.toString())
            dateString = (view.findViewById<TextView>(R.id.btn_select_date).text.toString())

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            try {
                val date: Date = format.parse(dateString) ?: Date()

                val imageView = view.findViewById<ImageView>(R.id.attached_image)
                val drawable = imageView.drawable
                val bitmap: Bitmap?
                var base64Image: String = "none"
                if (drawable != null) {
                    bitmap = (imageView.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                     base64Image = android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT)
                }

                var completed = false
                if (statusDropdown.text.toString() == "Completed"){
                    completed = true
                    startTime =(view.findViewById<TextView>(R.id.btn_start_time).text.toString())
                    endTime = (view.findViewById<TextView>(R.id.btn_end_time).text.toString())
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                    duration = Duration.between(LocalTime.parse(startTime, timeFormatter), LocalTime.parse(endTime, timeFormatter)).toMinutes().toInt()
                }

                val tempTask = Task(taskName, taskDesc, category,date, startTime, endTime, base64Image, duration, completed);
                UserHelper.TaskList.add(tempTask)
            }
            catch (e: Exception) {
                Log.e("Error", e.toString())
            }

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