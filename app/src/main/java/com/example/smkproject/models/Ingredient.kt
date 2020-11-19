package com.example.smkproject.models

class Ingredient(ingredient: String, amount: Float, unit: String) {
    var id:Long
        get() {
           return id
        }
        set(value) {
            id = value
        }
}