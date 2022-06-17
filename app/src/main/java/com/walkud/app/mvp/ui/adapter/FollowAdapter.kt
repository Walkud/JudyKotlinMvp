package com.walkud.app.mvp.ui.adapter

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
import com.chad.library.adapter.base.BaseViewHolder
import com.walkud.app.R
import com.walkud.app.common.glide.GlideApp
import com.walkud.app.mvp.model.bean.HomeBean

/**
 * 关注竖向列表Adapter
 * Created by Zhuliya on 2018/11/29
 */
class FollowAdapter(layoutResId: Int = R.layout.item_follow, data: MutableList<HomeBean.Issue.Item> = ArrayList()) :
        BaseQuickAdapter<HomeBean.Issue.Item, BaseViewHolder>(layoutResId, data) {

    //签到水平Item的点击事件
    var onItemClick: OnItemClickListener? = null

    /**
     * 提前初始化 嵌套水平的 RecyclerView
     */
    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val helper = super.onCreateDefViewHolder(parent, viewType)

        val recyclerView = helper.getView<RecyclerView>(R.id.fl_recyclerView)
        recyclerView?.apply {
            val adapter = FollowHorizontalAdapter()
            recyclerView.layoutManager = LinearLayoutManager(mContext as Activity, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
            //传递点击事件
            adapter.onItemClickListener = OnItemClickListener { adapter, view, position ->
                onItemClick?.onItemClick(adapter, view, position)
            }
        }
        return helper
    }

    override fun convert(helper: BaseViewHolder, item: HomeBean.Issue.Item) {

        val headerData = item.data?.header
        //加载作者头像
        GlideApp.with(mContext)
                .load(headerData?.icon!!)
                .placeholder(R.mipmap.default_avatar).circleCrop()
                .transition(DrawableTransitionOptions().crossFade())
                .into(helper.getView(R.id.iv_avatar))

        helper.setText(R.id.tv_title, headerData.title)
                .setText(R.id.tv_desc, headerData.description)

        val recyclerView = helper.getView<RecyclerView>(R.id.fl_recyclerView)
        (recyclerView.adapter as FollowHorizontalAdapter).setNewData(item.data.itemList)
    }
}