package com.zarisa.dictionaryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.zarisa.dictionaryapp.data_base.Word
import com.zarisa.dictionaryapp.databinding.FragmentAddWordBinding
import com.zarisa.dictionaryapp.model.MainViewModel
import java.util.*

class AddWordFragment : Fragment() {

    private lateinit var binding: FragmentAddWordBinding
    val viewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            saveWordInDataBase()
        }
    }

    private fun saveWordInDataBase() {
        if (dataIsValid()) {
            var userWord = Word(
                binding.EditTextEnglishWord.text.toString().lowercase(Locale.getDefault()),
                binding.EditTextPersianWord.text.toString().lowercase(Locale.getDefault()),
                binding.EditTextExample.text.toString(),
                binding.EditTextSynonym.text.toString()
            )
            viewModel.addWord(userWord)
            Toast.makeText(
                requireContext(),
                "New word added.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Please Enter at least English and Persian word.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun dataIsValid(): Boolean {
        var isValid = true
        binding.EditTextEnglishWord.let {
            if (it.text.isNullOrBlank()) {
                it.error = "Enter the word!"
                isValid = false
            }
        }
        binding.EditTextPersianWord.let {
            if (it.text.isNullOrBlank()) {
                it.error = "Enter the word!"
                isValid = false
            }
        }
        return isValid
    }
}