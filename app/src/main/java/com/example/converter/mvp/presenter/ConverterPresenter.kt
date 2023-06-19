package com.example.converter.mvp.presenter

import com.example.converter.mvp.model.ConvertAndSaveImpl
import com.example.converter.mvp.model.ImagePickerImpl
import com.example.converter.mvp.view.ConverterView
import com.example.converter.utils.disposeBy
import com.example.converter.utils.subscribeByDefault
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter

class ConverterPresenter(
    private val router: Router,
    private val convertAndSave: ConvertAndSaveImpl,
    private val imagePicker: ImagePickerImpl
) : MvpPresenter<ConverterView>() {

    private var bag = CompositeDisposable()

    var uri: String? = null

    fun requestPermission() {
        imagePicker.requestPermission()
    }

    fun convertAndSave() {
        viewState.showLoading()
        viewState.showCancelBtn()
        convertAndSave.convertToPngRx(uri)
            .subscribeByDefault()
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

    fun pickImage() {
        imagePicker.pickImageRx()
            .subscribeByDefault()
            .subscribe(
                {
                    viewState.makeToastGallery()
                },
                {
                    viewState.makeToastError(it)
                })
            .disposeBy(bag)
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