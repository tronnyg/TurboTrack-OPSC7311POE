package com.yugen.opsc7311_poe.objects

import android.graphics.Bitmap
import java.time.LocalTime
import java.util.Date

data class Task(
    val taskName: String,
    val taskDesc: String,
    val category: String,
    val date: Date,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val bitmapUrl: String,
    val duration: Int,
    val completed: Boolean
)
