package com.annalisetarhan.creativeconvergence.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.annalisetarhan.creativeconvergence.R
import com.annalisetarhan.creativeconvergence.databinding.SkippedFragmentBinding
import com.annalisetarhan.creativeconvergence.ui.main.MainViewModel

class SkippedFragment : Fragment() {
    private lateinit var binding: SkippedFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.skipped_fragment,
                container,
                false
        )

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        /*
         * Refresh list of skipped questions and check if empty.
         */
        viewModel.refreshSkippedQuestions()
        if (viewModel.skippedQuestions.value.isNullOrEmpty()) {
            showNoSkippedQuestionsMessage()
        } else {
            showSkippedQuestions()
        }

        return binding.root
    }

    /*
     * Send list of skipped questions to recyclerView adapter.
     * Include a callback to navigate to a skipped question if clicked.
     */
    private fun showSkippedQuestions() {
        // Might have been hidden earlier by showNoSkippedQuestionsMessage()
        binding.recyclerView.visibility = View.VISIBLE
        binding.skippedTitle.visibility = View.VISIBLE
        binding.noSkippedQuestionsTextview.visibility = View.GONE

        val adapter = viewModel.skippedQuestions.value?.let {
            SkippedAdapter(it) { item ->
                viewModel.currentQuestion.value = item
                this.findNavController().navigate(R.id.action_skippedFragment_to_mainFragment)
            }
        }
        binding.recyclerView.adapter = adapter

        viewModel.skippedQuestions.observe(viewLifecycleOwner, {
            it?.let {
                adapter?.submitList(it)
            }
        })
    }

    private fun showNoSkippedQuestionsMessage() {
        binding.recyclerView.visibility = View.GONE
        binding.skippedTitle.visibility = View.GONE
        binding.noSkippedQuestionsTextview.visibility = View.VISIBLE
    }
}