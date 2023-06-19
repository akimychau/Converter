package com.example.converter.mvp.model

import io.reactivex.rxjava3.core.Completable

interface IImagePicker {

    fun pickImageRx(): Completable
    fun requestPermission()
}