package com.yugen.opsc7311_poe.helpers

import android.app.Activity
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.yugen.opsc7311_poe.dateString
import com.yugen.opsc7311_poe.objects.Medals
import com.yugen.opsc7311_poe.objects.User
import com.yugen.opsc7311_poe.objects.Task
import com.yugen.opsc7311_poe.objects.WeeklyStats
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DBHelper {
    private val db = FirebaseFirestore.getInstance()

    fun createNewUser(User: User, Medals: Medals) {
        val userCollectionRef = db.collection("Users")
        val userID =  userCollectionRef.document().id
        User.userID = userID
        db.collection("Users").document(userID).set(User)
        val userRef = db.collection("Users").document(userID)

        val taskCollectionRef = userRef.collection("Tasks")
        userRef.collection("Medals").document("MedalDoc").set(Medals)
        val activityCollectionRef = userRef.collection("Activity")
        val weeklyStatsCollectionRef = userRef.collection("WeeklyStats")
    }

    suspend fun getUsers(): MutableList<User> {
        val userCollection = mutableListOf<User>()
        try {
            val personCollectionRef = db.collection("Users")
            val personList = personCollectionRef.get().await()
            for (person in personList) {
                userCollection.add(person.toObject(User::class.java))
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return userCollection
    }

    suspend fun getTaskCollection(): MutableList<Task> {
        val taskCollection = mutableListOf<Task>()
        try {
            val userID = UserHelper.loggedInUser.userID
            val taskCollectionRef = db.collection("Users").document(userID).collection("Task")
            val taskList = taskCollectionRef.get().await()
            for (task in taskList) {
                taskCollection.add(task.toObject(Task::class.java))
            }
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
        return taskCollection
    }

    suspend fun getTaskCollectionWithUserID(userID: String): MutableList<Task> {
        val taskCollection = mutableListOf<Task>()
        try {
            val taskCollectionRef = db.collection("Users").document(userID).collection("Tasks")
            val taskList = taskCollectionRef.get().await()
            for (task in taskList) {
                taskCollection.add(task.toObject(Task::class.java))
            }
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
        return taskCollection
    }
    suspend fun getMedalsCollection(userID: String): Medals? {
        var medals: Medals? = null
        try {
            val medalsDocRef = db.collection("Users").document(userID).collection("Medals").document("MedalDoc")
            medals = medalsDocRef.get().await().toObject(Medals::class.java)
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return medals
    }
    suspend fun getWeeklyStatsCollection(userID: String): WeeklyStats {
        var weeklyStats: WeeklyStats? = null
        try {
            val medalsDocRef = db.collection("Users").document(userID).collection("WeeklyStats").document("WeeklyStatsDoc")
            weeklyStats = medalsDocRef.get().await().toObject(WeeklyStats::class.java)
        }
        catch (e: Exception) {
            println(e)
        }
        return weeklyStats!!
    }
    suspend fun getActivityCollection(userID: String): MutableList<Activity> {
        val taskCollection = mutableListOf<Activity>()
        try {
            val taskCollectionRef = db.collection("Users").document(userID).collection("Activity")
            val taskList = taskCollectionRef.get().await()
            for (task in taskList) {
                taskCollection.add(task.toObject(Activity::class.java))
            }
        }
        catch (e: Exception) {
            println(e)
        }
        return taskCollection
    }

    suspend fun updatePersonTask(taskList: List<Task>, user: User) {
        val userID = UserHelper.loggedInUser.userID
        val taskCollection = db.collection("Users").document(userID).collection("Task")

        // Delete existing tasks
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents) {
            document.reference.delete()
        }
        // Add new tasks
        for (task in taskList) {
            taskCollection.add(task)
        }
    }
    suspend fun updateMedals(userID: String, medals: Medals) {
        try {
            val medalsDocRef = db.collection("Users").document(userID).collection("Medals").document("MedalDoc")
            medalsDocRef.set(medals).await()
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }
    suspend fun calculateMonthlyXPAndUpdateMedals(userID: String): Pair<List<Double>, Double> {
        val taskCollection = UserHelper.TaskList
        Log.d("TaskCollection", taskCollection.count().toString())

        // Calculate total XP
        val totalXP = taskCollection.sumByDouble { it.duration * 1.7 }

        // Get the current date
        val calendar = Calendar.getInstance()
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        // Calculate XP for each month and assign medals
        val monthlyXP = mutableMapOf<String, Double>()  // Map from month-year to XP
        val medals = getMedalsCollection(userID) ?: Medals(0, 0, 0, 0, 0)

        for (task in taskCollection) {
            try {
                val formattedDate: Date = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(task.date.toString()) ?: Date()
                calendar.time = formattedDate
                Log.d("Date", formattedDate.toString())
                val taskMonth = calendar.get(Calendar.MONTH)
                val taskYear = calendar.get(Calendar.YEAR)
                val monthYearKey = "$taskMonth-$taskYear"
                val xp = task.duration * 1.7
                monthlyXP[monthYearKey] = (monthlyXP[monthYearKey] ?: 0.0) + xp
            } catch (e: Exception) {
                Log.e("Date Parsing Error", e.toString())
            }
        }

        val monthlyXPList = mutableListOf<Double>()

        for ((monthYear, xp) in monthlyXP) {
            monthlyXPList.add(xp)
            when {
                xp >= 5000 -> medals.rubyCnt += 1
                xp >= 4000 -> medals.purpleCnt += 1
                xp >= 3000 -> medals.goldCnt += 1
                xp >= 2000 -> medals.silverCnt += 1
                xp >= 1000 -> medals.bronzeCnt += 1
            }
        }

        updateMedals(userID, medals)
        return Pair(monthlyXPList, totalXP)
    }


    private fun filterByDateRange(tasks: MutableList<Task>, startDate: String, endDate: String): MutableList<Task> {
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        Log.d("Start Date", startDate)
        Log.d("End Date", endDate)

        return tasks.filter { outputFormat.format(inputFormat.parse(it.date.toString())) in startDate..endDate }.toMutableList()
    }


    suspend fun updatePersonActivity(activityList: List<Activity>, user: User) {
        val taskCollection = db.document(user.userID).collection("Activity")

        // Delete existing activities
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents) {
            document.reference.delete()
        }
        // Add new activity
        for (activity in activityList) {
            taskCollection.add(activity)
        }
    }
}
/*==========================END OF FILE====================================================================================================================================================*/