package com.walkud.app.mvp.presenter

import com.walkud.app.common.ExtraKey
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.ui.fragment.RankFragment
import com.walkud.app.net.space.bindUi

/**
 * 热门-排行Presenter
 * Created by Zhuliya on 2018/12/4
 */
class RankPresenter : BasePresenter<RankFragment, MainModel>() {


    private var apiUrl: String? = null//排行请求url

    /**
     * 初始化
     */
    fun init() {
        apiUrl = view.arguments?.getString(ExtraKey.API_URL)
        if (apiUrl == null) {
            view.showToast("")
            view.backward()
            return
        }
    }


    /**
     * 查询排行列表数据
     */
    fun queryRankList() {
        model.getHotRankData(apiUrl!!)
            .bindUi(view.getMultipleStatusProgressView(), view)
            .request { view.updateListUi(it.itemList) }
    }

}