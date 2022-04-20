package com.zarisa.dictionaryapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zarisa.dictionaryapp.data_base.Word

class MainViewModel(app: Application) : AndroidViewModel(app) {
    val wordCounterLiveData=MutableLiveData<Int>(0)
    lateinit var searchedWord:Word
    init {
        Repository.initDB(app.applicationContext)
        updateWordCounter()
    }
    fun getWord(word: String): Word? {
        return Repository.getWord(word)
    }
    fun updateWordCounter(){
        wordCounterLiveData.value=Repository.getCountOfWords().value
    }
    fun addWord(word: Word){
        Repository.addWord(word)
    }
}