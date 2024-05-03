package com.yugen.opsc7311_poe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yugen.opsc7311_poe.objects.Session



/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTimesheetDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTimesheetDetails : Fragment() {
    private var session: Session? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timesheet_details, container, false)
        view.findViewById<TextView>(R.id.timesheet_entry_title).text = session!!.taskName
        view.findViewById<TextView>(R.id.entry_date_time).text = "Added ${session!!.date} - ${session!!.startTime} to ${session!!.endTime}"
        view.findViewById<TextView>(R.id.category_type).text = session!!.category
        view.findViewById<TextView>(R.id.description).text = session!!.taskDesc
        view.findViewById<ImageView>(R.id.attachment).setImageBitmap(session!!.attachedImage)
        return view
    }

    fun setSession(session: Session?) {
        this.session = session
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