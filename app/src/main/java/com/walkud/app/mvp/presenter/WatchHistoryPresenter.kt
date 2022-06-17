package com.walkud.app.mvp.presenter

import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.ui.activity.WatchHistoryActivity
import com.walkud.app.net.space.bindUi

/**
 * 观看记录 Presneter
 * Created by Zhuliya on 2018/12/6
 */
class WatchHistoryPresenter : BasePresenter<WatchHistoryActivity, MainModel>() {

    /**
     * 查询观看记录列表数据
     */
    fun queryWatchHistory() {
        model.getWatchHistory()
            .bindUi(view.getMultipleStatusProgressView(), view)
            .request { view.updateListUi(it) }
    }

}