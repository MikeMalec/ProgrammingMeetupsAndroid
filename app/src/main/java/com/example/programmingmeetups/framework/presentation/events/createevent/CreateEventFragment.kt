package com.example.programmingmeetups.framework.presentation.events.createevent

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.*
import com.example.programmingmeetups.databinding.CreateEventFragmentBinding
import com.example.programmingmeetups.framework.presentation.UIController
import com.example.programmingmeetups.utils.DATE_BOTTOM_DIALOG
import com.example.programmingmeetups.utils.PROGRAMMING_EVENT_CREATED
import com.example.programmingmeetups.utils.SOMETHING_WENT_WRONG
import com.example.programmingmeetups.utils.TAG_BOTTOM_DIALOG
import com.example.programmingmeetups.utils.extensions.view.hide
import com.example.programmingmeetups.utils.extensions.view.show
import com.example.programmingmeetups.utils.frameworkrequests.FrameworkContentManager
import com.example.programmingmeetups.utils.localization.LocalizationDispatcher
import com.example.programmingmeetups.utils.localization.LocalizationDispatcherInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.create_event_fragment.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class CreateEventFragment(
    private val contentManager: FrameworkContentManager,
    var createEventViewModel: CreateEventViewModel? = null,
    private val localizationDispatcherInterface: LocalizationDispatcherInterface
) :
    Fragment(R.layout.create_event_fragment), View.OnClickListener {

    private var binding: CreateEventFragmentBinding? = null

    @Inject
    lateinit var eventTextWatcher: EventTextWatcher

    private val args: CreateEventFragmentArgs by navArgs()

    private lateinit var uiController: UIController

    private val latitude: Double get() = args.coordinates.latitude
    private val longitude: Double get() = args.coordinates.longitude

    private val getEventImage =
        registerForActivityResult(contentManager.PickImage()) { result: Uri? ->
            result?.run {
                createEventViewModel?.setImage(result)
            }
        }

    private fun requestEventImage() = getEventImage.launch("")

    private val getEventIcon =
        registerForActivityResult(contentManager.PickImage()) { result: Uri? ->
            result?.run {
                createEventViewModel?.setIcon(result)
            }
        }

    private fun requestEventIcon() = getEventIcon.launch("")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setBinding(view)
        setClicks()
        setTextListener()
        setUIController()
        setCoordinates()
        setAddress()
        observeValidationMessage()
        observeCreateEventRequestResponse()
    }

    private fun setViewModel() {
        createEventViewModel = createEventViewModel ?: ViewModelProvider(requireActivity()).get(
            CreateEventViewModel::class.java
        )
        eventTextWatcher.createEventViewModel = createEventViewModel!!
    }

    private fun setTextListener() {
        binding!!.etEventDescription.addTextChangedListener(eventTextWatcher)
    }

    private fun setClicks() {
        binding.run {
            clAddDate.setOnClickListener(this@CreateEventFragment)
            ivAddTag.setOnClickListener(this@CreateEventFragment)
            clAddEventImage.setOnClickListener(this@CreateEventFragment)
            clAddEventIcon.setOnClickListener(this@CreateEventFragment)
            btnCreateEvent.setOnClickListener(this@CreateEventFragment)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.clAddDate -> showDateDialog()
            R.id.ivAddTag -> showAddTagDialog()
            R.id.clAddEventImage -> requestEventImage()
            R.id.clAddEventIcon -> requestEventIcon()
            R.id.btnCreateEvent -> createEventViewModel?.attemptToCreateEvent()
        }
    }

    private fun setBinding(view: View) {
        binding = CreateEventFragmentBinding.bind(view)
        binding!!.lifecycleOwner = viewLifecycleOwner
        binding!!.createEventViewModel = createEventViewModel!!
    }

    private fun showDateDialog() {
        DateBottomDialog(createEventViewModel!!).show(requireFragmentManager(), DATE_BOTTOM_DIALOG)
    }

    private fun showAddTagDialog() {
        TagBottomDialog(createEventViewModel!!).show(requireFragmentManager(), TAG_BOTTOM_DIALOG)
    }

    private fun setCoordinates() {
        createEventViewModel!!.setCoordinates(latitude, longitude)
    }

    private fun setAddress() {
        lifecycleScope.launchWhenStarted {
            withContext(IO) {
                val localization = localizationDispatcherInterface.getAddress(latitude, longitude)
                if (localization != null) {
                    createEventViewModel!!.setAddress(localization)
                } else {
                    withContext(Main) {
                        navigateBackWithErrorMessage()
                    }
                }
            }
        }
    }

    private fun observeValidationMessage() {
        lifecycleScope.launchWhenStarted {
            createEventViewModel!!.validationMessage.observe(
                viewLifecycleOwner,
                Observer { message ->
                    message.getContent()
                        ?.also { runUiControllerAction { uiController.showShortToast(it) } }
                })
        }
    }

    private fun observeCreateEventRequestResponse() {
        lifecycleScope.launchWhenStarted {
            createEventViewModel!!.createEventRequestResponse.observe(
                viewLifecycleOwner, Observer {
                    it.getContent().run {
                        when (this) {
                            is Success -> {
                                hideLoading()
                                showCreatedMessageAndNavigateBack()
                            }
                            is Error -> error?.also { errMsg ->
                                hideLoading()
                                runUiControllerAction {
                                    uiController.showShortToast(
                                        errMsg
                                    )
                                }
                            }
                            is Loading -> showLoading()
                            else -> {
                            }
                        }
                    }
                }
            )
        }
    }

    private fun navigateBackWithErrorMessage() {
        runUiControllerAction { uiController.showShortToast(SOMETHING_WENT_WRONG) }
        findNavController().popBackStack()
    }

    private fun showCreatedMessageAndNavigateBack() {
        runUiControllerAction { uiController.showShortSnackbar(PROGRAMMING_EVENT_CREATED) }
        findNavController().popBackStack()
    }

    private fun showLoading() {
        binding?.pbCreateEvent?.show()
    }

    private fun hideLoading() {
        binding?.pbCreateEvent?.hide()
    }

    private fun runUiControllerAction(action: () -> Unit) {
        if (::uiController.isInitialized) action()
    }

    private fun setUIController() {
        try {
            uiController = activity as UIController
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUIController()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}