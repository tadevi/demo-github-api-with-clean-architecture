package com.tadevi.demo.app.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment<Binding>: Fragment() {
    protected var _binding: Binding? = null

    protected abstract fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Binding

    protected abstract fun rootView(): View

    fun requireBinding(): Binding = _binding!!

    protected fun setTitle(title: String) {
        requireActivity().actionBar?.title = title
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = initBinding(inflater, container, savedInstanceState)
        return rootView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}