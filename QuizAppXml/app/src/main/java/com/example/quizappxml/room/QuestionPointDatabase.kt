package com.example.quizappxml.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.quizappxml.model.QuestionPoint

@Database(entities = [QuestionPoint::class], version = 1, exportSchema = false)
abstract class QuestionPointDatabase: RoomDatabase() {
    abstract fun questionPointDao() :QuestionPointDao

    companion object {
        @Volatile
        private var INSTANCE: QuestionPointDatabase? = null

        fun getDatabase(context: Context): QuestionPointDatabase {
            var tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuestionPointDatabase::class.java,
                    "questionPoints"
                )
                    .fallbackToDestructiveMigration() // Yıkıcı migrasyonu ekleyin
                    .build()
                tempInstance = instance
                return instance
            }
        }


    }
}