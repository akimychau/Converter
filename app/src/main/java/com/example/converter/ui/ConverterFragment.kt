package com.example.converter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.converter.App
import com.example.converter.databinding.FragmentConverterBinding
import com.example.converter.mvp.presenter.ConverterPresenter
import com.example.converter.mvp.view.ConverterView
import com.example.converter.navigation.OnBackPressedListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ConverterFragment : MvpAppCompatFragment(), ConverterView, OnBackPressedListener {

    private var _viewBinding: FragmentConverterBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val presenter by moxyPresenter { ConverterPresenter(App.instance.router) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)

        _viewBinding = FragmentConverterBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        fun newInstance() = ConverterFragment()
    }

    override fun onBackPressed() = presenter.onBackPressed()

    override fun showDialogForRequestPermission() {
        TODO("Not yet implemented")
    }

    override fun showDialogForClosedPermission() {
        TODO("Not yet implemented")
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun makeToastSuccess(pack: String) {
        TODO("Not yet implemented")
    }

    override fun makeToastError() {
        TODO("Not yet implemented")
    }

    override fun makeToastGallery(pack: String) {
        TODO("Not yet implemented")
    }
}