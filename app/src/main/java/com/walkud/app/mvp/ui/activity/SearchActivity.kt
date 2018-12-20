package com.walkud.app.mvp.ui.activity

import android.annotation.TargetApi
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.flexbox.*
import com.walkud.app.R
import com.walkud.app.common.extensions.closeKeyBoard
import com.walkud.app.common.extensions.openKeyBoard
import com.walkud.app.mvp.base.MvpActivity
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.presenter.SearchPresenter
import com.walkud.app.mvp.ui.adapter.CategoryDetailAdapter
import com.walkud.app.mvp.ui.adapter.HotKeywordsAdapter
import com.walkud.app.utils.ContextUtil
import com.walkud.app.utils.StatusBarUtil
import com.walkud.app.view.ViewAnimUtils
import kotlinx.android.synthetic.main.activity_search.*

/**
 * 搜索界面UI
 * Created by Zhuliya on 2018/11/26
 */
class SearchActivity : MvpActivity<SearchPresenter>() {

    private var mTextTypeface: Typeface? = null
    private var loadingMore = false
    private val listAdapter: CategoryDetailAdapter = CategoryDetailAdapter()

    override fun getP() = SearchPresenter().apply { view = this@SearchActivity }

    override fun getLayoutId() = R.layout.activity_search

    init {
        //细黑简体字体
        mTextTypeface = Typeface.createFromAsset(ContextUtil.getContext().assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }

    override fun initView(savedInstanceState: Bundle?) {
        executeAnimation()

        tv_title_tip.typeface = mTextTypeface
        tv_hot_search_words.typeface = mTextTypeface
        //初始化查询结果的 RecyclerView
        mRecyclerView_result.layoutManager = LinearLayoutManager(this)
        mRecyclerView_result.adapter = listAdapter

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
    }

    /**
     * 处理加载逻辑
     */
    override fun processLogic(savedInstanceState: Bundle?) {
        super.processLogic(savedInstanceState)
        presenter.queryHotWordData()
    }

    /**
     * 执行动画
     */
    private fun executeAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpEnterAnimation() // 入场动画
            setUpExitAnimation() // 退场动画
        } else {
            setUpView()
        }
    }

    /**
     * 进场动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpEnterAnimation() {
        val transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.arc_motion)
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {

            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        })
    }

    /**
     * 退场动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpExitAnimation() {
        val fade = Fade()
        window.returnTransition = fade
        fade.duration = 300
    }

    /**
     * 显示布局动画
     */
    private fun setUpView() {
        val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        animation.duration = 300
        rel_container.startAnimation(animation)
        rel_container.visibility = View.VISIBLE
        //打开软键盘
        openKeyBoard(et_search_view)
    }

    /**
     * 展示动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateRevealShow() {
        ViewAnimUtils.animateRevealShow(
                this, rel_frame,
                fab_circle.width / 2, R.color.backgroundColor,
                object : ViewAnimUtils.OnRevealAnimationListener {
                    override fun onRevealHide() {

                    }

                    override fun onRevealShow() {
                        setUpView()
                    }
                })
    }

    /**
     * 添加事件
     */
    override fun addListener() {
        super.addListener()

        //实现自动加载
        mRecyclerView_result.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView_result.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView_result.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loadingMore && lastVisibleItem == (itemCount - 1)) {
                    loadingMore = true
                    presenter.loadMoreData()
                }
            }
        })

        //取消
        tv_cancel.setOnClickListener { backward() }
        //键盘的搜索按钮
        et_search_view.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    closeKeyBoard(et_search_view)
                    val keyWords = et_search_view.text.toString().trim()
                    if (keyWords.isEmpty()) {
                        showToast("请输入你感兴趣的关键词")
                    } else {
                        presenter.querySearchResult(keyWords)
                    }
                }
                return false
            }

        })

        listAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as HomeBean.Issue.Item
            VideoDetailActivity.startActivity(this@SearchActivity, view, item)
        }

    }

    /**
     * 更新搜索结果UI
     */
    fun updateSearchResultUi(issue: HomeBean.Issue, keyWords: String) {
        loadingMore = false

        hideHotWordView()
        tv_search_count.visibility = View.VISIBLE
        tv_search_count.text = String.format(resources.getString(R.string.search_result_count), keyWords, issue.total)

        listAdapter.setNewData(issue.itemList)
    }

    /**
     * 没有找到相匹配的内容
     */
    fun setEmptyView() {
        showToast("抱歉，没有找到相匹配的内容")
        hideHotWordView()
        tv_search_count.visibility = View.GONE
        multipleStatusView.showEmpty()
    }

    /**
     * 隐藏热门关键字的 View
     */
    private fun hideHotWordView() {
        layout_hot_words.visibility = View.GONE
        layout_content_result.visibility = View.VISIBLE
    }

    /**
     * 显示热门关键字的 流式布局
     */
    private fun showHotWordView() {
        layout_hot_words.visibility = View.VISIBLE
        layout_content_result.visibility = View.GONE
    }

    /**
     * 设置热门关键词
     */
    fun setHotWordData(keys: ArrayList<String>) {
        showHotWordView()
        val hotKeywordsAdapter = HotKeywordsAdapter(keys)

        val flexBoxLayoutManager = FlexboxLayoutManager(this)
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP      //按正常方向换行
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW   //主轴为水平方向，起点在左端
        flexBoxLayoutManager.alignItems = AlignItems.CENTER    //定义项目在副轴轴上如何对齐
        flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START  //多个轴对齐方式

        mRecyclerView_hot.layoutManager = flexBoxLayoutManager
        mRecyclerView_hot.adapter = hotKeywordsAdapter

        //设置热门关键词点击事件
        hotKeywordsAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            closeKeyBoard(et_search_view)
            val keyWord = adapter.getItem(position) as String
            presenter.querySearchResult(keyWord)
        }
    }

}