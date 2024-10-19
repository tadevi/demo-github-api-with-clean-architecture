package com.tadevi.demo.app.screens.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.app.databinding.UserBriefItemViewBinding

class UserListAdapter(
    private val onItemClick: (UserBrief) -> Unit,
): PagingDataAdapter<UserBrief, UserBriefItemViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: UserBriefItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBriefItemViewHolder {
        val binding = UserBriefItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserBriefItemViewHolder(binding, onItemClick)
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<UserBrief>() {
    override fun areItemsTheSame(oldItem: UserBrief, newItem: UserBrief): Boolean {
        return oldItem.username == newItem.username
    }

    override fun areContentsTheSame(oldItem: UserBrief, newItem: UserBrief): Boolean {
        return oldItem == newItem
    }
}
