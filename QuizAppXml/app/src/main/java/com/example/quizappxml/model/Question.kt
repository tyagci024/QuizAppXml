package com.example.quizappxml.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "question_table")
data class Question(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val text: String,
    val options: List<String>,
    var category: String? = null,
    val correctAnswerIndex: Int,
    var lastFetchedAt: Long


)