    package com.yugen.opsc7311_poe.helpers

    import android.util.Log
    import com.yugen.opsc7311_poe.objects.Session
    import com.yugen.opsc7311_poe.objects.Task
    import java.text.SimpleDateFormat
    import java.time.LocalDate
    import java.time.LocalTime
    import java.time.format.DateTimeFormatter
    import java.util.Locale

    class SessionsListHelper {
        companion object {

            fun updateHoursWorkedToday(sessions: List<Session>) {
                // Get today's date
                val today = LocalDate.now()

                // Filter sessions for today
                val todaySessions = sessions.filter { LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE) == today }

                // Calculate total hours worked today
                var totalHoursWorkedToday = 0

                todaySessions.forEach { session ->
                    val startTime = LocalTime.parse(session.startTime, DateTimeFormatter.ISO_LOCAL_TIME)
                    val endTime = LocalTime.parse(session.endTime, DateTimeFormatter.ISO_LOCAL_TIME)

                    val hoursWorked = endTime.hour - startTime.hour
                    val minutesWorked = endTime.minute - startTime.minute

                    totalHoursWorkedToday += hoursWorked
                    if (minutesWorked > 0) {
                        totalHoursWorkedToday++
                    }
                }

                // Update UserHelper's hoursWorkedToday
                UserHelper.hoursWorkedToday = totalHoursWorkedToday
            }

            fun filterByCategory(sessions: MutableList<Session>, category: String): MutableList<Session> {
                return sessions.filter { it.category == category }.toMutableList()
            }

            fun calculateTotalHoursInCategory(sessions: List<Session>, category: String): Int {
                var totalHours = 0

                // Calculate total hours for the specified category
                sessions.filter { it.category == category }.forEach { session ->
                    val startTimeParts = session.startTime.split(":").map { it.toInt() }
                    val endTimeParts = session.endTime.split(":").map { it.toInt() }

                    val startHour = startTimeParts[0]
                    val startMinute = startTimeParts[1]
                    val endHour = endTimeParts[0]
                    val endMinute = endTimeParts[1]

                    val hoursWorked = endHour - startHour
                    val minutesWorked = endMinute - startMinute

                    totalHours += hoursWorked
                    if (minutesWorked > 0) {
                        totalHours++
                    }
                }
                return totalHours
            }

            fun filterByDateRange(tasks: MutableList<Task>, startDate: String, endDate: String):
                    MutableList<Task>
            {
                val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

                Log.d("Start Date", startDate)
                Log.d("End Date", endDate)

                return tasks.filter { outputFormat.format(inputFormat.parse(it.date.toString())) in startDate..endDate }.toMutableList()
            }
        }
    }