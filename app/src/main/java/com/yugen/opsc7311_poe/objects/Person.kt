package com.yugen.opsc7311_poe.objects

import android.graphics.Bitmap

data class Person(
    var userID: String,
    var name: String,
    var surname: String,
    var password: String,
    var email: String,
    var experienceLevel: String,
    val profilePicture: String
    // CATEGORY COLLECTION
    // TASKS COLLECTION

)
{   constructor() : this(
        userID = "",
        name = "",
        surname = "",
        password = "",
        email = "",
        experienceLevel = "",
        profilePicture = ""
    )
}

