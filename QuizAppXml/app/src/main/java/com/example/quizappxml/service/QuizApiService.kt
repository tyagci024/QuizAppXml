package com.example.quizappxml.service

import com.example.quizappxml.model.Question
import retrofit2.http.GET

interface QuizApiService {

    @GET("tyagci024/pharmacyapi/main/science.json")
    suspend fun getScienceQuestions(): List<Question>

    @GET("tyagci024/pharmacyapi/main/history.json")
    suspend fun getHistoryQuestions(): List<Question>

    @GET("tyagci024/pharmacyapi/main/body.json")
    suspend fun getBodyQuestions(): List<Question>

    @GET("tyagci024/pharmacyapi/main/space.json")
    suspend fun getSpaceQuestions(): List<Question>

    @GET("tyagci024/pharmacyapi/main/technology.json")
    suspend fun getTechnologyQuestions(): List<Question>

    @GET("tyagci024/pharmacyapi/main/spor.json")
    suspend fun getSportsQuestions(): List<Question>

    @GET("tyagci024/pharmacyapi/main/rave.json")
    suspend fun getRaveQuestions(): List<Question>

    @GET("tyagci024/pharmacyapi/main/geography.json")
    suspend fun getGeographyQuestions(): List<Question>

    @GET("tyagci024/pharmacyapi/main/kotlin.json")
    suspend fun getKotlinQuestions(): List<Question>



}