package com.walkud.app.mvp.presenter

import com.walkud.app.common.exception.ExceptionHandle
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.activity.SearchActivity
import com.walkud.app.net.space.bindUi
import com.walkud.app.view.ProgressView

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
            .bindUi(ProgressView.EMPTY, view)
            .doError {
                view.showExceptionToast(it)
                view.showErrorUi(it)
            }
            .request { view.setHotWordData(it) }
    }

    /**
     * 查询关键字
     */
    fun querySearchResult(keyStr: String) {
        keyWords = keyStr

        model.getSearchResult(keyWords!!)
            .bindUi(view.getPrgressProgressView(), view)
            .doError {
                view.showExceptionToast(it)
                view.showErrorUi(it)
            }
            .request {
                issue = it
                if (it.count > 0 && it.itemList.size > 0) {
                    nextPageUrl = it.nextPageUrl
                    view.updateSearchResultUi(it, keyStr)
                } else {
                    view.setEmptyView()
                }
            }
    }

    /**
     * 加载更多
     */
    fun loadMoreData() {
        model.getSearchIssueData(nextPageUrl!!)
            .bindUi(ProgressView.EMPTY, view)
            .doError { view.showErrorUi(it) }
            .request {
                issue!!.itemList.addAll(it.itemList)
                nextPageUrl = it.nextPageUrl
                view.updateSearchResultUi(issue!!, keyWords!!)
            }
    }
}