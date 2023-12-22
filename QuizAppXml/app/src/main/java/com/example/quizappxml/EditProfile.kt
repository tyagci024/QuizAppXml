package com.example.quizappxml

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.quizappxml.databinding.FragmentEditProfileBinding
import com.example.quizappxml.databinding.FragmentHomePageBinding
import com.example.quizappxml.room.QuestionDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking


class EditProfile : Fragment() {
    private val PREF_NAME = "QuizAppPreferences"
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var sharedPreferences: SharedPreferences





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        val distinctCorrectQuestions = loadDistinctCorrectQuestionsFromSharedPreferences()

        val correctQuestionsPoint = distinctCorrectQuestions.size * 3

        binding.coinPoint.text = correctQuestionsPoint.toString()

        val userName = sharedPreferences.getString("user_name", "Quizer One")

        // EditText'e kullanıcı adını set et
        binding.textName.setText(userName)

        binding.RightQuestion.setText((distinctCorrectQuestions.size).toString())



        binding.cardView2.setOnClickListener {
            findNavController().navigate(R.id.action_editProfile_to_wrongQueListPage)
        }

    setHasOptionsMenu(true)

        return binding.root
    }

    private fun loadDistinctCorrectQuestionsFromSharedPreferences(): List<String> {
        val sharedPreferences =
            requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit_page) {
            findNavController().navigate(R.id.action_editProfile_to_userInfoEdit)
            return true

        }
        return super.onOptionsItemSelected(item)
    }




}