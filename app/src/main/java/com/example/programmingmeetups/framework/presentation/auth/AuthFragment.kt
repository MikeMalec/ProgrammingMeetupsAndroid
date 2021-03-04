package com.example.programmingmeetups.framework.presentation.auth

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.programmingmeetups.R
import com.example.programmingmeetups.databinding.AuthFragmentBinding
import com.example.programmingmeetups.framework.utils.STORAGE_REQUEST
import com.example.programmingmeetups.framework.utils.extensions.view.gone
import com.example.programmingmeetups.framework.utils.extensions.view.show
import com.example.programmingmeetups.framework.utils.frameworkrequests.FrameworkContentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class AuthFragment(
    private val contentManager: FrameworkContentManager,
    var authViewModel: AuthViewModel? = null
) :
    Fragment(R.layout.auth_fragment), TabLayout.OnTabSelectedListener {

    private lateinit var binding: AuthFragmentBinding
    private lateinit var authAdapter: AuthAdapter
    lateinit var registerFragment: RegisterFragment
    lateinit var loginFragment: LoginFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AuthFragmentBinding.bind(view)
        setViewModel()
        setAdapter()
        setViewPager()
        setTabLayout()
        checkStoragePermissions()
    }

    private fun setViewModel() {
        authViewModel =
            authViewModel ?: ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    private fun setAdapter() {
        registerFragment =
            RegisterFragment(::navigateToMapFragment, contentManager, authViewModel!!)
        loginFragment = LoginFragment(::navigateToMapFragment, authViewModel!!)
        authAdapter = AuthAdapter(
            listOf(
                registerFragment,
                loginFragment
            ),
            childFragmentManager,
            lifecycle
        )
    }

    private fun setViewPager() {
        binding.authViewPager.apply {
            adapter = authAdapter
        }
    }

    private fun setTabLayout() {
        TabLayoutMediator(binding.authTabLayout, binding.authViewPager) { tab, position ->
            tab.text = authAdapter.fragments[position].getName()
        }.attach()
        binding.authTabLayout.addOnTabSelectedListener(this)
    }

    private fun checkStoragePermissions() {
        if (!EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            EasyPermissions.requestPermissions(
                this,
                "",
                STORAGE_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.run {
            authAdapter.createFragment(this.position)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}
    override fun onTabReselected(tab: TabLayout.Tab?) {}

    private fun showLoading() {
        binding.pbAuth.show()
    }

    private fun hideLayout() {
        binding.apply {
            authTabLayout.gone()
            authViewPager.gone()
        }
    }

    private fun navigateToMapFragment() {
        hideLayout()
        showLoading()
        AuthFragmentDirections.actionAuthFragmentToMapFragment().run {
            findNavController().navigate(this)
        }
    }
}