package com.example.converter.mvp.presenter

import com.example.converter.mvp.view.MainView
import com.example.converter.navigation.Screens
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter

class MainPresenter(private val router: Router) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(Screens.converter())
    }

    fun onBackPressed() {
        router.exit()
    }
}