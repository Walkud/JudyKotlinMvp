package com.walkud.app.mvp.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.walkud.app.R
import com.walkud.app.common.ExtraKey
import com.walkud.app.mvp.base.BaseFragmentAdapter
import com.walkud.app.mvp.base.MvpFragment
import com.walkud.app.mvp.model.bean.TabInfoBean
import com.walkud.app.mvp.presenter.HotPresenter
import com.walkud.app.rx.transformer.MultipleStatusViewTransformer
import com.walkud.app.utils.StatusBarUtil
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * 热门 UI
 * Created by Zhuliya on 2018/12/4
 */
class HotFragment : MvpFragment<HotPresenter>() {

    private val mTabTitleList = ArrayList<String>()//存放 tab 标题
    private val mFragmentList = ArrayList<Fragment>()

    companion object {
        fun getInstance(title: String) = HotFragment().apply {
            arguments = Bundle().apply { putString(ExtraKey.COMMON_TITLE, title) }
        }
    }

    override fun getP() = HotPresenter().apply { view = this@HotFragment }

    override fun getLayoutId() = R.layout.fragment_hot

    /**
     * 获取加载进度切换事务
     */
    override fun <VT> getMultipleStatusViewTransformer(): ObservableTransformer<VT, VT> {
        return MultipleStatusViewTransformer(multipleStatusView)
    }

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?, rootView: View) {

        tv_header_title.text = arguments?.getString("title")

        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }

        presenter.queryRankTabData()
    }

    /**
     * 添加点击事件
     */
    override fun addListener() {
        super.addListener()
        //异常布局，点击重新加载
        multipleStatusView.setOnRetryClickListener {
            presenter.queryRankTabData()
        }
    }

    /**
     * 更新 Tab UI
     */
    fun updateTabUi(tabInfoBean: TabInfoBean) {

        tabInfoBean.tabInfo.tabList.mapTo(mTabTitleList) { it.name }
        tabInfoBean.tabInfo.tabList.mapTo(mFragmentList) { RankFragment.getInstance(it.apiUrl) }

        mViewPager.adapter = BaseFragmentAdapter(childFragmentManager, mFragmentList, mTabTitleList)
        mViewPager.offscreenPageLimit = mFragmentList.size
        mTabLayout.setupWithViewPager(mViewPager)
    }

}