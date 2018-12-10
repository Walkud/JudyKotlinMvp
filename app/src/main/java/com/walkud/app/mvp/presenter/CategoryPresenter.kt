package com.walkud.app.mvp.presenter

import com.trello.rxlifecycle2.android.FragmentEvent
import com.walkud.app.common.exception.ExceptionHandle
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.CategoryBean
import com.walkud.app.mvp.ui.fragment.CategoryFragment
import com.walkud.app.rx.RxSubscribe
import com.walkud.app.rx.transformer.NetTransformer

/**
 * 发现-分类Presenter
 * Created by Zhuliya on 2018/11/30
 */
class CategoryPresenter : BasePresenter<CategoryFragment, MainModel>() {

    /**
     * 获取分类列表
     */
    fun queryCategoryData() {
        model.getCategoryData()
                .compose(NetTransformer())
                .compose(view.getMultipleStatusViewTransformer())
                .compose(bindFragmentUntilEvent(FragmentEvent.DESTROY))
                .subscribe(object : RxSubscribe<ArrayList<CategoryBean>>() {
                    override fun call(result: ArrayList<CategoryBean>) {
                        //更新UI列表
                        view.updateListUi(result)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        //处理错误
                        ExceptionHandle.handleException(e)
                        view.showErrorUi(ExceptionHandle.errorCode)
                    }
                })
    }


}