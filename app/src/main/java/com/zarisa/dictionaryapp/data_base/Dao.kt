package com.zarisa.dictionaryapp.data_base

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zarisa.dictionaryapp.model.Repository

@Dao
interface WordDao{
    @Query("SELECT * FROM Word WHERE englishWord in (:word) or persianWord in (:word)")
    fun getWord(word:String):Word?
    @Query("SELECT count(*) FROM Word")
    fun getCountOfWords():LiveData<Int>
    @Query("SELECT * From Word")
    fun getWordsList():List<Word>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(word:Word)
//    @Query("REPLACE Into Word with ")
//    fun updateWord(oldWord:Word,editedWord:Word)
    @Delete
    fun deleteWord(word:Word)
}