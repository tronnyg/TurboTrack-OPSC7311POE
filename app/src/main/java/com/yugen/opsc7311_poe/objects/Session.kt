package com.yugen.opsc7311_poe.objects


public class Session {
    lateinit var taskName: String
    lateinit var taskDesc: String
    lateinit var category: String
    lateinit var date: String
    var startTimeHour: Int = 0
    var startTimeMinute: Int = 0
    var endTimeHour: Int = 0
    var endTimeMinute: Int = 0


    constructor(tName: String, tDesc: String, tCat: String, tDate: String, sTimeHour: Int, sTimeMinute: Int, eTimeHour: Int, eTimeMinute: Int) : this()
    {
        this.taskName = tName
        this.taskDesc = tDesc
        this.category = tCat
        this.date = tDate
        this.startTimeHour = sTimeHour
        this.startTimeMinute = sTimeMinute
        this.endTimeHour = eTimeHour
        this.endTimeMinute = eTimeMinute
    }
    constructor()
}

