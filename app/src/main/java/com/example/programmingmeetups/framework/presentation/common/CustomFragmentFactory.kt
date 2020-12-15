package com.example.programmingmeetups.framework.presentation.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject

class CustomFragmentFactory @Inject constructor() : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            //::somefragment::class.java.name -> SomeFragment(depens)
            else -> super.instantiate(classLoader, className)
        }
    }
}