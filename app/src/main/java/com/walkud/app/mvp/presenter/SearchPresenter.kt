package com.walkud.app.mvp.presenter

import com.walkud.app.common.exception.ExceptionHandle
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.activity.SearchActivity
import com.walkud.app.rx.RxSubscribe
import com.walkud.app.rx.transformer.NetTransformer
import com.walkud.app.rx.transformer.ProgressTransformer

/**
 * 搜索Presenter
 * Created by Zhuliya on 2018/11/26
 */
class SearchPresenter : BasePresenter<SearchActivity, MainModel>() {

    private var keyWords: String? = null//关键字
    private var nextPageUrl: String? = null//下一页Url
    private var issue: HomeBean.Issue? = null//查询结果

    /**
     * 请求热门关键词的数据
     */
    fun queryHotWordData() {
        model.getHotWordData()
                .compose(NetTransformer())
                .compose(bindUntilOnDestroyEvent())
                .subscribe(object : RxSubscribe<ArrayList<String>>() {
                    override fun call(result: ArrayList<String>) {
                        view.setHotWordData(result)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        view.showToast(ExceptionHandle.handleExceptionMsg(e))
                    }
                })
    }

    /**
     * 查询关键字
     */
    fun querySearchResult(keyStr: String) {
        keyWords = keyStr

        model.getSearchResult(keyWords!!)
                .compose(NetTransformer())
                .compose(ProgressTransformer(view))
                .compose(bindUntilOnDestroyEvent())
                .subscribe(object : RxSubscribe<HomeBean.Issue>() {
                    override fun call(result: HomeBean.Issue) {
                        issue = result
                        if (result.count > 0 && result.itemList.size > 0) {
                            nextPageUrl = result.nextPageUrl
                            view.updateSearchResultUi(result, keyStr)
                        } else
                            view.setEmptyView()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        view.showToast(ExceptionHandle.handleExceptionMsg(e))
                    }
                })
    }

    /**
     * 加载更多
     */
    fun loadMoreData() {
        model.getSearchIssueData(nextPageUrl!!)
                .compose(NetTransformer())
                .compose(bindUntilOnDestroyEvent())
                .subscribe(object : RxSubscribe<HomeBean.Issue>() {
                    override fun call(result: HomeBean.Issue) {
                        issue!!.itemList.addAll(result.itemList)
                        nextPageUrl = result.nextPageUrl
                        view.updateSearchResultUi(issue!!, keyWords!!)
                    }
                })
    }
}