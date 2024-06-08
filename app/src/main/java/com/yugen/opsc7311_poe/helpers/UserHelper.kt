package com.yugen.opsc7311_poe.helpers

import androidx.compose.ui.graphics.Color
import com.yugen.opsc7311_poe.objects.Activity
import com.yugen.opsc7311_poe.objects.Medals
import com.yugen.opsc7311_poe.objects.User
import com.yugen.opsc7311_poe.objects.Task
import com.yugen.opsc7311_poe.objects.WeeklyStats



object UserHelper {

   lateinit var loggedInUser: User
   var currentUserID = ""

   var DailyGoalMin = 60
   var DailyGoalMax = 180
   var currentDayHours = 0
   var minHours = DailyGoalMin/60
   var maxHours = DailyGoalMax/60

   var TaskList = mutableListOf<Task>()
   val ActivityList = mutableListOf<Activity>()
   val Medals:Medals? = null
   val WeeklyStats:WeeklyStats? = null

   enum class TaskCategory(val displayName: String, val color: String) {
      PRODUCTIVITY("Work & Productivity", "#FF6347"),
      LEARNING_GROWTH("Learning & Growth", "#87CEFA"),
      FINANCES("Finances", "#90EE90"),
      ENJOYMENT("Hobbies", "#FFD700"),
      PERSONAL("Personal Projects", "#DDA0DD"),
      GROUPWORK("Collaboration & Teamwork", "#FFB6C1"),
      OTHER("Other", "#D2B48C")
   }
   val categoryList = TaskCategory.entries.map { it.displayName }
}
/*==========================END OF FILE====================================================================================================================================================*/