package com.walkud.app.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.walkud.app.R

/**
 * 我的Item Adapter
 * Created by Zhuliya on 2018/12/5
 */
class MineAdapter(data: ArrayList<Int>, layoutResId: Int = R.layout.item_mine) : BaseQuickAdapter<Int, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: Int) {
        helper.setText(R.id.item_name, item)
    }
}