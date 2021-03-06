package com.zarisa.dictionaryapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.zarisa.dictionaryapp.data_base.Word

class MainViewModel(app: Application) : AndroidViewModel(app) {
    lateinit var wordCounterLiveData: LiveData<Int>
    init {
        Repository.initDB(app.applicationContext)
        updateWordCounter()
    }
    fun getWord(word: String): Word? {
        return Repository.getWord(word)
    }
    fun updateWordCounter(){
        wordCounterLiveData=Repository.getCountOfWords()
    }
    fun addWord(word: Word){
        Repository.addWord(word)
    }
    fun deleteWord(word:Word){
        Repository.delete(word)
    }
    fun getWordsList():List<Word>{
        return Repository.getWordsList()
    }
    fun updateWord(word:Word){
        Repository.updateWord(word)
    }

    fun getWordListSize(): Int {
        return Repository.getWordListSize()
    }
    fun getWordById(wordID:Int):Word{
        return Repository.getWordById(wordID)
    }
}