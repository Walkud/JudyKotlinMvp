package com.walkud.app.mvp.presenter

import com.walkud.app.common.exception.ExceptionHandle
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.ui.fragment.CategoryFragment
import com.walkud.app.net.space.bindUi

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
            .bindUi(view.getMultipleStatusProgressView(), view)
            .doError { //处理错误
                view.showErrorUi(it)
            }
            .request { //更新UI列表
                view.updateListUi(it)
            }
    }


}