package com.example.converter.utils

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

fun Disposable.disposeBy(bag: CompositeDisposable) {
    bag.add(this)
}