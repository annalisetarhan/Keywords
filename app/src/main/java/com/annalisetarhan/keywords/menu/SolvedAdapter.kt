package com.annalisetarhan.keywords.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annalisetarhan.keywords.Question
import com.annalisetarhan.keywords.R
import java.util.*

class SolvedAdapter(
        private val solvedSet: List<Question>
) : ListAdapter<Question, SolvedAdapter.ViewHolder>(SolvedQuestionDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cue1: TextView = view.findViewById(R.id.cue1)
        private val cue2: TextView = view.findViewById(R.id.cue2)
        private val cue3: TextView = view.findViewById(R.id.cue3)
        private val solution: TextView = view.findViewById(R.id.solution)
        private val difficulty: TextView = view.findViewById(R.id.difficulty)

        fun bind(question: Question) {
            cue1.text = question.cue1
            cue2.text = question.cue2
            cue3.text = question.cue3
            solution.text = question.solution?.toUpperCase(Locale.ROOT)
            difficulty.text = question.difficulty_phrase
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.solved_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(solvedSet[position])
    }
}

class SolvedQuestionDiffCallback : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.generated_id == newItem.generated_id
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }

}