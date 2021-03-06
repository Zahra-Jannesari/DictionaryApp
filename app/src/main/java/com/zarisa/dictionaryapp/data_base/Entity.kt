package com.zarisa.dictionaryapp.data_base

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey(autoGenerate = true) val wordId:Int,
    var englishWord: String,
    var persianWord: String,
    var example: String,
    var synonym: String,
    var wikiLink: String,
    var pronunciation:String,
    var isFavorite:Boolean=false
)