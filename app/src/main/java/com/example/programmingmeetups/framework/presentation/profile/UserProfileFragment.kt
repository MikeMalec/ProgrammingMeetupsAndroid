package com.example.programmingmeetups.framework.presentation.profile

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.*
import com.example.programmingmeetups.databinding.UserProfileFragmentBinding
import com.example.programmingmeetups.framework.presentation.events.common.BaseFragment
import com.example.programmingmeetups.utils.PROFILE_SUCCESSFULLY_UPDATED
import com.example.programmingmeetups.utils.extensions.view.hide
import com.example.programmingmeetups.utils.extensions.view.show
import com.example.programmingmeetups.utils.frameworkrequests.FrameworkContentManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment(
    private val contentManager: FrameworkContentManager,
    var userProfileViewModel: UserProfileViewModel? = null
) :
    BaseFragment(R.layout.user_profile_fragment), View.OnClickListener {

    private var binding: UserProfileFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setHasOptionsMenu(true)
        setBinding(view)
        observeUpdateResponse()
        setClicks()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setViewModel() {
        userProfileViewModel = userProfileViewModel ?: ViewModelProvider(requireActivity()).get(
            UserProfileViewModel::class.java
        )
    }

    private fun setBinding(view: View): View {
        binding = UserProfileFragmentBinding.bind(view)
        binding!!.lifecycleOwner = this
        binding!!.userProfileViewModel = userProfileViewModel
        return binding!!.root
    }

    private fun observeUpdateResponse() {
        lifecycleScope.launchWhenStarted {
            userProfileViewModel?.updateResponse?.observe(viewLifecycleOwner, Observer {
                val value = it.getContent()
                when (value) {
                    is Success -> {
                        hideLoading()
                        runUiControllerAction {
                            uiController.showShortSnackbar(
                                PROFILE_SUCCESSFULLY_UPDATED
                            )
                        }
                    }
                    is Error -> {
                        hideLoading()
                        value.error?.also { err ->
                            runUiControllerAction { uiController.showShortToast(err) }
                        }
                    }
                    is Loading -> {
                        showLoading()
                    }
                    else -> {
                    }
                }
            })
        }
    }

    private fun showLoading() {
        binding!!.pbUpdateProfile.show()
    }

    private fun hideLoading() {
        binding!!.pbUpdateProfile.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setClicks() {
        binding!!.apply {
            btnUpdateProfile.setOnClickListener(this@UserProfileFragment)
            ivUserImage.setOnClickListener(this@UserProfileFragment)

        }
    }

    private val description: String
        get() = binding!!.etUserDescription.text.toString()

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnUpdateProfile -> userProfileViewModel?.updateProfile(description)
            R.id.ivUserImage -> requestImage()
        }
    }

    private val getImage = registerForActivityResult(contentManager.PickImage()) { result: Uri? ->
        result?.also {
            Glide.with(binding!!.ivUserImage).load(it).into(binding!!.ivUserImage)
            userProfileViewModel!!.imageUri = it
        }
    }

    private fun requestImage() {
        getImage.launch("")
    }
}