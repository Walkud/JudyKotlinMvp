package com.walkud.app.mvp.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hazz.kotlinmvp.glide.GlideApp
import com.walkud.app.R
import com.walkud.app.common.ExtraKey
import com.walkud.app.mvp.base.MvpFragment
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.presenter.HomePresenter
import com.walkud.app.mvp.ui.activity.SearchActivity
import com.walkud.app.mvp.ui.activity.VideoDetailActivity
import com.walkud.app.mvp.ui.adapter.HomeAdapter
import com.walkud.app.rx.transformer.MultipleStatusViewTransformer
import com.walkud.app.rx.transformer.SmartRefreshTransformer
import com.walkud.app.utils.StatusBarUtil
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 首页精选
 * Created by Zhuliya on 2018/11/20
 */
class HomeFragment : MvpFragment<HomePresenter>() {

    private lateinit var bgaBanner: BGABanner//BannerView

    private var homeAdapter = HomeAdapter()

    private var loadingMore = false//加载更多标记

    companion object {
        fun getInstance(title: String) = HomeFragment().apply {
            arguments = Bundle().apply { putString(ExtraKey.COMMON_TITLE, title) }
        }
    }

    override fun getP(): HomePresenter = HomePresenter().apply { view = this@HomeFragment }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }


    private val simpleDateFormat by lazy {
        SimpleDateFormat("- MMM. dd, 'Brunch' -", Locale.ENGLISH)
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    /**
     * 获取刷新控件事务
     */
    override fun <VT> getSmartRefreshTransformer(): ObservableTransformer<VT, VT> {
        return SmartRefreshTransformer(mRefreshLayout)
    }

    /**
     * 获取进度、错误、内容切换View事务
     */
    override fun <VT> getMultipleStatusViewTransformer(): ObservableTransformer<VT, VT> {
        return MultipleStatusViewTransformer(multipleStatusView)
    }

    /**
     * 初始化 View
     */
    override fun initView(savedInstanceState: Bundle?, rootView: View) {
        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.setOnRefreshListener {
            presenter.refreshListData(getSmartRefreshTransformer())
        }

        mRecyclerView.adapter = homeAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()

        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }

        //初始化Banner
        val view = layoutInflater.inflate(R.layout.item_home_banner, null)
        bgaBanner = view.findViewById(R.id.banner)
        //添加至Adapter头部
        homeAdapter.setHeaderView(view)
    }

    /**
     * 添加事件
     */
    override fun addListener() {
        super.addListener()
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val childCount = mRecyclerView.childCount
                    val itemCount = mRecyclerView.layoutManager.itemCount
                    val firstVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (firstVisibleItem + childCount == itemCount) {
                        if (!loadingMore) {
                            loadingMore = true
                            presenter.loadMoreListData()
                        }
                    }
                }
            }

            //RecyclerView滚动的时候调用
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                if (currentVisibleItemPosition == 0) {
                    //背景设置为透明
                    toolbar.setBackgroundColor(getColor(R.color.color_translucent))
                    iv_search.setImageResource(R.mipmap.ic_action_search_white)
                    tv_header_title.text = ""
                } else {
                    if (homeAdapter.itemCount > 1) {
                        toolbar.setBackgroundColor(getColor(R.color.color_title_bg))
                        iv_search.setImageResource(R.mipmap.ic_action_search_black)
                        val itemList = homeAdapter.data
                        val item = itemList[currentVisibleItemPosition - 1]
                        if (item.type == "textHeader") {
                            tv_header_title.text = item.data?.text
                        } else {
                            tv_header_title.text = simpleDateFormat.format(item.data?.date)
                        }
                    }
                }


            }
        })

        iv_search.setOnClickListener { openSearchActivity() }

        //Item点击事件
        homeAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val itemData = adapter.getItem(position) as HomeBean.Issue.Item
            VideoDetailActivity.startActivity(activity!!, view, itemData)
        }

        //异常布局，点击重新加载
        multipleStatusView.setOnRetryClickListener {
            presenter.refreshListData(getMultipleStatusViewTransformer())
        }
    }

    /**
     * 懒加载数据
     */
    override fun onLazyLoadOnce() {
        super.onLazyLoadOnce()
        presenter.refreshListData(getMultipleStatusViewTransformer())
    }

    private fun openSearchActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = activity?.let { ActivityOptionsCompat.makeSceneTransitionAnimation(it, iv_search, iv_search.transitionName) }
            startActivity(Intent(activity, SearchActivity::class.java), options?.toBundle())
        } else {
            startActivity(Intent(activity, SearchActivity::class.java))
        }
    }

    /**
     * 更新Banner列表UI
     */
    fun updateBannerUi(data: List<HomeBean.Issue.Item>) {
        val bannerFeedList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        //构建数据
        for (datum in data) {
            bannerFeedList.add(datum.data?.cover?.feed ?: "")
            bannerTitleList.add(datum.data?.title ?: "")
        }

        //设置Banner数据
        bgaBanner.run {
            setAutoPlayAble(bannerFeedList.size > 1)
            setData(bannerFeedList, bannerTitleList)
            setAdapter { banner, itemView, feedImageUrl, position ->
                GlideApp.with(activity!!)
                        .load(feedImageUrl)
                        .transition(DrawableTransitionOptions().crossFade())
                        .placeholder(R.drawable.placeholder_banner)
                        .into(banner.getItemImageView(position))

                itemView.setOnClickListener {
                    VideoDetailActivity.startActivity(activity!!, it, data[position])
                }
            }
        }

        if (bgaBanner.visibility == View.INVISIBLE) {
            bgaBanner.visibility = View.VISIBLE
        }
    }

    /**
     * 更新列表UI
     */
    fun updateListUi(data: List<HomeBean.Issue.Item>) {
        loadingMore = false
        multipleStatusView.showContent()
        homeAdapter.setNewData(data)
    }

    fun getColor(colorId: Int): Int {
        return resources.getColor(colorId)
    }
}