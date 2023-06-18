package com.example.converter.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ConverterView : MvpView {

    fun showDialogForRequestPermission()
    fun showDialogForClosedPermission()
    fun showLoading()
    fun hideLoading()
    fun makeToastSuccess(pack: String)
    fun makeToastError()
    fun makeToastGallery(pack: String)
}