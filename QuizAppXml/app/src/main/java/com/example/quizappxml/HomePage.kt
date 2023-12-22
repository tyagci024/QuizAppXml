package com.example.quizappxml

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quizappxml.databinding.FragmentHomePageBinding
import com.example.quizappxml.room.QuestionDatabase
import com.example.quizappxml.room.QuestionPointDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Random

class HomePage : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private val quizViewModel: QuizPageViewModel by viewModels()
    private val PREF_NAME = "QuizAppPreferences"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val distinctCorrectQuestions = loadDistinctCorrectQuestionsFromSharedPreferences()

        val userName = sharedPreferences.getString("user_name", "Quizer One")
        binding.quizerName.setText(userName)

        binding.accountEdit.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_editProfile)
        }

        val db = QuestionDatabase.getDatabase(requireContext())
        val questionDao = db.questionDao()

        runBlocking {
            var wrongQueList =questionDao.getQuestionsByCategory("answeredquestion")
            binding.totalAnswer.text=wrongQueList.size.toString()
        }








        val correctQuestionsPoint = distinctCorrectQuestions.size*3
        binding.textView2.text = correctQuestionsPoint.toString()


        val cardCategories = mapOf(
            binding.cardHistory to "history",
            binding.cardScience to "science",
            binding.cardSpace to "space",
            binding.cardBody to "body",
            binding.cardSpor to "spor",
            binding.cardRave to "music",
            binding.cardKotlin to "android_kotlin",
            binding.cardGeography to "coğrafya",
            binding.cardTechnology to "technology"



        )

        cardCategories.forEach { (cardView, category) ->
            cardView.setOnClickListener {
                val action = HomePageDirections.actionHomePageToQuestionPage(category)
                findNavController().navigate(action)
            }
        }

        binding.button.setOnClickListener {
            // Rastgele bir kategori seç
            val random = Random()
            val randomIndex = random.nextInt(cardCategories.size)
            val randomCategory = cardCategories.values.toList()[randomIndex]

            // Seçilen rastgele kategoriye git
            val action = HomePageDirections.actionHomePageToQuestionPage(randomCategory)
            findNavController().navigate(action)
        }


        return binding.root
    }



    private fun loadDistinctCorrectQuestionsFromSharedPreferences(): List<String> {
        val sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val correctQuestionsJson = sharedPreferences.getString("correctQuestionsKey", null)

        return if (correctQuestionsJson != null) {
            val correctQuestionsList = Gson().fromJson<List<String>>(
                correctQuestionsJson,
                object : TypeToken<List<String>>() {}.type
            )
            correctQuestionsList.distinct()
        } else {
            emptyList()
        }
    }

}