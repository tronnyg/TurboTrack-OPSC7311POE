package com.yugen.opsc7311_poe

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.yugen.opsc7311_poe.helpers.SessionsListHelper
import com.yugen.opsc7311_poe.helpers.UserHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var barChart: BarChart
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

    fun getDateOneWeekAgo(currentDate: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        return calendar.time
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_reports, container, false)
        val btnFilterByDate = view.findViewById<Button>(R.id.btn_filter_reports)

        barChart = view.findViewById(R.id.barChart)
        barChart.setDrawGridBackground(false)
        barChart.setGridBackgroundColor(R.color.transparent)

        var startDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(getDateOneWeekAgo(Date()))
        var endDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())
        updateChartWithSelectedDateRange(startDate, endDate)

        /*Filter Chart Button*/
        btnFilterByDate.setOnClickListener {

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
                startDate = btnStartDate.text.toString()
                endDate = btnEndDate.text.toString()
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    val toastMessage = "Please enter both start and end date."
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    updateChartWithSelectedDateRange(startDate, endDate)
                    alertDialog.dismiss()
                }
            }
        }
        return view
    }

    private fun showDatePicker(button: TextView) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                button.text = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun updateChartWithSelectedDateRange(startDate: String, endDate: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val start: Date = dateFormat.parse(startDate) ?: return
        val end: Date = dateFormat.parse(endDate) ?: return

        // Fetch tasks asynchronously and update chart
        runBlocking(Dispatchers.IO) {
            UserHelper.TaskList = DBHelper.getTaskCollection()
        }

        // Generate a list of all dates within the range
        val datesInRange = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        calendar.time = start
        while (!calendar.time.after(end)) {
            datesInRange.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
        }

        // Create a map of dates to tasks
        val taskMap = SessionsListHelper.filterByDateRange(UserHelper.TaskList, startDate, endDate).groupBy { task ->
            dateFormat.format(task.date)
        }

        // Create entries for the chart, ensuring all dates are represented
        val entries = ArrayList<BarEntry>()
        val days = ArrayList<String>()
        val colors = ArrayList<Int>()

        val turboDarkGreen = ContextCompat.getColor(requireContext(), R.color.turboDarkGreen)
        val turboDarkRed = ContextCompat.getColor(requireContext(), R.color.turboDarkRed)
        val turboDarkBlue = ContextCompat.getColor(requireContext(), R.color.turboDarkBlue)

        for (i in datesInRange.indices) {
            val date = datesInRange[i]
            val formattedDate = dateFormat.format(date)
            val tasksForDate = taskMap[formattedDate]
            val duration = tasksForDate?.sumBy { it.duration }?.toFloat() ?: 0f

            entries.add(BarEntry(i.toFloat(), duration/60))
            days.add(SimpleDateFormat("dd/MM", Locale.ENGLISH).format(date))

            // Set the color based on the duration
            val color = when {
                duration > UserHelper.DailyGoalMax -> turboDarkGreen
                duration < UserHelper.DailyGoalMin -> turboDarkRed
                else -> turboDarkBlue
            }
            colors.add(color)
        }

        val dataSet = BarDataSet(entries, "Hours Worked")
        dataSet.colors = colors
        val barData = BarData(dataSet)
        barChart.data = barData

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(days)

        val leftAxis = barChart.axisLeft
        leftAxis.removeAllLimitLines() // Clear existing limit lines

        val minGoal = LimitLine(UserHelper.DailyGoalMin.toFloat()/60, "Min Goal")
        minGoal.lineColor = Color.RED
        minGoal.lineWidth = 2f
        minGoal.textColor = Color.BLACK
        minGoal.textSize = 12f
        leftAxis.addLimitLine(minGoal)

        val maxGoal = LimitLine(UserHelper.DailyGoalMax.toFloat()/60, "Max Goal")
        maxGoal.lineColor = Color.GREEN
        maxGoal.lineWidth = 2f
        maxGoal.textColor = Color.BLACK
        maxGoal.textSize = 12f
        leftAxis.addLimitLine(maxGoal)

        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setDrawGridBackground(false)

        barChart.invalidate() // Refresh the chart
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