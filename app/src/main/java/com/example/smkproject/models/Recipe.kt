package com.example.smkproject.models

import java.text.SimpleDateFormat
import java.util.*


class Recipe(_title: String, _describ: String, _date: String, _tags: String) {
    var title: String
    var describ: String
    var dateTime: String
    var tags: String = ""
    init{
        title = _title
        describ = _describ
        dateTime = _date
        tags = _tags
    }
}