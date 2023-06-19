package com.example.converter.mvp.model

import io.reactivex.rxjava3.core.Completable

fun interface IConvertAndSave {
    fun convertToPngRx(uri: String?): Completable
}