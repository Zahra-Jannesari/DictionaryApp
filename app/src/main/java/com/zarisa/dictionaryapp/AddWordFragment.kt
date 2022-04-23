package com.zarisa.dictionaryapp

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.zarisa.dictionaryapp.data_base.Word
import com.zarisa.dictionaryapp.databinding.FragmentAddWordBinding
import com.zarisa.dictionaryapp.model.MainViewModel
import java.io.IOException
import java.util.*

const val LOG_TAG = "AudioRecordTest"

class AddWordFragment : Fragment() {
    private lateinit var binding: FragmentAddWordBinding
    val viewModel: MainViewModel by viewModels()
    var isAudioRecording = false
    var recordPermissionGranted = false
    var fileName = ""
    var didPronounceRecorde = false
    private var recorder: MediaRecorder? = null
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    requireContext(),
                    "Permission granted.",
                    Toast.LENGTH_SHORT
                ).show()
                recordPermissionGranted = true
                startRecording()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fileName = "${activity?.externalCacheDir?.absolutePath}/${Calendar.getInstance().time}.3gp"
        onClicks()
    }

    private fun onClicks() {
        binding.fieldAudio.setEndIconOnClickListener {
            requestPermissions()
            if (!isAudioRecording && recordPermissionGranted) {
                startRecording()
            } else if (isAudioRecording) {
                stopRecording()
            }
        }
        binding.btnSave.setOnClickListener {
            saveWordInDataBase()
        }
    }

    private fun requestPermissions() {
        when {
            //if user already granted the permission
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                recordPermissionGranted = true
            }
            //if user already denied the permission once
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) -> {
                showRationDialog()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun showRationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage("We need to use microphone to record pronunciation for this word.")
            setTitle("permission required")
            setPositiveButton("ok") { _, _ ->
                requestPermissionLauncher.launch(
                    Manifest.permission.RECORD_AUDIO,
                )
            }
        }
        builder.create().show()
    }

    private fun stopRecording() {
        binding.fieldAudio.endIconDrawable =
            resources.getDrawable(R.drawable.ic_baseline_mic_none_24)
        isAudioRecording = false
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    private fun startRecording() {
        didPronounceRecorde = true
        binding.fieldAudio.endIconDrawable =
            resources.getDrawable(R.drawable.ic_baseline_mic_24)
        isAudioRecording = true
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
    }

    private fun saveWordInDataBase() {
        if (dataIsValid()) {
            var userWord = Word(
                binding.EditTextEnglishWord.text.toString().lowercase(Locale.getDefault()),
                binding.EditTextPersianWord.text.toString().lowercase(Locale.getDefault()),
                binding.EditTextExample.text.toString(),
                binding.EditTextSynonym.text.toString(),
                binding.EditTextWikiLink.text.toString(),
                if (didPronounceRecorde) fileName else ""
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