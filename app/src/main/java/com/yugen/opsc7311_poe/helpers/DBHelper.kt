package com.yugen.opsc7311_poe.helpers

import android.app.Activity
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.yugen.opsc7311_poe.objects.Medals
import com.yugen.opsc7311_poe.objects.User
import com.yugen.opsc7311_poe.objects.Task
import com.yugen.opsc7311_poe.objects.WeeklyStats
import kotlinx.coroutines.tasks.await

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

    suspend fun getTaskCollection(userID: String): MutableList<Task> {
        val taskCollection = mutableListOf<Task>()
        try {
            val taskCollectionRef = db.collection("Users").document(userID).collection("Tasks")
            val taskList = taskCollectionRef.get().await()
            for (task in taskList) {
                taskCollection.add(task.toObject(Task::class.java))
            }
        } catch (e: Exception) {
            println(e)
        }
        return taskCollection
    }
    suspend fun getMedalsCollection(userID: String): Medals {
        var medals: Medals? = null
        try {
            val medalsDocRef = db.collection("Users").document(userID).collection("Medals").document("MedalDoc")
            medals = medalsDocRef.get().await().toObject(Medals::class.java)
        }
        catch (e: Exception) {
            println(e)
        }
        return medals!!
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
    suspend fun updatePersonMedals(medalsList: List<Medals>, user: User) {
        val taskCollection = db.document(user.userID).collection("Medals")

        // Delete existing tasks
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents) {
            document.reference.delete()
        }
        // Add new tasks
        for (medal in medalsList) {
            taskCollection.add(medal)
        }
    }
    suspend fun updatePersonWeeklyStats(weeklyStatsList: List<WeeklyStats>, user: User) {
        val taskCollection = db.document(user.userID).collection("WeeklyStats")

        // Delete existing tasks
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents) {
            document.reference.delete()
        }
        // Add new tasks
        for (weeklyStats in weeklyStatsList) {
            taskCollection.add(weeklyStats)
        }
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