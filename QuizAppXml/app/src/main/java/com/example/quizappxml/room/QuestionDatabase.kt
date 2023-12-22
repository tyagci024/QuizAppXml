package com.example.quizappxml.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quizappxml.Converters
import com.example.quizappxml.model.Question

@Database(entities = [Question::class], version = 11, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuestionDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: QuestionDatabase? = null

        fun getDatabase(context: Context): QuestionDatabase {
            var tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val migration_10_11 = object : Migration(10, 11) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        // Daha önceki versiyondan yeni versiyona geçiş için gerekli SQL sorgularını ekleyin
                    }
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuestionDatabase::class.java,
                    "questions"
                )
                    .addMigrations(migration_10_11) // Eklenen migration kodu
                    .fallbackToDestructiveMigration() // Yıkıcı migrasyonu ekleyin
                    .build()

                // onCreate ve onOpen metotlarını ekleyin


                instance.openHelper.writableDatabase
                tempInstance = instance
                return instance
            }
        }
    }
}