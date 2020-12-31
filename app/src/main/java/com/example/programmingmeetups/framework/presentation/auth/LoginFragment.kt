package com.example.programmingmeetups.framework.presentation.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.util.Resource.Error
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.databinding.LoginFragmentBinding
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.presentation.UIController
import com.example.programmingmeetups.framework.utils.LOGIN_FRAGMENT
import com.example.programmingmeetups.framework.utils.SOMETHING_WENT_WRONG
import com.example.programmingmeetups.framework.utils.extensions.view.hide
import com.example.programmingmeetups.framework.utils.extensions.view.show
import java.lang.Exception

class LoginFragment(
    private val navigateToMapFragment: () -> Unit,
    private val authViewModel: AuthViewModel
) : Fragment(R.layout.login_fragment),
    NamedFragment, View.OnClickListener {

    override fun getName(): String {
        return LOGIN_FRAGMENT
    }

    private lateinit var binding: LoginFragmentBinding

    private lateinit var uiController: UIController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginFragmentBinding.bind(view)
        setClicks()
        observeLoginValidation()
        observeLoginResponse()
        observeLoading()
    }

    private fun setClicks() {
        binding.loginBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.loginBtn -> authViewModel.attemptLogin(loginRequest)
        }
    }

    private val email: String
        get() = binding.etLoginEmail.text.toString()

    private val password: String
        get() = binding.etLoginPassword.text.toString()

    private val loginRequest: LoginRequest
        get() = LoginRequest(email, password)

    private fun observeLoginValidation() {
        lifecycleScope.launchWhenStarted {
            authViewModel.loginValidationMessage.observe(viewLifecycleOwner, Observer {
                it.getContent()?.also { errMsg ->
                    runUiControllerAction { uiController.showShortToast(errMsg) }
                }
            })
        }
    }

    private fun observeLoginResponse() {
        lifecycleScope.launchWhenStarted {
            authViewModel.loginRequestResponse.observe(viewLifecycleOwner, Observer {
                it.getContent().also { authResp ->
                    when (authResp) {
                        is Success -> {
                            navigateToMapFragment()
                            hideLoading()
                        }
                        is Error -> {
                            hideLoading()
                            if (authResp.error != null) {
                                runUiControllerAction { uiController.showShortToast(authResp.error) }
                            } else {
                                runUiControllerAction {
                                    uiController.showShortToast(
                                        SOMETHING_WENT_WRONG
                                    )
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
            authViewModel.loginLoading.observe(viewLifecycleOwner, Observer {
                val content = it.getContent()
                if (content != null && content == true) {
                    showLoading()
                } else {
                    hideLoading()
                }
            })
        }
    }

    private fun showLoading() {
        binding.pbLogin.show()
    }

    private fun hideLoading() {
        binding.pbLogin.hide()
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