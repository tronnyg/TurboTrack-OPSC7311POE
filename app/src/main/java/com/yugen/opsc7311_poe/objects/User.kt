package com.yugen.opsc7311_poe.objects

data class User(
    var userID: String,
    var name: String,
    var surname: String,
    var password: String,
    var email: String,
    var experienceLevel: String,
    val profilePicture: String

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

