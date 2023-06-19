package com.example.converter.mvp.presenter

import com.example.converter.mvp.model.ConvertAndSaveImpl
import com.example.converter.mvp.model.ImagePickerImpl
import com.example.converter.mvp.view.ConverterView
import com.example.converter.utils.disposeBy
import com.example.converter.utils.subscribeByDefault
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter
import java.util.concurrent.TimeUnit

class ConverterPresenter(
    private val router: Router,
    private val convertAndSave: ConvertAndSaveImpl,
    private val imagePicker: ImagePickerImpl
) : MvpPresenter<ConverterView>() {

    private val bag = CompositeDisposable()

    var uri: String? = null

    fun requestPermission() {
        imagePicker.requestPermission()
    }

    fun convertAndSave() {
        viewState.showLoading()
        convertAndSave.convertToPngRx(uri)
            .delay(3, TimeUnit.SECONDS)
            .subscribeByDefault()
            .subscribe(
                {
                    viewState.hideLoading()
                    viewState.makeToastSuccess()
                },
                {
                    viewState.makeToastError(it)
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

    override fun onDestroy() {
        super.onDestroy()
        bag.dispose()
    }

    fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
}