package com.zarisa.dictionaryapp

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.dictionaryapp.data_base.Word
import com.zarisa.dictionaryapp.databinding.FragmentWordDetailBinding
import com.zarisa.dictionaryapp.model.MainViewModel
import java.lang.Exception


class WordDetailFragment : Fragment() {
    private lateinit var binding: FragmentWordDetailBinding
    private val viewModel: MainViewModel by viewModels()
    lateinit var searchedWord: Word
    private var mediaPlayer: MediaPlayer? = null
    var url: String? = ""
    var readMoreIsOpen = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariables()
        putDataInEditTexts()
        onClicks()
    }

    private fun initVariables() {
        searchedWord = viewModel.getWordById(requireArguments().getInt(WORDID, 0))
        url = searchedWord.pronunciation // your URL here
        if (!url.isNullOrBlank()) {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
            }
        }
        binding.buttonFavorite.isSelected=searchedWord.isFavorite
    }

    private fun onClicks() {
        binding.btnEdit.setOnClickListener {
            val bundle = bundleOf(
                EDIT to true,
                WORDID to searchedWord.wordId
            )
            findNavController().navigate(R.id.action_wordDetailFragment_to_addWordFragment, bundle)
        }
        binding.btnDelete.setOnClickListener {
            viewModel.deleteWord(searchedWord)
            Toast.makeText(requireContext(), "Word deleted!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_wordDetailFragment_to_mainFragment)
        }
        binding.buttonPlay.setOnClickListener {
            if (mediaPlayer?.isPlaying == true)
                pauseAudio()
            else
                playAudio()
        }
        mediaPlayer?.setOnCompletionListener {
            binding.buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
        binding.btnReadMore.setOnClickListener {
            linkProcess()
        }
        binding.buttonFavorite.setOnClickListener {
            if ((it as ImageButton).isSelected)
                it.setSelected(false)
            else
                it.setSelected(true)
            var updateWord = Word(
                searchedWord.wordId,
                searchedWord.englishWord,
                searchedWord.persianWord,
                searchedWord.example,
                searchedWord.synonym,
                searchedWord.wikiLink,
                searchedWord.pronunciation,
                it.isSelected
            )
            viewModel.updateWord(updateWord)
        }
    }

    private fun linkProcess() {
        if (!readMoreIsOpen) {
            searchedWord.wikiLink.let {
                if (it.isBlank())
                    Toast.makeText(
                        requireContext(),
                        "There is no more details.",
                        Toast.LENGTH_SHORT
                    ).show()
                else {
                    binding.webView.loadUrl(it)
                    binding.btnReadMore.apply {
                        this.text = "read less"
                        this.icon =
                            resources.getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    }
                    binding.webView.visibility = View.VISIBLE
                    readMoreIsOpen = true
                }
            }
        } else if (readMoreIsOpen) {
            binding.btnReadMore.apply {
                this.text = "read more"
                this.icon = resources.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
            binding.webView.visibility = View.GONE
            readMoreIsOpen = false
        }
    }

    private fun pauseAudio() {
        binding.buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        mediaPlayer?.pause()
    }

    private fun playAudio() {
        if (mediaPlayer == null) {
            Toast.makeText(
                requireContext(),
                "There is no recorded pronunciation for this word.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            try {
                mediaPlayer?.apply {
                    prepare()
                    start()
                }
                binding.buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)
                mediaPlayer?.duration?.let{binding.progressBarAudio.max=it}
                mediaPlayer?.currentPosition?.let{binding.progressBarAudio.progress=it}
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "playing failed!",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }

    private fun putDataInEditTexts() {
        binding.EditTextEnglishWord.setText(searchedWord.englishWord)
        binding.EditTextPersianWord.setText(searchedWord.persianWord)
        binding.EditTextExample.setText(searchedWord.example)
        binding.EditTextSynonym.setText(searchedWord.synonym)
    }
}


//previous edit feature
//if (!editTime) {
//    changeEnable(true)
//    editTime = true
//    Toast.makeText(requireContext(), "Start editing.", Toast.LENGTH_SHORT).show()
//} else {
//    changeEnable(false)
//    editTime = false
//    val editedWord = Word(
//        binding.EditTextEnglishWord.text.toString().lowercase(Locale.getDefault()),
//        binding.EditTextPersianWord.text.toString().lowercase(Locale.getDefault()),
//        binding.EditTextExample.text.toString(),
//        binding.EditTextSynonym.text.toString(),
//        binding.EditTextExample.text.toString(),
//        binding.EditTextSynonym.text.toString()
//    )
//    viewModel.addWord(editedWord)
//    Toast.makeText(
//        requireContext(),
//        "Word updated.",
//        Toast.LENGTH_SHORT
//    ).show()
//}

//private fun changeEnable(requireState: Boolean) {
//    binding.EditTextEnglishWord.isEnabled = requireState
//    binding.EditTextPersianWord.isEnabled = requireState
//    binding.EditTextExample.isEnabled = requireState
//    binding.EditTextSynonym.isEnabled = requireState
//}