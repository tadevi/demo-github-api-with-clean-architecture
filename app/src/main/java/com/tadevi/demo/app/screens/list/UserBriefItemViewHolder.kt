package com.tadevi.demo.app.screens.list

import android.graphics.Color
import android.text.util.Linkify
import androidx.recyclerview.widget.RecyclerView
import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.app.databinding.UserBriefItemViewBinding
import com.tadevi.demo.app.util.ImageLoaderUtil

class UserBriefItemViewHolder(
    private val binding: UserBriefItemViewBinding,
    private val onItemClick: (UserBrief) -> Unit,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(userBrief: UserBrief?) {
        if (userBrief == null) return

        binding.run {
            root.setOnClickListener { onItemClick(userBrief) }
            tvUsername.text = userBrief.username
            ImageLoaderUtil.loadCircleShape(ivAvatar, userBrief.avatarUrl)
            tvHtmlLink.text = userBrief.htmlUrl
            tvHtmlLink.setLinkTextColor(Color.BLUE)
            Linkify.addLinks(tvHtmlLink, Linkify.WEB_URLS)
        }
    }
}