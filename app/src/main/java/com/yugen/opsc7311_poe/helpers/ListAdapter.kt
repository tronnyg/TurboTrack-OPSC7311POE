package com.yugen.opsc7311_poe.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.yugen.opsc7311_poe.R
import com.yugen.opsc7311_poe.objects.Task
import java.text.SimpleDateFormat
import java.util.Locale

class SessionAdapter(context: Context, private val tasks: List<Task>) : ArrayAdapter<Task>(context, R.layout.list_item, tasks) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder()
            viewHolder.entryTitleTextView = view.findViewById(R.id.entry_title)
            viewHolder.entryDescriptionTextView = view.findViewById(R.id.entry_description)
            viewHolder.entryDateTimeTextView = view.findViewById(R.id.entry_date_time)

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val task = tasks[position]
        viewHolder.entryTitleTextView.text = task.taskName
        viewHolder.entryDescriptionTextView.text = task.taskDesc
        val dateFormat = SimpleDateFormat("MMMM d, yyyy 'at' z", Locale.getDefault())
        viewHolder.entryDateTimeTextView.text = "${dateFormat.format(task.date)} - ${task.startTime} to ${task.endTime}"

        return view!!
    }

    private class ViewHolder {
        lateinit var entryTitleTextView: TextView
        lateinit var entryDescriptionTextView: TextView
        lateinit var entryDateTimeTextView: TextView
        // Add other views if needed
    }
}

/*class CategoryAdapter(context: Context, private val categories: List<Category>) :
    ArrayAdapter<Category>(context, R.layout.category_item, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
            viewHolder = ViewHolder()
            viewHolder.titleTextView = view.findViewById(R.id.category_title)
            viewHolder.hoursTextView = view.findViewById(R.id.category_hours)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val category = categories[position]
        viewHolder.titleTextView.text = category.categoryName
        viewHolder.hoursTextView.text = "Total hours worked: ${category.categoryHours}"
        return view!!
    }*/

    private class ViewHolder {
        lateinit var titleTextView: TextView
        lateinit var hoursTextView: TextView

    }
/*==========================END OF FILE====================================================================================================================================================*/
