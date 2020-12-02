package com.annalisetarhan.creativeconvergence

import androidx.lifecycle.MutableLiveData

class QuestionRepository(private val questionDao: QuestionDao) {
    lateinit var nextQuestion: Question
    val question = MutableLiveData<Question>()

    fun loadNextQuestion() {
        nextQuestion = questionDao.getNextQuestion()
        question.value = nextQuestion
    }

    fun markCurrentQuestion(question: Question, status: Status) {
        when (status) {
            Status.SOLVED -> question.status = Status.SOLVED.ordinal
            Status.SKIPPED -> question.status = Status.SKIPPED.ordinal
            Status.REVEALED -> question.status = Status.REVEALED.ordinal
            Status.UNSEEN -> error("Should never be Status.UNSEEN")
        }
        questionDao.updateQuestionStatus(question)
    }

    fun getSkippedQuestions(): List<Question> {
        return questionDao.getSkippedQuestions()
    }

    fun getSolvedQuestions(): List<Question> {
        return questionDao.getSolvedQuestions()
    }
}