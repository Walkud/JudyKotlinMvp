package com.walkud.app.mvp.ui.adapter

import android.support.v4.content.ContextCompat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.walkud.app.R
import com.walkud.app.common.extensions.durationFormat
import com.walkud.app.common.glide.GlideApp
import com.walkud.app.mvp.model.bean.HomeBean

/**
 * 观看记录 Adapter
 * Created by Zhuliya on 2018/12/6
 */
class WatchHistoryAdapter(layoutResId: Int = R.layout.item_video_small_card, data: MutableList<HomeBean.Issue.Item> = ArrayList()) :
        BaseQuickAdapter<HomeBean.Issue.Item, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, data: HomeBean.Issue.Item) {
        helper.setText(R.id.tv_title, data.data?.title!!)
                .setText(R.id.tv_tag, "#${data.data.category} / ${durationFormat(data.data.duration)}")
                .setTextColor(R.id.tv_title, ContextCompat.getColor(mContext, R.color.color_black))

        GlideApp.with(mContext)
                .load(data.data.cover.detail)
                .placeholder(R.drawable.placeholder_banner)
                .transition(DrawableTransitionOptions().crossFade())
                .into(helper.getView(R.id.iv_video_small_card))
    }
}