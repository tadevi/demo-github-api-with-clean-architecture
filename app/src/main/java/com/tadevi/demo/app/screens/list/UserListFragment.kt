package com.tadevi.demo.app.screens.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.tadevi.demo.app.R
import com.tadevi.demo.app.databinding.FragmentUserListBinding
import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.app.screens.BaseFragment
import com.tadevi.demo.app.util.ToastUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class UserListFragment : BaseFragment<FragmentUserListBinding>() {
    private val viewModel by activityViewModel<UserListViewModel>()

    private var mAdapter: UserListAdapter? = null

    private val navController by lazy { findNavController() }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentUserListBinding {
        return FragmentUserListBinding.inflate(layoutInflater, container, false)
    }

    override fun rootView(): View {
        return requireBinding().root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initData()
    }

    private fun initUI() {
        // init recycler view
        mAdapter = UserListAdapter {
            navController.navigate(
                R.id.action_userListFragment_to_userDetailFragment,
                bundleOf(EXTRA_USER_NAME to it.username)
            )
        }

        requireBinding().recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { collectState(it) }
            }
        }
    }

    private suspend fun collectState(state: UserListUIState) {
        when (state) {
            is UserListUIState.Initial -> {}
            is UserListUIState.Fail -> showError()
            is UserListUIState.Success -> submitData(state.pagingFlow)
        }
    }

    private fun showError() {
        ToastUtil.showToast(requireContext(), getString(R.string.txt_something_went_wrong))
    }

    private suspend fun submitData(flow: Flow<PagingData<UserBrief>>) {
        // use collectLatest because we only care about the latest data
        flow.collectLatest { mAdapter?.submitData(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAdapter = null
    }

    companion object {
        const val EXTRA_USER_NAME = "username"
    }
}