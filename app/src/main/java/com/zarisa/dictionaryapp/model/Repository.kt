package com.zarisa.dictionaryapp.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.zarisa.dictionaryapp.data_base.AppDatabase
import com.zarisa.dictionaryapp.data_base.Word
import com.zarisa.dictionaryapp.data_base.WordDao

object Repository {
    lateinit var wordDao  : WordDao
    fun initDB(context : Context){
        wordDao = AppDatabase.getAppDataBase(context).wordDao()
    }
    fun getCountOfWords():LiveData<Int>{
        return wordDao.getCountOfWords()
    }
    fun getWord(word:String):Word?{
        return wordDao.getWord(word)
    }
    fun addWord(word: Word){
        wordDao.insertOrUpdate(word)
    }
    fun delete(word:Word){
        wordDao.deleteWord(word)
    }
}