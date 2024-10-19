package com.tadevi.demo.app.screens.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tadevi.demo.app.R
import com.tadevi.demo.app.databinding.FragmentUserDetailBinding
import com.tadevi.demo.app.screens.BaseFragment
import com.tadevi.demo.app.screens.list.UserListFragment
import com.tadevi.demo.app.util.ImageLoaderUtil
import com.tadevi.demo.app.util.ToastUtil
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserDetailFragment : BaseFragment<FragmentUserDetailBinding>() {
    private var username = ""
    private val viewModel by viewModel<UserDetailViewModel>()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentUserDetailBinding {
        return FragmentUserDetailBinding.inflate(layoutInflater, container, false)
    }

    override fun rootView(): View {
        return requireBinding().root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        username = arguments?.getString(UserListFragment.EXTRA_USER_NAME).orEmpty()
        viewModel.loadData(username)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { updateUI(it) }
            }
        }

        viewModel.loadData(username)
    }

    private fun updateUI(state: UserDetailUIState) {
        when (state) {
            is UserDetailUIState.Loading -> {
                // for simplicity, we just show a toast here
                ToastUtil.showToast(requireContext(), getString(R.string.txt_loading))
            }

            is UserDetailUIState.Fail -> {
                ToastUtil.showToast(requireContext(), getString(R.string.error_while_loading_data))
            }

            is UserDetailUIState.Success -> {
                requireBinding().apply {
                    tvUsername.text = state.data.username
                    tvLocation.text = state.data.location
                    tvBlogLink.text = state.data.htmlUrl
                    tvFollowerCount.text = state.data.followers.toString()
                    tvFollowingCount.text = state.data.following.toString()
                    ImageLoaderUtil.loadCircleShape(ivAvatar, state.data.avatarUrl)
                }
            }
        }
    }
}