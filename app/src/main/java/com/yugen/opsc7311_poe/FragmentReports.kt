package com.yugen.opsc7311_poe

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentReports.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentReports : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_reports, container, false)
        val listView = view.findViewById<ListView>(R.id.categories_list)
        val btnFilterByDate = view.findViewById<Button>(R.id.btn_filter_reports)
        val btnStartDate = view.findViewById<Button>(R.id.categories__start_date)
        val btnEndDate = view.findViewById<Button>(R.id.categories_end_date)

       /* val adapter = UserHelper.loggedInUser?.let { CategoryAdapter(requireContext(), it.categoryList  ) }
        listView.adapter = adapter*/

        btnStartDate.setOnClickListener{
            showDatePicker(btnStartDate)
        }
        btnEndDate.setOnClickListener{
            showDatePicker(btnEndDate)
        }

        btnFilterByDate.setOnClickListener {
          /*  val filteredSessionList = SessionsListHelper.filterByDateRange( UserHelper.loggedInUser!!.sessionList,btnStartDate.text.toString(),btnEndDate.text.toString())
            val tempCategories = UserHelper.loggedInUser!!.categoryList
            tempCategories.forEach { category ->  category.updateHours(SessionsListHelper.calculateTotalHoursInCategory(filteredSessionList, category.categoryName) ) }
            listView.adapter = CategoryAdapter(requireContext(), tempCategories)*/
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentReports.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentReports().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}