package com.walkud.app.mvp.presenter

import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.ui.fragment.HotFragment
import com.walkud.app.net.space.bindUi

/**
 * 热门 Presenter
 * Created by Zhuliya on 2018/12/4
 */
class HotPresenter : BasePresenter<HotFragment, MainModel>() {

    /**
     * 查询热门 Tab 数据
     */
    fun queryRankTabData() {
        model.getRankTabData()
            .bindUi(view.getMultipleStatusProgressView(), view)
            .request { view.updateTabUi(it) }
    }
}