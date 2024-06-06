import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.FirebaseFirestore
import com.yugen.opsc7311_poe.DBHelper
import com.yugen.opsc7311_poe.R
import com.yugen.opsc7311_poe.helpers.SessionsListHelper
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.objects.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.Date

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentCalendar : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var pieChart: PieChart
    private val db = FirebaseFirestore.getInstance()

    private var maxGoal = 0
    private var minGoal = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        // Initialize the PieChart
        pieChart = view.findViewById(R.id.chart)

        // Fetch data for the past 30 days and calculate counts
        val (daysAboveMaxGoal, daysBelowMinGoal, daysBetweenMinMaxGoals) = fetchDataForPast30Days()

        // Create pie chart entries
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(daysAboveMaxGoal.toFloat(), "Days Above Max Goal"))
        entries.add(PieEntry(daysBelowMinGoal.toFloat(), "Days Below Min Goal"))
        entries.add(PieEntry(daysBetweenMinMaxGoals.toFloat(), "Days Between Min and Max Goals"))

        val pieDataSet = PieDataSet(entries, "Goal Status")
        // Define your custom colors
        val customColors = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.turboDarkRed),
            ContextCompat.getColor(requireContext(), R.color.turboDarkBlue),
            ContextCompat.getColor(requireContext(), R.color.turboDarkGreen)
        )

        // Assign custom colors to your PieDataSet
        pieDataSet.colors = customColors.toList()

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(16f)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.animateY(1000)

        pieChart.invalidate() // refresh

        return view
    }

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
        val currentDate = Date().time
        val thirtyDaysAgo = currentDate - 30 * 24 * 60 * 60 * 1000 // 30

         val taskCollection = SessionsListHelper.filterByDateRange(UserHelper.TaskList,thirtyDaysAgo.toString(), currentDate.toString())

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
