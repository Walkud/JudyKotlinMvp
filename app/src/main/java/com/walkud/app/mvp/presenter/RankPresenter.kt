package com.walkud.app.mvp.presenter

import com.trello.rxlifecycle2.android.FragmentEvent
import com.walkud.app.common.ExtraKey
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.fragment.RankFragment
import com.walkud.app.rx.RxSubscribe
import com.walkud.app.rx.transformer.NetTransformer

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

        queryRankList()
    }


    /**
     * 查询排行列表数据
     */
    fun queryRankList() {
        model.getHotRankData(apiUrl!!)
                .compose(NetTransformer())
                .compose(view.getMultipleStatusViewTransformer())
                .compose(bindFragmentUntilEvent(FragmentEvent.DESTROY))
                .subscribe(object : RxSubscribe<HomeBean.Issue>() {

                    override fun call(result: HomeBean.Issue) {
                        view.updateListUi(result.itemList)
                    }
                })
    }

}