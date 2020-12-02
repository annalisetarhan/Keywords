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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.solved_fragment,
                container,
                false
        )

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.refreshSolvedQuestions()
        val adapter = viewModel.solvedQuestions.value?.let { SolvedAdapter(it) }
        binding.recyclerView.adapter = adapter

        viewModel.solvedQuestions.observe(viewLifecycleOwner, {
            it?.let {
                adapter?.submitList(it)
            }
        })

        return binding.root
    }
}