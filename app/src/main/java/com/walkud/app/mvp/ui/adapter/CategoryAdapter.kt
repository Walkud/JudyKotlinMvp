package com.walkud.app.mvp.ui.adapter

import android.graphics.Typeface
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hazz.kotlinmvp.glide.GlideApp
import com.walkud.app.R
import com.walkud.app.mvp.model.bean.CategoryBean
import com.walkud.app.utils.ContextUtil

/**
 * 发现-分类Adapter
 * Created by Zhuliya on 2018/11/30
 */
class CategoryAdapter(layoutResId: Int = R.layout.item_category, data: MutableList<CategoryBean> = ArrayList()) : BaseQuickAdapter<CategoryBean, BaseViewHolder>(layoutResId, data) {

    private var textTypeface: Typeface = Typeface.createFromAsset(ContextUtil.getContext().assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")

    override fun convert(helper: BaseViewHolder, item: CategoryBean) {
        helper.setText(R.id.tv_category_name, "#${item.name}")
        //设置方正兰亭细黑简体
        helper.getView<TextView>(R.id.tv_category_name).typeface = textTypeface

        GlideApp.with(mContext)
                .load(item.bgPicture)
                .placeholder(R.color.color_darker_gray)
                .transition(DrawableTransitionOptions().crossFade())
                .thumbnail(0.5f)
                .into(helper.getView(R.id.iv_category))
    }
}