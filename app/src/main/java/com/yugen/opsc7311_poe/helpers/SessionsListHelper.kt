    package com.yugen.opsc7311_poe.helpers

    import com.yugen.opsc7311_poe.objects.Session

    class SessionsListHelper {
        companion object {
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

            fun filterByDateRange(sessions: MutableList<Session>, startDate: String, endDate: String):
                    MutableList<Session>
            {
                return sessions.filter { it.date in startDate..endDate }.toMutableList()
            }
        }
    }
