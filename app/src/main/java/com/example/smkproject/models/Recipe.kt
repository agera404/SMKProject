package com.example.smkproject.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Recipe() : Parcelable {
    var id: Long= -1
    var title: String = ""
    var describ: String=""
    var dateTime: String = ""
    var tags: String = ""

    constructor(_id: Long,_title: String,_describ: String, _dateTime: String, _tags: String) : this() {
        id=_id
        title=_title
        describ=_describ
        dateTime=_dateTime
        tags=_tags
    }


}

