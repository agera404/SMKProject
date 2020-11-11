package com.example.smkproject.models

import java.sql.Time

class Recipe(_title: String, _describ: String, _time: Time) {
    var title: String
    var describ: String
    val time: Time
    init{
        title = _title
        describ = _describ
        time = _time
    }
}