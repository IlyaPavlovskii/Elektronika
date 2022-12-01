package by.bulba.watch.elektronika.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Context.activityIntent(): Intent =
    Intent(this, T::class.java)