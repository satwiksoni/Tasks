package com.example.tasks_to_do

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TodoModel::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun todoDao(): Dao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDB? = null

        fun getDatabase(context: Context): AppDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDB::class.java,
                        DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}