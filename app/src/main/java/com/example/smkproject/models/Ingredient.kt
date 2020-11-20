package com.example.smkproject.models

class Ingredient(_ingredient: String, _amount: Double, _unit: String) {
    var id:Long
        get() {
           return id
        }
        set(value) {
            id = value
        }
    var ingredient = _ingredient
    var amount = _amount
    var unit = _unit
}