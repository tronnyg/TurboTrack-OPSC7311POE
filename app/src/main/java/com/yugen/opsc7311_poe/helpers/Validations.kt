package com.yugen.opsc7311_poe.helpers

import android.util.Log
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object ValidationUtils {


    fun isValidEmail(email: String): Boolean {
        return if (email.isEmpty())
        {
            false
        }
        else
        {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
    fun isValidPassword(password: String): Boolean {
        val regex = Regex("""^(.{0,7}|[^0-9]*|[^A-Z]*|[^a-z]*|[a-zA-Z0-9]*)$""")
        return if(password.isEmpty())
        {
            false
        }
        else
        {
            !regex.matches(password)
        }
    }

    fun isValidString(input: String): Boolean
    {
        return if (input.isEmpty())
        {
            false
        }
        else
        {
            true
        }
    }

    fun isValidDateRange(dateOne: String, dateTwo: String):Boolean {
        // Define the date format
        val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE
        // Parse the input strings into LocalDate objects
        val parsedDateOne = LocalDate.parse(dateOne, dateFormat)
        val parsedDateTwo = LocalDate.parse(dateTwo, dateFormat)

        // Get today's date
        val today = LocalDate.now()

        // Check if dateOne is earlier than dateTwo and dateTwo is earlier or equal to today's date
        return if (parsedDateOne.isBefore(parsedDateTwo) && !parsedDateTwo.isAfter(today))
        {
            true
        }
        else
        {
            false
        }
    }
    fun isValidDate(date: String): Boolean {
        // Define the date format
        val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE

        return try {
            // Parse the input string into a LocalDate object
            val parsedDate = LocalDate.parse(date, dateFormat)

            // Get today's date
            val today = LocalDate.now()

            // Check if the parsed date is not after today's date
            !parsedDate.isAfter(today)
        } catch (e: Exception) {
            println("Invalid date format. Please provide the date in yyyy-mm-dd format.")
            false
        }
    }

    fun isValidTimeRange(timeOne: String, timeTwo: String): Boolean {
        // Define the time format
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

        try {
            // Parse the input strings into LocalTime objects
            val parsedTimeOne = LocalTime.parse(timeOne, timeFormat)
            val parsedTimeTwo = LocalTime.parse(timeTwo, timeFormat)

            // Get current time
            val currentTime = LocalTime.now()

            // Check if timeOne is earlier than timeTwo and timeTwo is earlier or equal to the current time
            return if (parsedTimeOne.isBefore(parsedTimeTwo) && !parsedTimeTwo.isAfter(currentTime)) {
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.d("InvalidTime","Invalid time format. Please provide the time in HH:mm 24-hour format.")
            return false
        }
    }
}