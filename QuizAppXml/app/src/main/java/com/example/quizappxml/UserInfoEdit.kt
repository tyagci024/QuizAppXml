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
import com.example.quizappxml.databinding.FragmentUserInfoEditBinding


class UserInfoEdit : Fragment() {
    private lateinit var binding: FragmentUserInfoEditBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoEditBinding.inflate(inflater,container,false)

        val userName = sharedPreferences.getString("user_name", "Quizer One")

        // EditText'e kullanıcı adını set et
        binding.editTextName.setText(userName)


        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_button, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            val userName = binding.editTextName.text.toString()
            saveUserNameToSharedPreferences(userName)
            findNavController().navigate(R.id.action_userInfoEdit_to_homePage)
            return true

        }
        return super.onOptionsItemSelected(item)
    }


    private fun saveUserNameToSharedPreferences(userName: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_name", userName)
        editor.apply()
    }
}