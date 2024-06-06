package com.yugen.opsc7311_poe.objects

import android.graphics.Bitmap


public class Session {
    lateinit var taskName: String
    lateinit var taskDesc: String
    lateinit var category: String
    lateinit var date: String
    lateinit var startTime: String
    lateinit var endTime: String
    lateinit var attachedImage: Bitmap

    constructor(tName: String, tDesc: String, tCat: String, tDate: String, sTime: String, eTime: String, aImage: Bitmap) : this()
    {
        this.taskName = tName
        this.taskDesc = tDesc
        this.category = tCat
        this.date = tDate
        this.startTime = sTime
        this.endTime = eTime
        this.attachedImage = aImage
    }
    constructor()
}

