package com.yugen.opsc7311_poe

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.objects.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTimesheetDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTimesheetDetails : Fragment() {
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        // Decode base64 string to byte array
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

        // Convert byte array to Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timesheet_details, container, false)
        view.findViewById<TextView>(R.id.timesheet_entry_title).text = task!!.taskName
        view.findViewById<TextView>(R.id.entry_date_time).text = "Added ${task!!.date} - ${task!!.startTime} to ${task!!.endTime}"
        view.findViewById<TextView>(R.id.category_type).text = task!!.category
        view.findViewById<TextView>(R.id.description).text = task!!.taskDesc
        val taskCompleted = view.findViewById<CheckBox>(R.id.taskCompletedCheckBox)
        taskCompleted.isChecked = task!!.completed
        if(task!!.bitmapUrl != "none")
        {
            view.findViewById<ImageView>(R.id.attachment).setImageBitmap(base64ToBitmap(task!!.bitmapUrl!!))
        }
        taskCompleted.setOnCheckedChangeListener { _, isChecked ->
            val index = UserHelper.TaskList.indexOfFirst { it.taskName == task!!.taskName }
            UserHelper.TaskList[index] = task!!.copy(completed = isChecked)
            runBlocking {
                DBHelper.updatePersonTask(UserHelper.TaskList, UserHelper.loggedInUser)
            }
        }
        return view
    }

    fun setSession(task: Task?) {
        this.task = task
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentTimesheetDetails().apply {
                arguments = Bundle().apply {
                }
            }
    }
}