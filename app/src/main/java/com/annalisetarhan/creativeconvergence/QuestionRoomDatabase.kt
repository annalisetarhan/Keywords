package com.annalisetarhan.creativeconvergence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Question::class], version = 1, exportSchema = false)
abstract class QuestionRoomDatabase : RoomDatabase() {
    abstract fun questionDao() : QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: QuestionRoomDatabase? = null

        fun getDatabase(
            context: Context,
            ) : QuestionRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuestionRoomDatabase::class.java,
                    "questionRoomDatabase"
                )
                        .createFromAsset("database/RemoteAssociates.db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}