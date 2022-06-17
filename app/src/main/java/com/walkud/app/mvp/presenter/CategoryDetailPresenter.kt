package com.walkud.app.mvp.presenter

import com.walkud.app.common.ExtraKey
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.CategoryBean
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.activity.CategoryDetailActivity
import com.walkud.app.net.space.bindUi
import com.walkud.app.view.ProgressView

/**
 * 分类详情列表Presenter
 * Created by Zhuliya on 2018/11/30
 */
class CategoryDetailPresenter : BasePresenter<CategoryDetailActivity, MainModel>() {


    private lateinit var categoryData: CategoryBean
    private var issue: HomeBean.Issue? = null
    private var nextPageUrl: String? = null

    /**
     * 初始化
     */
    fun init() {
        if (!view.intent.hasExtra(ExtraKey.CATEGORY_DATA)) {
            //判断参数
            view.showToast("参数错误")
            view.backward()
            return
        }

        categoryData = view.intent.getSerializableExtra(ExtraKey.CATEGORY_DATA) as CategoryBean
        view.updateTopUi(categoryData)
        queryCategoryDetailList()
    }

    /**
     * 获取详情列表第一页数据
     */
    fun queryCategoryDetailList() {
        model.getCategoryDetailList(categoryData.id)
            .bindUi(view.getMultipleStatusProgressView(), view)
            .request {
                issue = it
                nextPageUrl = it.nextPageUrl
                view.updateListUi(issue!!)
            }
    }


    /**
     * 获取详情列表更多数据
     */
    fun queryMoreCategoryData() {
        model.getMoreCategoryData(nextPageUrl!!)
            .bindUi(ProgressView.EMPTY, view)
            .doError { view.showExceptionToast(it) }
            .request {
                issue!!.itemList.addAll(it.itemList)
                view.updateListUi(issue!!)
            }
    }

}