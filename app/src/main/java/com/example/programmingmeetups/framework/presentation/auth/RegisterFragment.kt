package com.example.programmingmeetups.framework.presentation.auth

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.util.Resource.Error
import com.example.programmingmeetups.business.domain.util.Resource.Loading
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.databinding.RegisterFragmentBinding
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RegisterRequest
import com.example.programmingmeetups.framework.presentation.UIController
import com.example.programmingmeetups.utils.REGISTER_FRAGMENT
import com.example.programmingmeetups.utils.SOMETHING_WENT_WRONG
import com.example.programmingmeetups.utils.extensions.view.clearBackground
import com.example.programmingmeetups.utils.extensions.view.clearImageTintList
import com.example.programmingmeetups.utils.extensions.view.hide
import com.example.programmingmeetups.utils.extensions.view.show
import com.example.programmingmeetups.utils.frameworkrequests.FrameworkContentManager
import java.lang.Exception

class RegisterFragment(
    private val contentManager: FrameworkContentManager,
    private val authViewModel: AuthViewModel
) :
    Fragment(R.layout.register_fragment), NamedFragment, View.OnClickListener {

    override fun getName(): String {
        return REGISTER_FRAGMENT
    }

    private lateinit var binding: RegisterFragmentBinding

    private lateinit var uiController: UIController

    var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterFragmentBinding.bind(view)
        setClicks()
        observeRegisterValidation()
        observeRegisterResponse()
        observeLoading()
    }

    private val getImage = registerForActivityResult(contentManager.PickImage()) { result: Uri? ->
        dispatchImageResult(result)
    }

    private fun dispatchImageResult(result: Uri?) {
        imageUri = result
        binding.userImageIv.clearBackground()
        binding.userImageIv.clearImageTintList()
        Glide.with(binding.userImageIv).load(result).into(binding.userImageIv)
    }

    private fun setClicks() {
        binding.registerBtn.setOnClickListener(this)
        binding.imageCard.setOnClickListener(this)
        binding.userImageIv.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.registerBtn -> authViewModel.attemptRegister(
                imageUri,
                registerRequest
            )
            R.id.imageCard, R.id.userImageIv -> requestPhoto()
        }
    }

    private fun requestPhoto() {
        getImage.launch("")
    }

    private val firstName: String
        get() = binding.etFirstName.text.toString()

    private val lastName: String
        get() = binding.etLastName.text.toString()

    private val email: String
        get() = binding.etRegisterEmail.text.toString()

    private val password: String
        get() = binding.etRegisterPassword.text.toString()

    private val registerRequest: RegisterRequest
        get() = RegisterRequest(firstName, lastName, email, password)

    private fun observeRegisterValidation() {
        lifecycleScope.launchWhenStarted {
            authViewModel.registerValidationMessage.observe(viewLifecycleOwner,
                Observer {
                    it.getContent()?.also { errMsg ->
                        runUiControllerAction { uiController.showShortToast(errMsg) }
                    }
                })
        }
    }

    private fun observeRegisterResponse() {
        lifecycleScope.launchWhenStarted {
            authViewModel.registerRequestResponse.observe(viewLifecycleOwner,
                Observer {
                    it.getContent().also { authResp ->
                        when (authResp) {
                            is Success -> {
                                hideLoading()
                            }
                            is Error -> {
                                hideLoading()
                                if (authResp.error != null) {
                                    runUiControllerAction { uiController.showShortToast(authResp.error) }
                                } else {
                                    runUiControllerAction {
                                        runUiControllerAction {
                                            uiController.showShortToast(
                                                SOMETHING_WENT_WRONG
                                            )
                                        }
                                    }
                                }
                            }
                            else -> {
                            }
                        }
                    }
                })
        }
    }

    private fun observeLoading() {
        lifecycleScope.launchWhenStarted {
            authViewModel.registerLoading.observe(viewLifecycleOwner, Observer {
                val value = it.getContent()
                if (value != null && value == true) {
                    showLoading()
                } else {
                    hideLoading()
                }
            })
        }
    }

    private fun showLoading() {
        binding.pbRegister.show()
    }

    private fun hideLoading() {
        binding.pbRegister.hide()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUIController()
    }

    private fun runUiControllerAction(action: () -> Unit) {
        if (::uiController.isInitialized) action()
    }

    private fun setUIController() {
        try {
            uiController = context as UIController
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}