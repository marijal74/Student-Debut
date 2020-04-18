package com.example.studentdebut.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = arrayOf(Entity::class), version = 1)
abstract class JobsDatabase : RoomDatabase() {


}