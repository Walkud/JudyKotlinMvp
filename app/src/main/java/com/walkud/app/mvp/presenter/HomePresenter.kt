package com.walkud.app.mvp.presenter

import com.trello.rxlifecycle2.android.FragmentEvent
import com.walkud.app.common.exception.ExceptionHandle
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.fragment.HomeFragment
import com.walkud.app.rx.RxSubscribe
import com.walkud.app.rx.transformer.NetTransformer
import io.reactivex.ObservableTransformer

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
    fun refreshListData(transformer: ObservableTransformer<HomeBean, HomeBean>) {

        model.getFirstHomeData()
                .compose(NetTransformer())
                .compose(transformer)
                .compose(bindFragmentUntilEvent(FragmentEvent.DESTROY))//自己指定绑定的声明周期，缺点是UI的类型(比如Activity或Fragment)切换会导致错误
                .subscribe(object : RxSubscribe<HomeBean>() {
                    override fun call(result: HomeBean) {
                        homeBean = result
                        nextPageUrl = result.nextPageUrl

                        //更新BannerUI
                        view.updateBannerUi(result.bannerData?.issueList!![0].itemList)
                        view.updateListUi(homeBean!!.issueList[0].itemList)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        view.showToast(ExceptionHandle.handleException(e))
                    }
                })
    }

    /**
     * 获取更多列表数据
     */
    fun loadMoreListData() {
        model.getMoreHomeData(nextPageUrl!!).compose(NetTransformer())
                .compose(view.getSmartRefreshTransformer())
                .compose(bindUntilOnDestroyEvent())//父类封装判断声明周期，缺点是需要对所需的生命周期都需要判断和封装
                .subscribe(object : RxSubscribe<HomeBean>() {
                    override fun call(result: HomeBean) {

                        nextPageUrl = result.nextPageUrl
                        //加载更多，添加至缓存列表中
                        homeBean!!.issueList[0].itemList.addAll(result.issueList[0].itemList)
                        view.updateListUi(homeBean!!.issueList[0].itemList)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        view.showToast(ExceptionHandle.handleException(e))
                    }
                })
    }
}