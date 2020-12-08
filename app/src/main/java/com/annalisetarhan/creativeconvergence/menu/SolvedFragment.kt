package com.annalisetarhan.creativeconvergence.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.annalisetarhan.creativeconvergence.R
import com.annalisetarhan.creativeconvergence.databinding.SolvedFragmentBinding
import com.annalisetarhan.creativeconvergence.ui.main.MainViewModel

class SolvedFragment : Fragment() {
    private lateinit var binding: SolvedFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.solved_fragment,
                container,
                false
        )

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        /*
         * Refresh list of solved questions and check if empty
         */

        viewModel.refreshSolvedQuestions()
        if (viewModel.solvedQuestions.value.isNullOrEmpty()) {
            showNoSolvedQuestionsMessage()
        } else {
            showSolvedQuestions()
        }

        return binding.root
    }

    private fun showSolvedQuestions() {
        // Might have been hidden earlier by showNoSolvedQuestionsMessage()
        binding.recyclerView.visibility = View.VISIBLE
        binding.solvedTitle.visibility = View.VISIBLE
        binding.noSolvedQuestionsTextview.visibility = View.GONE

        val adapter = viewModel.solvedQuestions.value?.let { SolvedAdapter(it) }
        binding.recyclerView.adapter = adapter

        viewModel.solvedQuestions.observe(viewLifecycleOwner, {
            it?.let {
                adapter?.submitList(it)
            }
        })
    }

    private fun showNoSolvedQuestionsMessage() {
        binding.recyclerView.visibility = View.GONE
        binding.solvedTitle.visibility = View.GONE
        binding.noSolvedQuestionsTextview.visibility = View.VISIBLE
    }
}