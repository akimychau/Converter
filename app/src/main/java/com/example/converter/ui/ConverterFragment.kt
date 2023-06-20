package com.example.converter.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.converter.App
import com.example.converter.R
import com.example.converter.databinding.FragmentConverterBinding
import com.example.converter.mvp.model.ConvertAndSaveImpl
import com.example.converter.mvp.presenter.ConverterPresenter
import com.example.converter.mvp.view.ConverterView
import com.example.converter.navigation.OnBackPressedListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ConverterFragment : MvpAppCompatFragment(), ConverterView, OnBackPressedListener {

    private var _viewBinding: FragmentConverterBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val presenter: ConverterPresenter by moxyPresenter {
        ConverterPresenter(
            App.instance.router,
            ConvertAndSaveImpl(App.instance.applicationContext),
            AndroidSchedulers.mainThread()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)

        _viewBinding = FragmentConverterBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.btn.setOnClickListener {
            requestPermission()
        }

        viewBinding.btn2.setOnClickListener {
            presenter.convertAndSave()
        }

        viewBinding.btn3.setOnClickListener {
            presenter.cancelConverting()
        }
    }

    private fun pickImage(registry: ActivityResultRegistry) {

        val getContent: ActivityResultLauncher<String> =
            registry.register(
                RESULT_REGISTRY_KEY,
                ActivityResultContracts.GetContent()
            ) { imageUri ->
                viewBinding.img.load(imageUri)
                presenter.uri = imageUri.toString()
            }

        Log.d("@@@", Thread.currentThread().name)
        getContent.launch("image/*")
    }

    private fun requestPermission() {
        val registry = requireActivity().activityResultRegistry

        val requestPermissionLauncher: ActivityResultLauncher<String> =
            registry.register(
                PERMISSION_REGISTRY_KEY,
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                when {
                    granted -> {
                        pickImage(registry)
                    }

                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        showDialogForRequestPermission()
                    }

                    else -> {
                        showDialogForClosedPermission()
                    }
                }
            }

        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        fun newInstance() = ConverterFragment()
        const val RESULT_REGISTRY_KEY = "pick_image"
        const val PERMISSION_REGISTRY_KEY = "permission"
    }

    override fun onBackPressed() = presenter.onBackPressed()

    override fun showDialogForRequestPermission() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_permission))
            .setMessage(
                getString(R.string.ask_for_permission)
            )
            .setPositiveButton(android.R.string.ok) { _, _ ->
                requestPermission()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showDialogForClosedPermission() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_for_closed_permission))
            .setMessage(getString(R.string.closed_permission))
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showLoading() {
        viewBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewBinding.progressBar.visibility = View.GONE
    }

    override fun makeToastSuccess() {
        Toast.makeText(context, "Картинка формата PNG сохранена", Toast.LENGTH_SHORT).show()
    }

    override fun makeToastError(error: Throwable) {
        Toast.makeText(context, "Ошибка $error", Toast.LENGTH_SHORT).show()
    }

    override fun makeToastGallery() {
        Toast.makeText(context, "Выберите картинку", Toast.LENGTH_SHORT).show()
    }

    override fun makeToastCancel() {
        Toast.makeText(context, "Отмена", Toast.LENGTH_SHORT).show()
    }

    override fun showCancelBtn() {
        viewBinding.btn3.visibility = View.VISIBLE
    }

    override fun hideCancelBtn() {
        viewBinding.btn3.visibility = View.GONE
    }
}