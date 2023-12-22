package com.example.quizappxml.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pointquestion_table")

data class QuestionPoint(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String)
