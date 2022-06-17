package com.walkud.app.mvp.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.walkud.app.R
import com.walkud.app.common.ExtraKey
import com.walkud.app.mvp.base.MvpFragment
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.presenter.RankPresenter
import com.walkud.app.mvp.ui.activity.VideoDetailActivity
import com.walkud.app.mvp.ui.adapter.CategoryDetailAdapter
import com.walkud.app.view.ProgressView
import kotlinx.android.synthetic.main.fragment_rank.*
import kotlinx.android.synthetic.main.fragment_rank.mRecyclerView
import kotlinx.android.synthetic.main.fragment_rank.multipleStatusView
import kotlinx.android.synthetic.main.layout_watch_history.*

/**
 * 热门-排行UI
 * Created by Zhuliya on 2018/12/4
 */
class RankFragment : MvpFragment<RankPresenter>() {

    private val categoryDetailAdapter = CategoryDetailAdapter()

    companion object {

        fun getInstance(apiUrl: String) = RankFragment().apply {
            arguments = Bundle().apply { putString(ExtraKey.API_URL, apiUrl) }
        }
    }

    override fun getP() = RankPresenter().apply { view = this@RankFragment }

    override fun getLayoutId() = R.layout.fragment_rank

    /**
     * 获取进度、错误、内容切换View事务
     */
    override fun getMultipleStatusProgressView() =
        ProgressView.MultipleStatusProgress(multipleStatusView)

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?, rootView: View) {
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = categoryDetailAdapter
        presenter.init()
    }

    override fun onLazyLoadOnce() {
        super.onLazyLoadOnce()
        presenter.queryRankList()
    }

    /**
     * 添加事件
     */
    override fun addListener() {
        super.addListener()
        categoryDetailAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                val itemData = adapter.getItem(position) as HomeBean.Issue.Item
                VideoDetailActivity.startActivity(activity!!, view, itemData)
            }

        //异常布局，点击重新加载
        multipleStatusView.setOnClickListener {
            presenter.queryRankList()
        }
    }

    /**
     * 更新列表UI
     */
    fun updateListUi(itemList: ArrayList<HomeBean.Issue.Item>) {
        categoryDetailAdapter.setNewData(itemList)
    }
}