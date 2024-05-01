package com.yugen.opsc7311_poe.objects

class User {

    lateinit var username: String
    lateinit var email: String
    lateinit var password: String
    lateinit var userId: String
    var sessionList: MutableList<Session> = mutableListOf()

    constructor(username: String, password: String, userId: String, email: String) : this()
    {
        this.username = username
        this.password = password
        this.userId = userId
        this.email = email
    }

    fun addSession(session: Session)
    {
        sessionList.add(session)
    }

    constructor()
}