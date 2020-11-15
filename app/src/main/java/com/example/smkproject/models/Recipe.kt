package com.example.smkproject.models

import java.text.SimpleDateFormat
import java.util.*


class Recipe(_title: String, _describ: String, _date: String, _tags: Array<String>) {
    var title: String
    var describ: String
    var dateTime: String
    var tags: Array<String> = arrayOf()
    init{
        title = _title
        describ = _describ
        dateTime = _date
        tags = _tags
    }
}