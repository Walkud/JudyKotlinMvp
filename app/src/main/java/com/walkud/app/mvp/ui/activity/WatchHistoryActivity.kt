package com.walkud.app.mvp.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.walkud.app.R
import com.walkud.app.mvp.base.MvpActivity
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.presenter.WatchHistoryPresenter
import com.walkud.app.mvp.ui.adapter.WatchHistoryAdapter
import com.walkud.app.utils.StatusBarUtil
import com.walkud.app.view.ProgressView
import kotlinx.android.synthetic.main.layout_watch_history.*
import java.util.*

/**
 * 观看记录 UI
 * Created by Zhuliya on 2018/12/6
 */
class WatchHistoryActivity : MvpActivity<WatchHistoryPresenter>() {

    private val watchHistoryAdapter = WatchHistoryAdapter()

    override fun getP() = WatchHistoryPresenter().apply { view = this@WatchHistoryActivity }

    override fun getLayoutId() = R.layout.layout_watch_history

    override fun getMultipleStatusProgressView() = ProgressView.MultipleStatusProgress(multipleStatusView)

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?) {

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = watchHistoryAdapter

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)

        presenter.queryWatchHistory()
    }

    /**
     * 添加事件
     */
    override fun addListener() {
        super.addListener()
        //返回
        toolbar.setNavigationOnClickListener { backward() }

        //记录Item点击事件
        watchHistoryAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val itemData = adapter.getItem(position) as HomeBean.Issue.Item
            VideoDetailActivity.startActivity(this@WatchHistoryActivity, view, itemData)
        }
    }

    /**
     * 更新列表UI
     */
    fun updateListUi(itemDatas: ArrayList<HomeBean.Issue.Item>) {
        if (itemDatas.isEmpty()) {
            multipleStatusView.showEmpty()
        } else {
            watchHistoryAdapter.setNewData(itemDatas)
        }
    }

}