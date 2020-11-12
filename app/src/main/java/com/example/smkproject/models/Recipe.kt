package com.example.smkproject.models

import java.text.SimpleDateFormat
import java.util.*


class Recipe(_title: String, _describ: String, _date: Date) {
    var title: String
    var describ: String
    val dateTime: String
    init{
        title = _title
        describ = _describ
        val dateFormat = SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss"
        )
        dateTime = dateFormat.format(_date)
    }
}