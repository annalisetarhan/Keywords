package com.annalisetarhan.keywords.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.annalisetarhan.keywords.R
import com.annalisetarhan.keywords.Status
import com.annalisetarhan.keywords.databinding.MainFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    // Setting this flag avoids turning check green when answer is revealed (as opposed to answered)
    private var revealedFlag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // When fragment is loaded or question changes, reset
        viewModel.currentQuestion.observe(viewLifecycleOwner, {
            setQuestion()
        })

        // Watch answer edittext
        binding.editTextAnswer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // If the solution was revealed, don't react
                if (!revealedFlag && checkAnswer()) {
                    onCorrectAnswer()
                }
            }
        })

        // When user hits enter on keyboard, check answer
        binding.editTextAnswer.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                // Yes, this list is the result of random flailing.
                EditorInfo.IME_ACTION_GO,
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_NEXT,
                EditorInfo.IME_ACTION_UNSPECIFIED -> {
                    if (!checkAnswer()) {
                        onIncorrectAnswer()
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.skipButton.setOnClickListener {
            viewModel.markCurrentQuestion(Status.SKIPPED)
            viewModel.slowlyLoadNextQuestion(0)
        }

        binding.revealButton.setOnClickListener {
            // Show frozen solution and inform viewModel
            // Set revealed flag so the textChangedListener doesn't react
            revealedFlag = true
            binding.editTextAnswer.isFocusable = false
            binding.editTextAnswer.setText(viewModel.currentQuestion.value?.solution)
            hideButtonBar()
            viewModel.markCurrentQuestion(Status.REVEALED)
            viewModel.slowlyLoadNextQuestion(3000)
            revealedFlag = false
        }

        return binding.root
    }

    private fun setQuestion() {
        binding.editTextAnswer.isFocusable = true
        binding.editTextAnswer.text.clear()
        binding.image.visibility = View.INVISIBLE
        showSkipButtonOnly()
    }

    private fun checkAnswer(): Boolean {
        val givenAnswer = binding.editTextAnswer.text.toString().toLowerCase(Locale.ROOT).trim()
        val correctAnswer = viewModel.currentQuestion.value?.solution
        return (givenAnswer == correctAnswer)
    }

    private fun onCorrectAnswer() {
        // Freeze answer, turn check mark green,
        // hide button bar, and inform viewModel
        binding.editTextAnswer.isFocusable = false
        turnCheckMark("GREEN")
        binding.image.visibility = View.VISIBLE
        hideButtonBar()
        viewModel.markCurrentQuestion(Status.SOLVED)
        viewModel.slowlyLoadNextQuestion()
    }


    private fun onIncorrectAnswer() {
        // Check mark immediately becomes a black X
        // After a second, answer is cleared and X turns back into a check
        turnCheckMark("BLACK_X")
        binding.image.visibility = View.VISIBLE
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            delay(1000)
            binding.image.visibility = View.INVISIBLE
            turnCheckMark("BLACK")
            binding.editTextAnswer.text.clear()
            showSkipAndRevealButtons()
        }
    }

    /*
     *  UTILITY FUNCTIONS
     */

    private fun turnCheckMark(color: String) {
        when(color) {
            "GREEN" -> binding.image.setImageResource(R.drawable.green_check)
            "BLACK" -> binding.image.setImageResource(R.drawable.black_check)
            "BLACK_X" -> binding.image.setImageResource(R.drawable.black_x)
        }
    }

    private fun hideButtonBar() {
        binding.skipButton.visibility = View.INVISIBLE
        binding.revealButton.visibility = View.INVISIBLE
    }

    private fun showSkipButtonOnly() {
        binding.skipButton.visibility = View.VISIBLE
        binding.revealButton.visibility = View.GONE
    }

    private fun showSkipAndRevealButtons() {
        binding.skipButton.visibility = View.VISIBLE
        binding.revealButton.visibility = View.VISIBLE
    }
}