package com.walkud.app.mvp.ui.adapter

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.walkud.app.R
import com.walkud.app.common.extensions.durationFormat
import com.walkud.app.common.glide.GlideApp
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.model.bean.HomeBean.Issue.Item.Companion.ITEM_TYPE_CONTENT
import com.walkud.app.mvp.model.bean.HomeBean.Issue.Item.Companion.ITEM_TYPE_TEXT_HEADER

/**
 * 主页精选Adapter
 * Created by Zhuliya on 2018/11/20
 */
class HomeAdapter(data: List<HomeBean.Issue.Item> = ArrayList()) :
        BaseMultiItemQuickAdapter<HomeBean.Issue.Item, BaseViewHolder>(data) {

    /**
     * 初始化布局类型
     */
    init {
        addItemType(HomeBean.Issue.Item.ITEM_TYPE_TEXT_HEADER, R.layout.item_home_header)
        addItemType(HomeBean.Issue.Item.ITEM_TYPE_CONTENT, R.layout.item_home_content)
    }

    override fun convert(helper: BaseViewHolder, item: HomeBean.Issue.Item) {

        when (helper.itemViewType) {
            //TextHeader
            ITEM_TYPE_TEXT_HEADER -> {
                convertHeader(helper, item)
            }

            //content
            ITEM_TYPE_CONTENT -> {
                convertContent(helper, item)
            }
        }

    }

    /**
     * 处理头部Item
     */
    fun convertHeader(helper: BaseViewHolder, item: HomeBean.Issue.Item) {
        helper.setText(R.id.tvHeader, item.data?.text ?: "")
    }

    /**
     * 处理内容Item
     */
    fun convertContent(helper: BaseViewHolder, item: HomeBean.Issue.Item) {
        val itemData = item.data

        val defAvatar = R.mipmap.default_avatar
        val cover = itemData?.cover?.feed
        var avatar = itemData?.author?.icon
        var tagText: String? = "#"

        // 作者出处为空，就显获取提供者的信息
        if (avatar.isNullOrEmpty()) {
            avatar = itemData?.provider?.icon
        }
        // 加载封页图
        GlideApp.with(mContext)
                .load(cover)
                .placeholder(R.drawable.placeholder_banner)
                .transition(DrawableTransitionOptions().crossFade())
                .into(helper.getView(R.id.iv_cover_feed))

        // 如果提供者信息为空，就显示默认
        if (avatar.isNullOrEmpty()) {
            GlideApp.with(mContext)
                    .load(defAvatar)
                    .placeholder(R.mipmap.default_avatar).circleCrop()
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(helper.getView(R.id.iv_avatar))

        } else {
            GlideApp.with(mContext)
                    .load(avatar)
                    .placeholder(R.mipmap.default_avatar).circleCrop()
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(helper.getView(R.id.iv_avatar))
        }
        helper.setText(R.id.tv_title, itemData?.title ?: "")

        //遍历标签
        itemData?.tags?.take(4)?.forEach {
            tagText += (it.name + "/")
        }
        // 格式化时间
        val timeFormat = durationFormat(itemData?.duration)

        tagText += timeFormat

        helper.setText(R.id.tv_tag, tagText!!)

        helper.setText(R.id.tv_category, "#" + itemData?.category)
    }
}