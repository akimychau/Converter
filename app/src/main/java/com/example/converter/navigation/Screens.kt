package com.example.converter.navigation

import com.example.converter.ui.ConverterFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun converter() = FragmentScreen { ConverterFragment.newInstance() }
}