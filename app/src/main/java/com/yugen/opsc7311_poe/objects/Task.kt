package com.yugen.opsc7311_poe.objects

import android.graphics.Bitmap
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date

data class Task(
    val taskName: String,
    val taskDesc: String,
    val category: String,
    val date: Date,
    val startTime: String?,
    val endTime: String?,
    val bitmapUrl: String?,
    var duration: Int,
    val completed: Boolean
)
{   constructor() : this(
          taskName = "",
            taskDesc = "",
            category = "",
            date = Date(),
            startTime = "",
            endTime = "",
            bitmapUrl = "",
            duration = 0,
            completed = false
)
}
