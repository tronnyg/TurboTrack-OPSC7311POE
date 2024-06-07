package com.yugen.opsc7311_poe.helpers

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
   var minHours = DailyGoalMin/60
   var maxHours = DailyGoalMax/60

   var TaskList = mutableListOf<Task>()
   val ActivityList = mutableListOf<Activity>()
   val Medals:Medals? = null
   val WeeklyStats:WeeklyStats? = null

   enum class TaskCategory(val description: String) {
      WELLBEING("Physical & Mental Health"),
      RELATIONSHIPS("Friends, Family, Partners"),
      PRODUCTIVITY("Work & Personal Projects"),
      LEARNING_GROWTH("Developing Skills & Knowledge"),
      FINANCES("Money Management"),
      ENJOYMENT("Hobbies & Relaxation"),
      CONTRIBUTION("Giving Back & Helping Others"),
      GROUPWORK("Collaboration & Teamwork"),
      OTHER("Other")
   }
   val categoryList = TaskCategory.entries.toMutableList()
}