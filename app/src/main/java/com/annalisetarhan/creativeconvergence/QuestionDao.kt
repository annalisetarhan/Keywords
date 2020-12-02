package com.annalisetarhan.creativeconvergence

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuestionDao {

    @Query("SELECT * FROM question_table WHERE status = 0 ORDER BY generated_id ASC LIMIT 1")
    fun getNextQuestion(): Question

    @Update
    fun updateQuestionStatus(question: Question)

    @Query("SELECT * FROM question_table WHERE status = 2 ORDER BY generated_id ASC")
    fun getSkippedQuestions(): List<Question>

    @Query("SELECT * FROM question_table WHERE status = 1 ORDER BY generated_id ASC")
    fun getSolvedQuestions(): List<Question>
}