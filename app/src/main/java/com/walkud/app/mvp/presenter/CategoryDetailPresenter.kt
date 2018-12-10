package com.walkud.app.mvp.presenter

import com.trello.rxlifecycle2.android.ActivityEvent
import com.walkud.app.common.ExtraKey
import com.walkud.app.common.exception.ExceptionHandle
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.CategoryBean
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.activity.CategoryDetailActivity
import com.walkud.app.rx.RxSubscribe
import com.walkud.app.rx.transformer.NetTransformer

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
                .compose(NetTransformer())
                .compose(view.getMultipleStatusViewTransformer())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : RxSubscribe<HomeBean.Issue>() {
                    override fun call(result: HomeBean.Issue) {
                        issue = result
                        nextPageUrl = result.nextPageUrl
                        view.updateListUi(issue!!)
                    }
                })
    }


    /**
     * 获取详情列表更多数据
     */
    fun queryMoreCategoryData() {
        model.getMoreCategoryData(nextPageUrl!!)
                .compose(NetTransformer())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : RxSubscribe<HomeBean.Issue>() {

                    override fun call(result: HomeBean.Issue) {
                        issue!!.itemList.addAll(result.itemList)
                        view.updateListUi(issue!!)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        view.showToast(ExceptionHandle.handleException(e))
                    }
                })
    }

}