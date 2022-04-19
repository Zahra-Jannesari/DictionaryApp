package com.zarisa.dictionaryapp.data_base

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao{
    @Query("SELECT * FROM Word WHERE englishWord in (:word) or persianWord in (:word)")
    fun getWord(word:String):Word?
//    @Query("SELECT * FROM Word WHERE persianWord in (:word)")
//    fun getPersianWord(word:String):Word
    @Query("SELECT count(*) FROM Word")
    fun getCountOfWords():LiveData<Int>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(word:Word)
    @Update
    fun updateWord(word:Word)
    @Delete
    fun deleteWord(word:Word)
}