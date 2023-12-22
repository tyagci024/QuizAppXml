package com.example.quizappxml.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.quizappxml.model.QuestionPoint

@Dao
interface QuestionPointDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCorrectQuestion(question: QuestionPoint)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQ(question: QuestionPoint)

    @Query("DELETE FROM pointquestion_table")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM pointquestion_table ")
    suspend fun getQuestionsTexts(): List<QuestionPoint>

}