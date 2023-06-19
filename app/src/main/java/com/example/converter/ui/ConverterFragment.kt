package com.example.converter.ui

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.converter.App
import com.example.converter.databinding.FragmentConverterBinding
import com.example.converter.mvp.model.ImagePickerImpl
import com.example.converter.mvp.presenter.ConverterPresenter
import com.example.converter.mvp.view.ConverterView
import com.example.converter.navigation.OnBackPressedListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ConverterFragment : MvpAppCompatFragment(), ConverterView, OnBackPressedListener {

    private var _viewBinding: FragmentConverterBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val presenter: ConverterPresenter by moxyPresenter {
        ConverterPresenter(App.instance.router,
            ImagePickerImpl(requireActivity().activityResultRegistry, { granted ->
                when {
                    granted -> {
                        presenter.pickImage()
                    }

                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        showDialogForRequestPermission()
                    }

                    else -> {
                        showDialogForClosedPermission()
                    }
                }
            }) { imageUri ->
                viewBinding.img.load(imageUri)
            })
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
            presenter.requestPermission()
        }
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
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к галерее")
            .setMessage(
                "Если не разрешить доступ при следующем запросе, то разрешить его " +
                        "можно будет в любой момент в настройках телефона.\nХотите разрешить сейчас?"
            )
            .setPositiveButton("Да") { _, _ ->
                presenter.requestPermission()
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showDialogForClosedPermission() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к галерее закрыт")
            .setMessage("Чтобы открыть галерею разрешите доступ в настройках телефона")
            .setPositiveButton("Ок") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
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

    override fun makeToastError(error: Throwable) {
        Toast.makeText(context, "Ошибка $error", Toast.LENGTH_SHORT).show()
    }

    override fun makeToastGallery() {
        Toast.makeText(context, "Выберите картинку", Toast.LENGTH_SHORT).show()
    }
}