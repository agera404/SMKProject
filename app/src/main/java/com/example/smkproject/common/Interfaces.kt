package com.example.smkproject.common

import android.content.Context

interface ViewNavigator {
    fun navigateTo(target: Class<*>)
}
interface ViewContext{
    fun getContex(): Context
}