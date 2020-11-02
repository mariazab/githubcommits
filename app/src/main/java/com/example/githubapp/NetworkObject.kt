package com.example.githubapp

import android.util.Log
import kotlin.properties.Delegates

object NetworkObject {
    var isConnected: Boolean by Delegates.observable(false) {
        property, oldValue, newValue ->
        Log.d("Network Connection", ": $newValue ")
    }
}