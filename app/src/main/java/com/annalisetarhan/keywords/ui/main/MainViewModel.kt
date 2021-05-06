package com.annalisetarhan.keywords.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.annalisetarhan.keywords.Question
import com.annalisetarhan.keywords.QuestionRepository
import com.annalisetarhan.keywords.QuestionRoomDatabase
import com.annalisetarhan.keywords.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuestionRepository
    val currentQuestion: MutableLiveData<Question>
    var skippedQuestions: MutableLiveData<List<Question>>
    var solvedQuestions: MutableLiveData<List<Question>>

    init {
        val database = QuestionRoomDatabase.getDatabase(application)
        val questionDao = database.questionDao()
        repository = QuestionRepository(questionDao)
        repository.loadNextQuestion()
        currentQuestion = repository.question
        skippedQuestions = MutableLiveData(repository.getSkippedQuestions())
        solvedQuestions = MutableLiveData(repository.getSolvedQuestions())
    }

    fun markCurrentQuestion(status: Status) {
        currentQuestion.value?.let { repository.markCurrentQuestion(it, status) }
    }

    fun slowlyLoadNextQuestion(waitTime: Long = 1500) {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            delay(waitTime)
            repository.loadNextQuestion()
        }
    }

    fun refreshSkippedQuestions() {
        skippedQuestions.value = repository.getSkippedQuestions()
    }

    fun refreshSolvedQuestions() {
        solvedQuestions.value = repository.getSolvedQuestions()
    }
}