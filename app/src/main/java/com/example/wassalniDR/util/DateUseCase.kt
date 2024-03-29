package com.example.wassalniDR.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DateUseCase"

class DateUseCase {
    companion object {
        fun fromMillisToHhMma(millis: Long): String {

            val date = Date(millis*1000) // create a Date object from the millisecond value

            val sdf = SimpleDateFormat("hh:mm a") // create a date format

            Log.e(TAG, "fromMillisToString1: ", )
            return sdf.format(date)

        }

        fun fromMillisToPatternddMMyyyyhhmm(millis: Long): String {

            val date = Date(millis*1000) // create a Date object from the millisecond value
            val sdf = SimpleDateFormat(" dd/MM/yyyy hh:mm a") // create a date format

            return sdf.format(date)

        }
        fun fromMillisToPatternddMMyyyy(millis: Long): String {

            val date = Date(millis*1000) // create a Date object from the millisecond value
            val sdf = SimpleDateFormat("dd/MM/yyyy") // create a date format
            val l=sdf.format(date)
            Log.e(TAG, "Millis:$millis ", )
            Log.e(TAG, "fromMillisToString3:$l ", )

            return l
        }
    }
}