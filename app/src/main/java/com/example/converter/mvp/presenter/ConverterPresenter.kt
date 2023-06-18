package com.example.converter.mvp.presenter

import com.example.converter.mvp.view.ConverterView
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter

class ConverterPresenter(private val router: Router) : MvpPresenter<ConverterView>() {

    fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
}