package com.annalisetarhan.keywords

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_table")
data class Question(
        @PrimaryKey var generated_id: Int?,
        var cue1: String?,
        var cue2: String?,
        var cue3: String?,
        var solution: String?,
        var difficulty_phrase: String?,
        var difficulty: Int?,
        var status: Int? // unseen = 0, solved = 1, skipped = 2, revealed = 3
)

enum class Status {
    UNSEEN, SOLVED, SKIPPED, REVEALED
}