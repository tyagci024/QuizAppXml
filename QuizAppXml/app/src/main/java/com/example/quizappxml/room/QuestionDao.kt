package com.example.quizappxml.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quizappxml.model.Question

@Dao
interface QuestionDao {

    @Insert
    suspend fun insertQuestions(questions: List<Question>)

    @Insert

    suspend fun insertQ(question: Question)

    @Query("DELETE FROM question_table")
    suspend fun deleteAllQuestions()

    @Query("DELETE FROM question_table WHERE category = :categoryType")
    suspend fun clearTable(categoryType: String)




    @Query("SELECT * FROM question_table WHERE category = :category")
    suspend fun getQuestionsByCategory(category: String): List<Question>
}