package com.annalisetarhan.creativeconvergence.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annalisetarhan.creativeconvergence.Question
import com.annalisetarhan.creativeconvergence.R

class SkippedAdapter(
        private val skippedSet: List<Question>,
        private val clickListener: (Question) -> Unit
) : ListAdapter<Question, SkippedAdapter.ViewHolder>(SkippedQuestionDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cue1: TextView = view.findViewById(R.id.cue1)
        private val cue2: TextView = view.findViewById(R.id.cue2)
        private val cue3: TextView = view.findViewById(R.id.cue3)

        fun bind(question: Question) {
            cue1.text = question.cue1
            cue2.text = question.cue2
            cue3.text = question.cue3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.skipped_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = skippedSet[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            clickListener(item)
        }
    }
}

class SkippedQuestionDiffCallback : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.generated_id == newItem.generated_id
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }

}