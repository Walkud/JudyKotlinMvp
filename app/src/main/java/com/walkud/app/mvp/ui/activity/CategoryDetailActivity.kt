package com.walkud.app.mvp.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hazz.kotlinmvp.glide.GlideApp
import com.walkud.app.R
import com.walkud.app.mvp.base.MvpActivity
import com.walkud.app.mvp.model.bean.CategoryBean
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.presenter.CategoryDetailPresenter
import com.walkud.app.mvp.ui.adapter.CategoryDetailAdapter
import com.walkud.app.rx.transformer.MultipleStatusViewTransformer
import com.walkud.app.utils.StatusBarUtil
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.activity_category_detail.*

/**
 * 分类详情列表UI
 * Created by Zhuliya on 2018/11/30
 */
class CategoryDetailActivity : MvpActivity<CategoryDetailPresenter>() {

    private val categoryDetailAdapter = CategoryDetailAdapter()
    private var loadingMore = false

    override fun getP() = CategoryDetailPresenter().apply { view = this@CategoryDetailActivity }

    override fun getLayoutId() = R.layout.activity_category_detail

    /**
     * 获取加载进度切换事务
     */
    override fun <VT> getMultipleStatusViewTransformer(): ObservableTransformer<VT, VT> {
        return MultipleStatusViewTransformer(multipleStatusView)
    }

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = categoryDetailAdapter
        //实现自动加载
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loadingMore && lastVisibleItem == (itemCount - 1)) {
                    loadingMore = true
                    presenter.queryMoreCategoryData()
                }
            }
        })

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)

        presenter.init()
    }

    /**
     * 更新顶部ToolbarUi
     */
    fun updateTopUi(categoryData: CategoryBean) {
        // 加载headerImage
        GlideApp.with(this)
                .load(categoryData.headerImage)
                .placeholder(R.color.color_darker_gray)
                .into(imageView)

        tv_category_desc.text = "#${categoryData.description}#"

        collapsing_toolbar_layout.title = categoryData.name
        collapsing_toolbar_layout.setExpandedTitleColor(Color.WHITE) //设置还没收缩时状态下字体颜色
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.BLACK) //设置收缩后Toolbar上字体的颜色
    }

    /**
     * 添加事件
     */
    override fun addListener() {
        super.addListener()
        toolbar.setNavigationOnClickListener { backward() }
        categoryDetailAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as HomeBean.Issue.Item
            VideoDetailActivity.startActivity(this@CategoryDetailActivity, view, item)
        }

        //异常布局，点击重新加载
        multipleStatusView.setOnClickListener {
            presenter.queryCategoryDetailList()
        }
    }

    /**
     * 更新列表UI
     */
    fun updateListUi(issue: HomeBean.Issue) {
        loadingMore = false
        categoryDetailAdapter.setNewData(issue.itemList)
    }

}