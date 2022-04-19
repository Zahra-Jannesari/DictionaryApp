package com.zarisa.dictionaryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zarisa.dictionaryapp.databinding.FragmentMainBinding
import com.zarisa.dictionaryapp.model.MainViewModel


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    val viewModel:MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.wordCounterLiveData.observe(viewLifecycleOwner){
            binding.textViewWordsCounter.text= it?.toString() ?: "0"
        }
        onClicks()
    }

    private fun onClicks() {
        binding.textFieldSearch.setEndIconOnClickListener {
            val searchedWord = viewModel.getWord(binding.editTextSearch.text.toString())
            if (searchedWord != null) {
                var word = bundleOf("word" to searchedWord)
                findNavController().navigate(R.id.action_wordDetailFragment_to_editWordFragment, word)
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage("The word you are looking for has not registered before." +
                            "\nTry adding the word with button `+` below.")
                    .setTitle("Word not found!")
                    .setPositiveButton("got it!") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addWordFragment)
        }
    }
}