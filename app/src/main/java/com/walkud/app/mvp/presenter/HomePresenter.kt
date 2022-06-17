package com.walkud.app.mvp.presenter

import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.fragment.HomeFragment
import com.walkud.app.net.space.bindUi

/**
 * Created by Zhuliya on 2018/11/20
 */
class HomePresenter : BasePresenter<HomeFragment, MainModel>() {

    var homeBean: HomeBean? = null//第一页Banner及列表数据
    var nextPageUrl: String? = null//下一页请求Url

    /**
     * 刷新列表数据
     * 使用状态布局切换事务
     */
    fun refreshListData() {

        model.getFirstHomeData()
            .bindUi(view.getSmartRefreshProgressView(), view)
            .doError { view.showExceptionToast(it) }
            .request {
                homeBean = it
                nextPageUrl = it.nextPageUrl

                //更新BannerUI
                view.updateBannerUi(it.bannerData?.issueList!![0].itemList)
                view.updateListUi(homeBean!!.issueList[0].itemList)
            }
    }

    /**
     * 获取更多列表数据
     */
    fun loadMoreListData() {
        nextPageUrl?.let { url ->
            model.getMoreHomeData(url)
                .bindUi(view.getSmartRefreshProgressView(), view)
                .doError { view.showExceptionToast(it) }
                .request {
                    nextPageUrl = it.nextPageUrl
                    //加载更多，添加至缓存列表中
                    homeBean!!.issueList[0].itemList.addAll(it.issueList[0].itemList)
                    view.updateListUi(homeBean!!.issueList[0].itemList)
                }
        }
    }
}