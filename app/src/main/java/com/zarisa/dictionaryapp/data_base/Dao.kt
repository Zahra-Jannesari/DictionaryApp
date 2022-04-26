package com.zarisa.dictionaryapp.data_base

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zarisa.dictionaryapp.model.Repository

@Dao
interface WordDao{
    @Query("SELECT * FROM Word WHERE englishWord in (:word) or persianWord in (:word) LIMIT 1")
    fun getWord(word:String):Word?
    @Query("SELECT * FROM Word WHERE wordId in (:wordID)")
    fun getWordById(wordID:Int):Word
    @Query("SELECT count(*) FROM Word")
    fun getCountOfWords():LiveData<Int>
    @Query("SELECT count(*) FROM Word")
    fun wordListSize():Int
    @Query("SELECT * From Word")
    fun getWordsList():List<Word>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word:Word)
    @Update
    fun updateWord(word:Word)
    @Delete
    fun deleteWord(word:Word)
}