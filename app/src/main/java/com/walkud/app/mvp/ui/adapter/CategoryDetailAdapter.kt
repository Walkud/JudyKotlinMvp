package com.walkud.app.mvp.ui.adapter

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.kotlinmvp.glide.GlideApp
import com.walkud.app.R
import com.walkud.app.common.extensions.durationFormat
import com.walkud.app.mvp.model.bean.HomeBean

/**
 * 搜索列表、分类详情列表Adapter
 * Created by Zhuliya on 2018/11/26
 */
class CategoryDetailAdapter(layoutResId: Int = R.layout.item_category_detail, data: List<HomeBean.Issue.Item> = ArrayList())
    : BaseQuickAdapter<HomeBean.Issue.Item, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: HomeBean.Issue.Item) {

        val itemData = item.data
        val cover = itemData?.cover?.feed ?: ""
        // 加载封页图
        GlideApp.with(mContext)
                .load(cover)
                .apply(RequestOptions().placeholder(R.drawable.placeholder_banner))
                .transition(DrawableTransitionOptions().crossFade())
                .into(helper.getView(R.id.iv_image))
        helper.setText(R.id.tv_title, itemData?.title ?: "")

        // 格式化时间
        val timeFormat = durationFormat(itemData?.duration)
        helper.setText(R.id.tv_tag, "#${itemData?.category}/$timeFormat")
    }
}