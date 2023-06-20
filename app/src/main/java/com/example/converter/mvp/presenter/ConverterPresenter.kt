package com.example.converter.mvp.presenter

import com.example.converter.mvp.model.IConvertAndSave
import com.example.converter.mvp.view.ConverterView
import com.example.converter.utils.disposeBy
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter

class ConverterPresenter(
    private val router: Router,
    private val convertAndSave: IConvertAndSave,
    private val scheduler: Scheduler
) : MvpPresenter<ConverterView>() {

    private var bag = CompositeDisposable()

    var uri: String? = null

    fun convertAndSave() {
        viewState.showLoading()
        viewState.showCancelBtn()
        convertAndSave.convertToPngRx(uri)
            .observeOn(scheduler)
            .subscribe(
                {
                    viewState.hideLoading()
                    viewState.hideCancelBtn()
                    viewState.makeToastSuccess()
                },
                {
                    viewState.makeToastError(it)
                    viewState.hideCancelBtn()
                    viewState.hideLoading()
                }
            ).disposeBy(bag)
    }

    fun cancelConverting() {
        bag.dispose()
        bag = CompositeDisposable()
        viewState.hideCancelBtn()
        viewState.hideLoading()
        viewState.makeToastCancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.dispose()
    }

    fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
}