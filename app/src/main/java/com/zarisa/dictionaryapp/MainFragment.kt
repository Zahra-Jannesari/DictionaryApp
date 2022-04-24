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
import com.zarisa.dictionaryapp.adapters.WordListAdapter
import com.zarisa.dictionaryapp.databinding.FragmentMainBinding
import com.zarisa.dictionaryapp.model.MainViewModel
import java.util.*


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    val wordListAdapter=WordListAdapter()
    val viewModel:MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        LayoutInflater.from(requireContext())
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.wordCounterLiveData.observe(viewLifecycleOwner){
            binding.textViewWordsCounter.text= it?.toString() ?: "0"
        }
        binding.recyclerview.adapter=wordListAdapter
        wordListAdapter.setDate(viewModel.getWordsList())
        onClicks()
    }

    private fun onClicks() {
        binding.textFieldSearch.setEndIconOnClickListener {
            val searchedWord = binding.editTextSearch.text.toString().lowercase(Locale.getDefault())
            if (viewModel.getWord(searchedWord) != null) {
                var bundle= bundleOf("word" to searchedWord)
                findNavController().navigate(R.id.action_mainFragment_to_wordDetailFragment,bundle)
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