package com.yugen.opsc7311_poe

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yugen.opsc7311_poe.helpers.SessionAdapter
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
        val view = inflater.inflate(R.layout.fragment_timesheet, container, false)

        val btnStartDate = view.findViewById<Button>(R.id.btn_Start_Date)
        val btnEndDate = view.findViewById<Button>(R.id.btn_End_Date)
        val btnAddEntry = view.findViewById<TextView>(R.id.txt_Plus)
        val btnFilterByDate = view.findViewById<Button>(R.id.btn_filter_by_date)
        val listView = view.findViewById<ListView>(R.id.timesheet_list)

        runBlocking(Dispatchers.IO){
            UserHelper.TaskList = DBHelper.getTaskCollection()
            Log.d("Test", UserHelper.TaskList.toString())
        }
        val adapter = SessionAdapter(requireContext(), UserHelper.TaskList)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            // Get the selected session
           /* val selectedSession = adapter?.getItem(position)

            // Display details of the selected session (e.g., in a new fragment or dialog)
            // For example:
            val detailsFragment = FragmentTimesheetDetails.newInstance()
            detailsFragment.setSession(selectedSession)
            fragmentManager?.beginTransaction()?.replace(R.id.frame_layout, detailsFragment)?.commit()*/
        }

        btnStartDate.setOnClickListener{
            showDatePicker(btnStartDate)
        }
        btnEndDate.setOnClickListener{
            showDatePicker(btnEndDate)
        }

        btnAddEntry.setOnClickListener{
           replaceFragment(FragmentTimesheetEntry())
        }

        btnFilterByDate.setOnClickListener{
/*
            val filteredSessionList = SessionsListHelper.filterByDateRange( UserHelper.loggedInUser!!.sessionList,btnStartDate.text.toString(),btnEndDate.text.toString())
            listView.adapter = SessionAdapter(requireContext(), filteredSessionList )*/
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