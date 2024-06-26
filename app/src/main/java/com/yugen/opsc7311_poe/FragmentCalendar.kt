import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.yugen.opsc7311_poe.DBHelper
import com.yugen.opsc7311_poe.R
import com.yugen.opsc7311_poe.helpers.SessionsListHelper
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.objects.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentCalendar : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var pieChart: PieChart
    private var totalHour30Days: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun getTotalDuration(taskCollection: List<Task>): Int {
        var totalDuration = 0
        for (task in taskCollection) {
            totalDuration += task.duration
        }
        return totalDuration
    }

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*Inflate the layout for this fragment*/
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        /*Initialize the PieChart*/
        pieChart = view.findViewById(R.id.chart)

        /*Fetch data for the past 30 days and calculate counts*/
        val (daysAboveMaxGoal, daysBelowMinGoal, daysBetweenMinMaxGoals) = fetchDataForPast30Days()

        /*Create pie chart entries*/
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(daysAboveMaxGoal.toFloat(), ""))
        entries.add(PieEntry(daysBelowMinGoal.toFloat(), ""))
        entries.add(PieEntry(daysBetweenMinMaxGoals.toFloat(), ""))

        val pieDataSet = PieDataSet(entries, "")
        /*Define your custom colors*/
        val customColors = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.turboYellow),
            ContextCompat.getColor(requireContext(), R.color.turboDarkRed),
            ContextCompat.getColor(requireContext(), R.color.turboBlue)
        )

        /*Assign custom colors to your PieDataSet*/
        pieDataSet.colors = customColors.toList()

        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(16f)
        pieData.setValueTextColor(Color.BLACK)
        pieChart.data = pieData
        pieChart.holeRadius = 60f
        pieChart.setHoleColor(ContextCompat.getColor(requireContext(), R.color.transparent));
        pieChart.transparentCircleRadius = 60f
        pieChart.setUsePercentValues(true)
        pieChart.animateY(1000)
        pieChart.animateX(1000)
        pieChart.centerText = totalHour30Days.toString() + " TOTAL HOURS"
        pieChart.setCenterTextSize(25f)
        pieChart.setCenterTextColor(R.color.turboBlue)
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.invalidate() // refresh

        return view
    }

    /*Fetch Task Date for Last 30 Days*/
    private fun fetchDataForPast30Days(): Triple<Int, Int, Int> {
        // Fetch the task collection for the past 30 days
        runBlocking(Dispatchers.IO){
            UserHelper.TaskList = DBHelper.getTaskCollection()
        }
        // Initialize counts
        var daysAboveMaxGoal = 0
        var daysBelowMinGoal = 0
        var daysBetweenMinMaxGoals = 0

        //Calculate Dates
        val currentDate = Date()
        val thirtyDaysAgo = Date(currentDate.time - 30L * 24 * 60 * 60 * 1000) // 30 days ago

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val formattedCurrentDate = format.format(currentDate)
        val formattedThirtyDaysAgo = format.format(thirtyDaysAgo)

        val taskCollection = SessionsListHelper.filterByDateRange(UserHelper.TaskList, formattedThirtyDaysAgo, formattedCurrentDate)
        totalHour30Days = getTotalDuration(taskCollection) / 60
        val amountOfTasks = taskCollection.count()
        val missingTasks = 30-amountOfTasks
        daysBelowMinGoal = missingTasks
        Log.d(taskCollection.toString(), "taskCollection")
        // Calculate counts based on tasks in the past 30 days
        for (task in taskCollection) {
            val duration = task.duration
            if (duration > UserHelper.DailyGoalMax) {
                daysAboveMaxGoal++
            } else if (duration < UserHelper.DailyGoalMin) {
                daysBelowMinGoal++
            } else {
                daysBetweenMinMaxGoals++
            }
        }

        return Triple(daysAboveMaxGoal, daysBelowMinGoal, daysBetweenMinMaxGoals)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentCalendar().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
