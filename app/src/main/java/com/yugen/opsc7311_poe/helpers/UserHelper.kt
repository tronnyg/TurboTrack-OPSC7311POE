package com.yugen.opsc7311_poe.helpers

import com.yugen.opsc7311_poe.objects.User

object UserHelper {

   var loggedInUser: User? = null
   var minHours = 0
   var maxHours = 24
   var hoursWorkedToday = 0
}