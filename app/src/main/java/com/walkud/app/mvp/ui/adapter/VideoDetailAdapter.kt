package com.walkud.app.mvp.ui.adapter

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.kotlinmvp.glide.GlideApp
import com.walkud.app.R
import com.walkud.app.common.extensions.durationFormat
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.utils.ContextUtil

/**
 * 视频详情列表Adatper
 * Created by Zhuliya on 2018/11/27
 */
class VideoDetailAdapter(data: MutableList<HomeBean.Issue.Item> = ArrayList()) : BaseMultiItemQuickAdapter<HomeBean.Issue.Item, BaseViewHolder>(data) {

    private var textTypeface: Typeface? = null

    init {
        addItemType(HomeBean.Issue.Item.ITEM_TYPE_CONTENT, R.layout.item_video_detail_info)
        addItemType(HomeBean.Issue.Item.ITEM_TYPE_TEXT_CARD, R.layout.item_video_text_card)
        addItemType(HomeBean.Issue.Item.ITEM_TYPE_VIDEO_SMALL_CARD, R.layout.item_video_small_card)
        textTypeface = Typeface.createFromAsset(ContextUtil.getContext().assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }

    override fun convert(helper: BaseViewHolder, item: HomeBean.Issue.Item) {

        when (helper.itemViewType) {
            HomeBean.Issue.Item.ITEM_TYPE_CONTENT -> convertContent(helper, item)

            HomeBean.Issue.Item.ITEM_TYPE_TEXT_CARD -> convertTextCard(helper, item)

            HomeBean.Issue.Item.ITEM_TYPE_VIDEO_SMALL_CARD -> convertVideoSmallCard(helper, item)
        }
    }

    /**
     * 设置当前视频相关数据 Item
     */
    fun convertContent(helper: BaseViewHolder, data: HomeBean.Issue.Item) {
        data.data?.title?.let { helper.setText(R.id.tv_title, it) }
        //视频简介
        data.data?.description?.let { helper.setText(R.id.expandable_text, it) }
        //标签
        helper.setText(R.id.tv_tag, "#${data.data?.category} / ${durationFormat(data.data?.duration)}")
        //喜欢
        helper.setText(R.id.tv_action_favorites, data.data?.consumption?.collectionCount.toString())
        //分享
        helper.setText(R.id.tv_action_share, data.data?.consumption?.shareCount.toString())
        //评论
        helper.setText(R.id.tv_action_reply, data.data?.consumption?.replyCount.toString())

        if (data.data?.author != null) {
            helper.setText(R.id.tv_author_name, data.data.author.name)
                    .setText(R.id.tv_author_desc, data.data.author.description)

            val iv: ImageView = helper.getView(R.id.iv_avatar)
            GlideApp.with(mContext)
                    .load(data.data.cover.detail)
                    .optionalTransform(com.hazz.kotlinmvp.glide.GlideRoundTransform())
                    .placeholder(R.drawable.placeholder_banner)
                    .into(iv)

        } else {
            helper.setVisible(R.id.layout_author_view, false)
        }

        helper.addOnClickListener(R.id.tv_action_favorites)
                .addOnClickListener(R.id.tv_action_share)
                .addOnClickListener(R.id.tv_action_reply)
                .addOnClickListener(R.id.tv_action_offline)
                .addOnClickListener(R.id.tv_attention)
    }

    /**
     * 设置文本 Item
     */
    fun convertTextCard(helper: BaseViewHolder, item: HomeBean.Issue.Item) {
        helper.setText(R.id.tv_text_card, item.data?.text!!)
        //设置方正兰亭细黑简体
        helper.getView<TextView>(R.id.tv_text_card).typeface = textTypeface
    }

    /**
     * 设置相关视频 Item
     */
    fun convertVideoSmallCard(helper: BaseViewHolder, item: HomeBean.Issue.Item) {
        helper.setText(R.id.tv_title, item.data?.title!!)
                .setText(R.id.tv_tag, "#${item.data.category} / ${durationFormat(item.data.duration)}")

        val iv: ImageView = helper.getView(R.id.iv_video_small_card)
        GlideApp.with(mContext)
                .load(item.data.cover.detail)
                .optionalTransform(com.hazz.kotlinmvp.glide.GlideRoundTransform())
                .placeholder(R.drawable.placeholder_banner)
                .into(iv)
    }
}