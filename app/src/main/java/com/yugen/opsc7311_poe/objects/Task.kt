package com.yugen.opsc7311_poe.objects

import java.util.Date

data class Task(
    val taskName: String,
    val taskDesc: String,
    val category: String,
    val date: Date,
    val startTime: Date,
    val endTime: Date,
    val bitmapUrl: String,
    val duration: Int,
    val completed: Boolean
)
