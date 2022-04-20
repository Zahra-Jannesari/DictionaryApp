package com.zarisa.dictionaryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.dictionaryapp.data_base.Word
import com.zarisa.dictionaryapp.databinding.FragmentWordDetailBinding
import com.zarisa.dictionaryapp.model.MainViewModel
import java.util.*


class WordDetailFragment : Fragment() {
    lateinit var binding: FragmentWordDetailBinding
    val viewModel: MainViewModel by viewModels()
    lateinit var searchedWord: Word
    var editTime = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWord(requireArguments().getString("word", ""))?.let {
            searchedWord = it
        }
        putDataInEditTexts()
        onClicks()
    }

    private fun onClicks() {
        binding.btnEdit.setOnClickListener {
            if (!editTime) {
                changeEnable(true)
                editTime = true
                Toast.makeText(requireContext(), "Start editing.", Toast.LENGTH_SHORT).show()
            } else {
                changeEnable(false)
                editTime = false
                val editedWord = Word(
                    binding.EditTextEnglishWord.text.toString().lowercase(Locale.getDefault()),
                    binding.EditTextPersianWord.text.toString().lowercase(Locale.getDefault()),
                    binding.EditTextExample.text.toString(),
                    binding.EditTextSynonym.text.toString()
                )
                viewModel.addWord(editedWord)
                Toast.makeText(
                    requireContext(),
                    "Word updated.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.btnDelete.setOnClickListener {
            viewModel.deleteWord(searchedWord)
            Toast.makeText(requireContext(), "Word deleted!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_wordDetailFragment_to_mainFragment)
        }
    }

    private fun changeEnable(requireState: Boolean) {
        binding.EditTextEnglishWord.isEnabled = requireState
        binding.EditTextPersianWord.isEnabled = requireState
        binding.EditTextExample.isEnabled = requireState
        binding.EditTextSynonym.isEnabled = requireState
    }

    private fun putDataInEditTexts() {
        binding.EditTextEnglishWord.setText(searchedWord.englishWord)
        binding.EditTextPersianWord.setText(searchedWord.persianWord)
        binding.EditTextExample.setText(searchedWord.example)
        binding.EditTextSynonym.setText(searchedWord.synonym)
    }
}