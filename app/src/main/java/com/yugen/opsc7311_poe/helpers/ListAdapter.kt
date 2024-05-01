package com.yugen.opsc7311_poe.helpers

import android.content.Context
import com.yugen.opsc7311_poe.objects.Session
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.yugen.opsc7311_poe.R

class SessionAdapter(context: Context, private val sessions: List<Session>) : ArrayAdapter<Session>(context, R.layout.list_item, sessions) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder()
            viewHolder.entryTitleTextView = view.findViewById(R.id.entry_title)
            viewHolder.entryDescriptionTextView = view.findViewById(R.id.entry_description)
            viewHolder.entryDateTimeTextView = view.findViewById(R.id.entry_date_time)
            // Add other views if needed
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val session = sessions[position]
        viewHolder.entryTitleTextView.text = session.taskName
        viewHolder.entryDescriptionTextView.text = session.taskDesc
        viewHolder.entryDateTimeTextView.text = "${session.date} - ${session.startTime} to ${session.endTime}"

        return view!!
    }

    private class ViewHolder {
        lateinit var entryTitleTextView: TextView
        lateinit var entryDescriptionTextView: TextView
        lateinit var entryDateTimeTextView: TextView
        // Add other views if needed
    }
}
