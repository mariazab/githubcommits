package com.example.githubapp

import java.util.*

//Const and methods used in many places
const val COMMIT_AUTHOR_NAME: String = "COMMIT_AUTHOR_NAME"
const val COMMIT_AUTHOR_EMAIL: String = "COMMIT_AUTHOR_EMAIL"
const val COMMIT_DATE: String = "COMMIT_DATE"
const val COMMIT_COMMITTER_NAME: String = "COMMIT_COMMITTER_NAME"
const val COMMIT_MESSAGE: String = "COMMIT_MESSAGE"
const val COMMIT_AUTHOR_AVATAR_URL: String = "COMMIT_AUTHOR_AVATAR_URL"
const val COMMIT_URL: String = "COMMIT_URL"

//Formatting the date as a string
fun getDateAsString(date: Date): String {
    //Create an calendar object and set it to the provided date
    var calendar = Calendar.getInstance()
    calendar.time = date

    //Get the fields from the calendar object
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1
    val year = calendar.get(Calendar.YEAR)

    return "$day.$month.$year"

}