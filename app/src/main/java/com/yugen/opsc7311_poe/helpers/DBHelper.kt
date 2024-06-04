package com.yugen.opsc7311_poe.helpers

import android.app.Activity
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
    private val personCollectionRef = Firebase.firestore.collection("Person")

    fun savePerson(person: Person)
    {
        try
        {
            personCollectionRef.add(person)
            personCollectionRef.document(person.userID).set(person)
        }
        catch (e: Exception)
        {
            println(e)
        }
    }


    suspend fun updatePersonTask(taskList: List<Task>, person: Person)
    {
        val taskCollection = db.document(person.userID).collection("Task")

        // Delete existing tasks
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents)
        {
            document.reference.delete()
        }
        // Add new tasks
        for (task in taskList)
        {
            taskCollection.add(task)
        }
    }


    suspend fun updatePersonMedals(medalsList: List<Medals>, person: Person)
    {
        val taskCollection = db.document(person.userID).collection("Medals")

        // Delete existing tasks
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents)
        {
            document.reference.delete()
        }
        // Add new tasks
        for (medal in medalsList)
        {
            taskCollection.add(medal)
        }
    }

    suspend fun updatePersonWeeklyStats(weeklyStatsList: List<WeeklyStats>, person: Person)
    {
        val taskCollection = db.document(person.userID).collection("WeeklyStats")

        // Delete existing tasks
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents)
        {
            document.reference.delete()
        }
        // Add new tasks
        for (weeklyStats in weeklyStatsList)
        {
            taskCollection.add(weeklyStats)
        }
    }

    suspend fun updatePersonActivity(activityList: List<Activity>, person: Person)
    {
        val taskCollection = db.document(person.userID).collection("Activity")

        // Delete existing activities
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents)
        {
            document.reference.delete()
        }
        // Add new activity
        for (activity in activityList)
        {
            taskCollection.add(activity)
        }
    }

    suspend fun updatePersonCategory(categoryList: List<Category>, person: Person)
    {
        val taskCollection = db.document(person.userID).collection("Task")

        // Delete existing tasks
        val snapshot = taskCollection.get().await()
        for (document in snapshot.documents)
        {
            document.reference.delete()
        }
        // Add new tasks
        for (category in categoryList)
        {
            taskCollection.add(category)
        }
    }

}