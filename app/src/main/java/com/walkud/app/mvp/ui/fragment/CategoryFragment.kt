package com.walkud.app.mvp.ui.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.walkud.app.R
import com.walkud.app.common.ExtraKey
import com.walkud.app.mvp.base.MvpFragment
import com.walkud.app.mvp.model.bean.CategoryBean
import com.walkud.app.mvp.presenter.CategoryPresenter
import com.walkud.app.mvp.ui.activity.CategoryDetailActivity
import com.walkud.app.mvp.ui.adapter.CategoryAdapter
import com.walkud.app.rx.transformer.MultipleStatusViewTransformer
import com.walkud.app.utils.DisplayManager
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.fragment_category.*

/**
 * 发现-分类 UI
 * Created by Zhuliya on 2018/11/29
 */
class CategoryFragment : MvpFragment<CategoryPresenter>() {

    private val categoryAdapter = CategoryAdapter()

    companion object {
        fun getInstance() = CategoryFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_category

    override fun getP() = CategoryPresenter().apply { view = this@CategoryFragment }

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?, rootView: View) {

        mRecyclerView.adapter = categoryAdapter
        mRecyclerView.layoutManager = GridLayoutManager(activity, 2)

        mRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                val position = parent.getChildPosition(view)
                val offset = DisplayManager.dip2px(2f)!!

                outRect.set(if (position % 2 == 0) 0 else offset, offset,
                        if (position % 2 == 0) offset else 0, offset)
            }

        })
    }

    /**
     * 点击事件
     */
    override fun addListener() {
        super.addListener()

        categoryAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, _, position ->
            val data = adapter.getItem(position) as CategoryBean
            val intent = Intent(activity, CategoryDetailActivity::class.java)
            intent.putExtra(ExtraKey.CATEGORY_DATA, data)
            startActivity(intent)
        }

        //异常布局，点击重新加载
        multipleStatusView.setOnClickListener {
            presenter.queryCategoryData()
        }
    }

    /**
     * 获取进度、错误、内容切换View事务
     */
    override fun <VT> getMultipleStatusViewTransformer(): ObservableTransformer<VT, VT> {
        return MultipleStatusViewTransformer(multipleStatusView)
    }

    /**
     * 懒加载数据
     */
    override fun onLazyLoadOnce() {
        super.onLazyLoadOnce()
        presenter.queryCategoryData()
    }

    /**
     * 更新列表UI
     */
    fun updateListUi(categoryList: ArrayList<CategoryBean>) {
        categoryAdapter.setNewData(categoryList)
    }

    /**
     * 显示错误UI
     */
    fun showErrorUi(errorCode: Int) {
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            multipleStatusView?.showNoNetwork()
        } else {
            multipleStatusView?.showError()
        }
    }
}