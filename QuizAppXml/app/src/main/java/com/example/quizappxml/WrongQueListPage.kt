package com.example.quizappxml

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappxml.databinding.FragmentEditProfileBinding
import com.example.quizappxml.databinding.FragmentWrongQueListPageBinding
import com.example.quizappxml.model.Question
import com.example.quizappxml.room.QuestionDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking


class WrongQueListPage : Fragment() {
    private lateinit var binding: FragmentWrongQueListPageBinding
    private lateinit var wrongQuestionAdapter: WrongQuestionAdapter

    private lateinit var distinctWrongQuestions : List<Question>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentWrongQueListPageBinding.inflate(inflater,container,false)

        // requireContext() burada güvenli bir şekilde kullanılabilir
        val db = QuestionDatabase.getDatabase(requireContext())
        val questionDao = db.questionDao()

        runBlocking {
            distinctWrongQuestions = questionDao.getQuestionsByCategory("wrongQuestion")
        }
        wrongQuestionAdapter = WrongQuestionAdapter(distinctWrongQuestions)
        binding.wronQueRecyclerView.adapter = wrongQuestionAdapter
        binding.wronQueRecyclerView.layoutManager = LinearLayoutManager(context)

        setHasOptionsMenu(true)



        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_bar,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.deleteall){
            runBlocking {
                val db = QuestionDatabase.getDatabase(requireContext())
                val questionDao = db.questionDao()

                questionDao.clearTable("wrongQuestion")
                wrongQuestionAdapter.updateData(emptyList())


            }

            return true

        }
        return super.onOptionsItemSelected(item)
    }








}