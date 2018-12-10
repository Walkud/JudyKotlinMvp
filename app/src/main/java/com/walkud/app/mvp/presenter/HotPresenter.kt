package com.walkud.app.mvp.presenter

import com.trello.rxlifecycle2.android.FragmentEvent
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.TabInfoBean
import com.walkud.app.mvp.ui.fragment.HotFragment
import com.walkud.app.rx.RxSubscribe
import com.walkud.app.rx.transformer.NetTransformer

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
                .compose(NetTransformer())
                .compose(view.getMultipleStatusViewTransformer())
                .compose(bindFragmentUntilEvent(FragmentEvent.DESTROY))
                .subscribe(object : RxSubscribe<TabInfoBean>() {
                    override fun call(result: TabInfoBean) {
                        view.updateTabUi(result)
                    }
                })
    }


}