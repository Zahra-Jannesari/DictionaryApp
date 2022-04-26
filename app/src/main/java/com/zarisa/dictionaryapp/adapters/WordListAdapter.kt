package com.zarisa.dictionaryapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zarisa.dictionaryapp.data_base.Word
import com.zarisa.dictionaryapp.databinding.WordListItemRowBinding
typealias goToDetail=(word:String)->Unit
class WordListAdapter(var wordList: MutableList<Word> = mutableListOf(),var onItemClick:goToDetail) : RecyclerView.Adapter<WordListAdapter.Holder>() {
    inner class Holder(val binding: WordListItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Word) {
            try {
                binding.itemEnglishWord.text = item.englishWord
                binding.itemPersianWord.text = item.persianWord
                binding.root.setOnClickListener {
                    onItemClick(item.englishWord)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding= WordListItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(wordList[position])
    }

    override fun getItemCount(): Int {
        return wordList.size
    }
    fun setDate(newList: List<Word>){
        wordList.clear()
        wordList.addAll(newList)
        notifyDataSetChanged()
    }
}