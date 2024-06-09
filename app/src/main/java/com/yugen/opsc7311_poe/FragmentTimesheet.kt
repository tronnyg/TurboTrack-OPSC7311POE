package com.yugen.opsc7311_poe

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yugen.opsc7311_poe.helpers.SessionAdapter
import com.yugen.opsc7311_poe.helpers.SessionsListHelper
import com.yugen.opsc7311_poe.helpers.UserHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTimesheet.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTimesheet : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var btnStartDate: Button
    private lateinit var btnEndDate: Button
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        runBlocking(Dispatchers.IO){
            UserHelper.TaskList = DBHelper.getTaskCollection()
        }
        val listView = view?.findViewById<ListView>(R.id.timesheet_list)
        val adapter = SessionAdapter(requireContext(), UserHelper.TaskList)
        if (listView != null) {
            listView.adapter = adapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timesheet, container, false)
        val btnAddEntry = view.findViewById<TextView>(R.id.btn_add_new)
        val btnFilterByDate = view.findViewById<Button>(R.id.btn_filter_by_date)
        val listView = view.findViewById<ListView>(R.id.timesheet_list)

        runBlocking(Dispatchers.IO){
            UserHelper.TaskList = DBHelper.getTaskCollection()
        }

        val adapter = SessionAdapter(requireContext(), UserHelper.TaskList)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            // Get the selected session
            val selectedTask = adapter?.getItem(position)

            // Display details of the selected session (e.g., in a new fragment or dialog)
            // For example:
            val detailsFragment = FragmentTimesheetDetails.newInstance()
            detailsFragment.setSession(selectedTask)
            fragmentManager?.beginTransaction()?.replace(R.id.frame_layout, detailsFragment)?.commit()
        }

   /*     btnStartDate.setOnClickListener{
            showDatePicker(btnStartDate)
        }
        btnEndDate.setOnClickListener{
            showDatePicker(btnEndDate)
        }*/

        btnAddEntry.setOnClickListener{
           replaceFragment(FragmentTimesheetEntry())
        }

        btnFilterByDate.setOnClickListener{

            val dialogView = layoutInflater.inflate(R.layout.dialog_filter, null)
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
            alertDialog = dialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(true)
            alertDialog.show()
            alertDialog.setOnCancelListener {
                // Handle the state reset or return to timer page
                Log.d("Dialog", "Dialog canceled")
            }

            btnStartDate = dialogView.findViewById(R.id.categories_start_date)
            btnEndDate = dialogView.findViewById(R.id.categories_end_date)
            val btnFilter = dialogView.findViewById<Button>(R.id.btn_submit_date)
            btnStartDate.setOnClickListener { showDatePicker(btnStartDate) }
            btnEndDate.setOnClickListener { showDatePicker(btnEndDate) }

            btnFilter.setOnClickListener {
                val startDate = btnStartDate.text.toString()
                val endDate = btnEndDate.text.toString()
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    val toastMessage = "Please enter both start and end date."
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    val filteredSessionList = SessionsListHelper.filterByDateRange( UserHelper.TaskList,btnStartDate.text.toString(),btnEndDate.text.toString())
                    listView.adapter = SessionAdapter(requireContext(), filteredSessionList )
                    alertDialog.dismiss()
                }
            }
        }
        return view
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

    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentTimesheet.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTimesheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}