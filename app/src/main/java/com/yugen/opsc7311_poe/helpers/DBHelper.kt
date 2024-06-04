package com.yugen.opsc7311_poe.helpers

import android.app.Activity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.yugen.opsc7311_poe.objects.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DBHelper {
    private val personCollectionRef = Firebase.firestore.collection("Person")

    fun savePerson(person: Person) {
        try {
            personCollectionRef.add(person)
            personCollectionRef.document(person.userID).set(person)
        }
        catch (e: Exception) {
            println(e)
        }
    }
}