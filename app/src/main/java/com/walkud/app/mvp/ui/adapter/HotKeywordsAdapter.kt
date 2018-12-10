package com.walkud.app.mvp.ui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.walkud.app.R

/**
 * 热门关键词Adapter
 * Created by Zhuliya on 2018/11/26
 */
class HotKeywordsAdapter(data: MutableList<String>, layoutResId: Int = R.layout.item_flow_text) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: String?) {
        helper.setText(R.id.tv_title, item)

        val params = helper.getView<TextView>(R.id.tv_title).layoutParams
        if (params is FlexboxLayoutManager.LayoutParams) {
            params.flexGrow = 1.0f
        }
    }
}