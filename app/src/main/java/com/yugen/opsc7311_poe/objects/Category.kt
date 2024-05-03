package com.yugen.opsc7311_poe.objects

import android.graphics.Bitmap
import android.graphics.Color

class Category {
    lateinit var categoryName: String
    lateinit var categoryColor: Color
    var categoryHours: Int = 0



    constructor(categoryName: String, categoryHours: Int) : this()
    {
        this.categoryName = categoryName
    }

    fun updateHours(hours: Int)
    {
        categoryHours = hours
    }
    constructor()
}