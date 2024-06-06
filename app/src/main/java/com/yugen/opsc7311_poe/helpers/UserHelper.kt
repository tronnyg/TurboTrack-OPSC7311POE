package com.yugen.opsc7311_poe.helpers

import com.yugen.opsc7311_poe.objects.Activity
import com.yugen.opsc7311_poe.objects.Medals
import com.yugen.opsc7311_poe.objects.User
import com.yugen.opsc7311_poe.objects.Task
import com.yugen.opsc7311_poe.objects.WeeklyStats



object UserHelper {

   lateinit var loggedInUser: User
   var minHours = 0
   var maxHours = 24
   var hoursWorkedToday = 0
   var currentUserID = ""

   var DailyGoalMin = 1
   var DailyGoalMax = 3

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