package com.yugen.opsc7311_poe.helpers

import com.yugen.opsc7311_poe.objects.Activity
import com.yugen.opsc7311_poe.objects.Category
import com.yugen.opsc7311_poe.objects.Medals
import com.yugen.opsc7311_poe.objects.Task
import com.yugen.opsc7311_poe.objects.User
import com.yugen.opsc7311_poe.objects.WeeklyStats



object UserHelper {

   var loggedInUser: User? = null
   var minHours = 0
   var maxHours = 24
   var hoursWorkedToday = 0

   val TaskList = mutableListOf<Task>()
   val CategoryList = mutableListOf<Category>()
   val ActivityList = mutableListOf<Activity>()
   val Medals:Medals? = null
   val WeeklyStats:WeeklyStats? = null
}