package com.example.quizappxml

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quizappxml.databinding.FragmentQuestionPageBinding
import com.example.quizappxml.model.Question
import com.example.quizappxml.model.QuestionPoint
import com.example.quizappxml.room.QuestionDatabase
import com.example.quizappxml.room.QuestionPointDatabase
import com.example.quizappxml.service.QuizApiService
import com.example.quizappxml.utill.IdGenerator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import kotlin.math.log


class QuestionPage : Fragment() {
    private lateinit var binding: FragmentQuestionPageBinding
    private lateinit var questions: List<Question>
    private lateinit var cardViews: List<CardView>
    private val quizViewModel: QuizPageViewModel by viewModels()
    private val SORU_GECERLILIK_SURESI = 24 * 60 * 60 * 1000 // 24 saat milisaniye cinsinden
    private lateinit var correctQuestions: MutableList<String>



    private val PREF_NAME = "QuizAppPreferences"
    private val WRONG_QUESTIONS_KEY = "wrongQuestionsKey"
    private val SORU_SAYAC_KEY = "soruSayacKey"

    private var currentQuestionIndex = 0
    private var selectedOptionIndex = -1
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        correctQuestions = mutableListOf()


        val sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val correctQuestionsJson = sharedPreferences.getString("correctQuestionsKey", null)


        if (correctQuestionsJson != null) {
            correctQuestions = Gson().fromJson(correctQuestionsJson, object : TypeToken<List<String>>() {}.type)

    }

          val db = QuestionDatabase.getDatabase(requireContext())
        val questionDao = db.questionDao()

        val quizApiService = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(QuizApiService::class.java)


        val categoryType = arguments?.getString("categoryType")

        var soruSayac = sharedPreferences.getInt(SORU_SAYAC_KEY, 0)

        runBlocking {
            when (categoryType) {
                "science", "history", "space", "body","spor","music","coğrafya","android_kotlin","technology" -> {
                    val currentTimestamp = System.currentTimeMillis()

                    val cachedQuestions = questionDao.getQuestionsByCategory(categoryType)
                    if (cachedQuestions.isNotEmpty() && currentTimestamp - cachedQuestions[0].lastFetchedAt < SORU_GECERLILIK_SURESI) {
                        Toast.makeText(requireContext(), "questions from room", Toast.LENGTH_SHORT).show()
                        questions = cachedQuestions
                    } else {
                        Toast.makeText(requireContext(), "questions from api", Toast.LENGTH_SHORT).show()
                        questions = when (categoryType) {
                            "science" -> quizApiService.getScienceQuestions().drop(soruSayac ).take(10)
                            "history" -> quizApiService.getHistoryQuestions().drop(soruSayac).take(10)
                            "space" -> quizApiService.getSpaceQuestions().drop(soruSayac).take(10)
                            "body" -> quizApiService.getBodyQuestions().drop(soruSayac).take(10)
                            "spor" -> quizApiService.getSportsQuestions().drop(soruSayac).take(10)
                            "music" -> quizApiService.getRaveQuestions().drop(soruSayac).take(10)
                            "technology" -> quizApiService.getTechnologyQuestions().drop(soruSayac).take(10)
                            "coğrafya" -> quizApiService.getGeographyQuestions().drop(soruSayac).take(10)
                            "android_kotlin" -> quizApiService.getKotlinQuestions().drop(soruSayac).take(10)

                            else -> emptyList()
                        }
                        soruSayac += 10
                        sharedPreferences.edit().putInt(SORU_SAYAC_KEY, soruSayac).apply()

                        questionDao.clearTable(categoryType)
                        questions.forEach { question ->
                            question.lastFetchedAt = currentTimestamp
                            questionDao.insertQ(question)
                            Log.d("QuestionPage", "Soru Room veritabanına eklendi: $question")

                        }

                    }


                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionPageBinding.inflate(inflater, container, false)
        showQuestion()
        setSelectedCardBG()

        binding.nextButton.setOnClickListener {
            // Kullanıcı bir seçenek seçmiş mi kontrol et
            if (selectedOptionIndex == -1) {
                Toast.makeText(requireContext(), "Lütfen bir seçenek seçin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            checkAnswerAndUpdateScore()

            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                showQuestion()
                enableCardClicks()
                setSelectedCardBG()
            } else {
                showScore()
                setSelectedCardBG()
                findNavController().navigate(R.id.action_questionPage_to_homePage)
            }
        }
        return binding.root
    }

    private fun showScore() {
        val scoreText = "Toplam Puan: $score"
        Toast.makeText(requireContext(), scoreText, Toast.LENGTH_SHORT).show()
        saveCorrectQuestionsToSharedPreferences(correctQuestions)
    }

    private fun showQuestion() {
        val question = questions[currentQuestionIndex]
        binding.questionText.text = question.text

        binding.textOptionOne.text = question.options[0]
        binding.textOptionTwo.text = question.options[1]
        binding.textOptionThree.text = question.options[2]
        binding.textOptionFour.text = question.options[3]
    }

    private fun setSelectedCardBG() {
        cardViews = listOf(
            binding.cardOptionOne,
            binding.cardOptionTwo,
            binding.cardOptionThree,
            binding.cardOptionFour
        )

        cardViews.forEachIndexed { index, cardView ->
            cardView.setOnClickListener {
                selectedOptionIndex = index

                val correctAnswerIndexTest = questions[currentQuestionIndex].correctAnswerIndex
                val isCorrect = selectedOptionIndex == correctAnswerIndexTest

                val backgroundColor = if (isCorrect) R.color.correct else R.color.wrong

                cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), backgroundColor))

                cardViews.filter { it != cardView }
                    .forEach { otherCardView ->
                        otherCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.default_option_color
                            )
                        )
                    }
                cardViews[correctAnswerIndexTest].setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.correct
                    )
                )
                disableCardClicks()
            }
        }
        cardViews.forEach {
            it.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_option_color))
        }
    }

    private fun checkAnswerAndUpdateScore() {
        if (selectedOptionIndex != -1) {
            val correctAnswerIndex = questions[currentQuestionIndex].correctAnswerIndex
            if (selectedOptionIndex == correctAnswerIndex) {
                score++
                correctQuestions.add(questions[currentQuestionIndex].text)

                val answeredQuestion = questions[currentQuestionIndex].copy().apply {
                    id = IdGenerator.generateUniqueId()
                    category = "answeredquestion"
                }
                val db = QuestionDatabase.getDatabase(requireContext())
                val questionDao = db.questionDao()

                runBlocking {
                    questionDao.insertQ(answeredQuestion)
                }

            } else {
                val db = QuestionDatabase.getDatabase(requireContext())
                val questionDao = db.questionDao()

                val wrongQuestion = questions[currentQuestionIndex]

                    runBlocking {
                        var wrongQueList =questionDao.getQuestionsByCategory("wrongQuestion")
                        val wrongQuestionsTextList: List<String> = wrongQueList.map { it.text }

                        if (!wrongQuestionsTextList.contains(wrongQuestion.text)) {
                            // Yanlış soruyu room veritabanına ekle
                            saveWrongQuestionToRoomDatabase(wrongQuestion)

                        }
                    }

                val answeredQuestion = questions[currentQuestionIndex].copy().apply {
                    id = IdGenerator.generateUniqueId()
                    category = "answeredquestion"
                }

                runBlocking {
                    questionDao.insertQ(answeredQuestion)
                }









            }

            selectedOptionIndex = -1
        }
    }

    private fun disableCardClicks() {
        cardViews.forEach { it.isClickable = false }
    }

    private fun enableCardClicks() {
        cardViews.forEach { it.isClickable = true }
    }

    private fun saveCorrectQuestionsToSharedPreferences(correctQuestions: List<String>) {
        val sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Doğru soruların metin listesini JSON formatına çevir ve SharedPreferences'a kaydet
        val correctQuestionsJson = Gson().toJson(correctQuestions)
        editor.putString("correctQuestionsKey", correctQuestionsJson)

        editor.apply()
    }

    private fun saveWrongQuestionToRoomDatabase(wrongQuestion: Question) {
        wrongQuestion.category = "wrongQuestion" // Yanlış sorunun kategorisini belirle
        wrongQuestion.id = IdGenerator.generateUniqueId()

        val db = QuestionDatabase.getDatabase(requireContext())
        val questionDao = db.questionDao()

        // Yanlış soruyu room veritabanına ekle
        runBlocking {
            withContext(Dispatchers.IO) {
                questionDao.insertQ(wrongQuestion)
            }
        }
    }







}
