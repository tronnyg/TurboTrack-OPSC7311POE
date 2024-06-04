package com.yugen.opsc7311_poe.helpers

import com.yugen.opsc7311_poe.objects.Activity
import com.yugen.opsc7311_poe.objects.Category
import com.yugen.opsc7311_poe.objects.Medals
import com.yugen.opsc7311_poe.objects.Task
import com.yugen.opsc7311_poe.objects.User
import com.yugen.opsc7311_poe.objects.WeeklyStats

private val TaskList = mutableListOf<Task>()
private val CategoryList = mutableListOf<Category>()
private val ActivityList = mutableListOf<Activity>()
private val MedalsList = mutableListOf<Medals>()
private  val WeeklyStatsList = mutableListOf<WeeklyStats>()

object UserHelper {

   var loggedInUser: User? = null
   var minHours = 0
   var maxHours = 24
   var hoursWorkedToday = 0
}