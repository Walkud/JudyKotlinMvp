package com.walkud.app.mvp.ui.fragment

import android.os.Bundle
import android.view.View
import com.walkud.app.R
import com.walkud.app.common.ExtraKey
import com.walkud.app.mvp.base.BaseFragmentAdapter
import com.walkud.app.mvp.base.MvcFragment
import com.walkud.app.utils.DisplayManager
import com.walkud.app.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * 发现(和热门首页同样的布局）
 * Created by Zhuliya on 2018/11/29
 */
class DiscoveryFragment : MvcFragment() {

    private val tabList = listOf("关注", "分类")

    private val fragments = listOf(FollowFragment.getInstance(), CategoryFragment.getInstance())

    companion object {
        fun getInstance(title: String) = DiscoveryFragment().apply {
            arguments = Bundle().apply { putString(ExtraKey.COMMON_TITLE, title) }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_hot

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?, rootView: View) {
        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }

        tv_header_title.text = arguments?.getString("title")

        /**
         * getSupportFragmentManager() 替换为getChildFragmentManager()
         */
        mViewPager.adapter = BaseFragmentAdapter(childFragmentManager, fragments, tabList)
        mViewPager.offscreenPageLimit = fragments.size
        mTabLayout.setupWithViewPager(mViewPager)
    }
}