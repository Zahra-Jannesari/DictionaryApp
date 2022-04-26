package com.zarisa.dictionaryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zarisa.dictionaryapp.adapters.WordListAdapter
import com.zarisa.dictionaryapp.databinding.FragmentMainBinding
import com.zarisa.dictionaryapp.model.MainViewModel
import java.util.*

const val EDIT = "addOrEdit"
const val WORDID = "wordId"

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val wordListAdapter = WordListAdapter {
        goToWordDetailFragment(it)
    }
    private val viewModel: MainViewModel by viewModels()
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
        viewModel.wordCounterLiveData.observe(viewLifecycleOwner) {
            binding.textViewWordsCounter.text = it?.toString() ?: "0"
        }
        binding.recyclerview.adapter = wordListAdapter
        wordListAdapter.setDate(viewModel.getWordsList())
        onClicks()
    }

    private fun goToWordDetailFragment(wordId: Int) {
        val bundle = bundleOf(
            WORDID to wordId
        )
        findNavController().navigate(R.id.action_mainFragment_to_wordDetailFragment, bundle)
    }

    private fun onClicks() {
        binding.textFieldSearch.setEndIconOnClickListener {
            val searchedWord = viewModel.getWord(
                binding.editTextSearch.text.toString().lowercase(Locale.getDefault())
            )
            if (searchedWord != null) {
                goToWordDetailFragment(searchedWord.wordId)
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(
                        "The word has not registered before.\nTry adding the word with button `+` below."
                    )
                    .setTitle("Word not found!")
                    .setPositiveButton("got it!") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
        binding.fabAdd.setOnClickListener {
            val bundle = bundleOf(
                EDIT to false
            )
            findNavController().navigate(R.id.action_mainFragment_to_addWordFragment, bundle)
        }
    }
}