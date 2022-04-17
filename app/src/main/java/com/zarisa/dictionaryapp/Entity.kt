package com.zarisa.dictionaryapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey var englishWord: String,
    var persianWord: String,
    var example: String,
    var synonym: String
)