package com.example.quizappxml

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappxml.model.Question

class WrongQuestionAdapter(private var wrongQuestions: List<Question>) : RecyclerView.Adapter<WrongQuestionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionTextView: TextView = view.findViewById(R.id.question)
        val answerText :TextView=view.findViewById(R.id.answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wrongQuestion = wrongQuestions[position]
        holder.questionTextView.text = wrongQuestion.text
        holder.answerText.text=wrongQuestion.options[wrongQuestion.correctAnswerIndex]
    }

    override fun getItemCount(): Int {
        return wrongQuestions.size
    }

    fun updateData(newData: List<Question>) {
        wrongQuestions = newData
        notifyDataSetChanged()
    }
}