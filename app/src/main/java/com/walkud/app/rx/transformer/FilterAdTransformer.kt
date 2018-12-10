package com.walkud.app.rx.transformer

import com.walkud.app.mvp.model.bean.HomeBean
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * 广告过滤事务
 * 过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
 * Created by Zhuliya on 2018/11/21
 */
class FilterAdTransformer : ObservableTransformer<HomeBean, HomeBean> {

    override fun apply(upstream: Observable<HomeBean>): ObservableSource<HomeBean> {
        return upstream.flatMap {
            //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
            val bannerItemList = it.issueList[0].itemList

            //需要过滤的类型
            val filterTypes = arrayListOf("campaign", "banner2", "horizontalScrollCard")

            bannerItemList.filter { item ->
                filterTypes.contains(item.type)
            }.forEach { item ->
                //移除 item
                bannerItemList.remove(item)
            }

            Observable.just(it)
        }
    }
}