package com.example.smkproject.models

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize

@Parcelize
class Recipe() : Parcelable {
    var id: Long = -1
    var title: String = ""
    var describ: String = ""
    var dateTime: String = ""
    var tags: String = ""
    var ingredients = arrayListOf<Ingredient>()
    var stringIngredient = ""

    constructor(
        _id: Long,
        _title: String,
        _describ: String,
        _dateTime: String,
        _tags: String,
        _ingredients: ArrayList<Ingredient>
    ) : this() {
        id = _id
        title = _title
        describ = _describ
        dateTime = _dateTime
        tags = _tags
        ingredients = _ingredients
        for (i in ingredients) {
            stringIngredient += "${i.ingredient} (${i.amount} ${i.unit}), "
        }
        stringIngredient = stringIngredient.dropLast(2)
    }

    constructor(
        _id: Long,
        _title: String,
        _describ: String,
        _dateTime: String,
        _tags: String,
        _ingredients: String
    ) : this() {
        id = _id
        title = _title
        describ = _describ
        dateTime = _dateTime
        tags = _tags
        stringIngredient = _ingredients
        for (i in stringIngredient.split(",").toTypedArray()) {

            var tempAmount = i.substringAfter("(").substringBefore(" ").replace(" ", "")
            if(tempAmount.isNotEmpty())
            {
                var ingr = i.substringBefore("(").replace(" ", "")
                var amount: Double = tempAmount.toDouble()
                var unit = i.substringAfter("(").substringAfter(" ").dropLast(1)
                ingredients.add(Ingredient(ingr, amount, unit))
            }

        }

    }


}

