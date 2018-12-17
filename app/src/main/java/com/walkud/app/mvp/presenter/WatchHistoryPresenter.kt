package com.walkud.app.mvp.presenter

import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.activity.WatchHistoryActivity
import com.walkud.app.rx.RxSubscribe
import com.walkud.app.rx.transformer.NetTransformer
import java.util.*

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
                .compose(NetTransformer())
                .compose(view.getMultipleStatusViewTransformer())
                .compose(bindUntilOnDestroyEvent())
                .subscribe(object : RxSubscribe<ArrayList<HomeBean.Issue.Item>>() {
                    override fun call(result: ArrayList<HomeBean.Issue.Item>) {
                        view.updateListUi(result)
                    }
                })
    }

}