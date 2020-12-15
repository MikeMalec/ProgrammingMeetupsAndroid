package com.example.programmingmeetups.framework.presentation.common

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import javax.inject.Inject

class MainNavigationHostFragment : NavHostFragment() {

    @Inject
    lateinit var fragmentFactory: CustomFragmentFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setFragmentFactory()
    }

    private fun setFragmentFactory() {
        childFragmentManager.fragmentFactory = fragmentFactory
    }
}