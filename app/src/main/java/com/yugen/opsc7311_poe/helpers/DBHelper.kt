package com.yugen.opsc7311_poe.helpers

import android.app.Activity
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.yugen.opsc7311_poe.objects.Category
import com.yugen.opsc7311_poe.objects.Medals
import com.yugen.opsc7311_poe.objects.Person
import com.yugen.opsc7311_poe.objects.Task
import com.yugen.opsc7311_poe.objects.User
import com.yugen.opsc7311_poe.objects.WeeklyStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DBHelper {
    private val db = FirebaseFirestore.getInstance()

    fun createNewUser(Person: Person, Medals: Medals) {
        val userCollectionRef = db.collection("Users")
        val userID =  userCollectionRef.document().id
        Person.userID = userID
        db.collection("Users").document(userID).set(Person)
        val userRef = db.collection("Users").document(userID)

        val taskCollectionRef = userRef.collection("Tasks")
        userRef.collection("Medals").document("MedalDoc").set(Medals)
        val activityCollectionRef = userRef.collection("Activity")
        val weeklyStatsCollectionRef = userRef.collection("WeeklyStats")
    }

    suspend fun getUsers(): MutableList<Person> {
        val personCollection = mutableListOf<Person>()
        try {
            val personCollectionRef = db.collection("Users")
            val personList = personCollectionRef.get().await()
            for (person in personList) {
                personCollection.add(person.toObject(Person::class.java))
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return personCollection
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
    suspend fun getCategoryCollection(userID: String): MutableList<Category> {
        val taskCollection = mutableListOf<Category>()
        try {
            val taskCollectionRef = db.collection("Users").document(userID).collection("Category")
            val taskList = taskCollectionRef.get().await()
            for (task in taskList) {
                taskCollection.add(task.toObject(Category::class.java))
            }
        }
        catch (e: Exception) {
            println(e)
        }
        return taskCollection
    }


    suspend fun updatePersonTask(taskList: List<Task>, person: Person) {
        val taskCollection = db.document(person.userID).collection("Task")

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
    suspend fun updatePersonMedals(medalsList: List<Medals>, person: Person) {
        val taskCollection = db.document(person.userID).collection("Medals")

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
    suspend fun updatePersonWeeklyStats(weeklyStatsList: List<WeeklyStats>, person: Person) {
        val taskCollection = db.document(person.userID).collection("WeeklyStats")

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
    suspend fun updatePersonActivity(activityList: List<Activity>, person: Person) {
        val taskCollection = db.document(person.userID).collection("Activity")

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
    suspend fun updatePersonCategory(categoryList: List<Category>, person: Person) {
        val taskCollection = db.document(person.userID).collection("Task")

        // Delete existing tasks
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents) {
            document.reference.delete()
        }
        // Add new tasks
        for (category in categoryList) {
            taskCollection.add(category)
        }
    }
}