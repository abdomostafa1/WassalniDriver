package com.example.wassalniDR.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class Drivers(
    val email:String,
    val password:String
)
{
    constructor():this("","")

}





