package com.example.programmingmeetups.framework.presentation.events.updateevent

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.databinding.UpdateEventFragmentBinding
import com.example.programmingmeetups.framework.presentation.MainActivity
import com.example.programmingmeetups.framework.presentation.events.common.EventCrudFragment
import com.example.programmingmeetups.framework.presentation.events.common.EventCrudViewModel
import com.example.programmingmeetups.framework.presentation.events.createevent.EventTextWatcher
import com.example.programmingmeetups.utils.Cancel
import com.example.programmingmeetups.utils.DELETE
import com.example.programmingmeetups.utils.DELETE_EVENT
import com.example.programmingmeetups.utils.extensions.view.hide
import com.example.programmingmeetups.utils.extensions.view.show
import com.example.programmingmeetups.utils.frameworkrequests.FrameworkContentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.create_event_fragment.*
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class UpdateEventFragment(
    override val contentManager: FrameworkContentManager,
    var updateEventViewModel: UpdateEventViewModel? = null
) :
    EventCrudFragment(
        contentManager,
        R.layout.update_event_fragment
    ), View.OnClickListener {

    private var _binding: UpdateEventFragmentBinding? = null
    val binding: UpdateEventFragmentBinding
        get() = _binding!!

    private val args: UpdateEventFragmentArgs by navArgs()

    @Inject
    lateinit var eventTextWatcher: EventTextWatcher

    override fun getViewModel(): EventCrudViewModel = updateEventViewModel!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModelAndEvent()
        setBinding(view)
        setClicks()
        setHasOptionsMenu(true)
        setTextListener()
        observeValidationMessage()
        observeUpdateRequestResponse()
        observeDeleteRequestResponse()
        observeLoading()
    }

    private fun setViewModelAndEvent() {
        updateEventViewModel =
            updateEventViewModel ?: ViewModelProvider(this).get(UpdateEventViewModel::class.java)
        lifecycleScope.launchWhenStarted {
            delay(300)
            updateEventViewModel!!.programmingEvent = args.event.copy()
            updateEventViewModel!!.setEvent()
        }
    }

    private fun setBinding(view: View) {
        _binding = UpdateEventFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.updateEventViewModel = updateEventViewModel
    }

    private fun setClicks() {
        binding.run {
            clAddDate.setOnClickListener(this@UpdateEventFragment)
            ivAddTag.setOnClickListener(this@UpdateEventFragment)
            clAddEventImage.setOnClickListener(this@UpdateEventFragment)
            clAddEventIcon.setOnClickListener(this@UpdateEventFragment)
            btnUpdateEvent.setOnClickListener(this@UpdateEventFragment)
        }
    }

    private fun setTextListener() {
        eventTextWatcher.viewModel = updateEventViewModel!!
        binding.etEventDescription.addTextChangedListener(eventTextWatcher)
    }

    private fun observeValidationMessage() {
        lifecycleScope.launchWhenStarted {
            updateEventViewModel!!.validationMessage.observe(viewLifecycleOwner, Observer {
                runUiControllerAction {
                    it.getContent()?.also { uiController.showShortToast(it) }
                }
            })
        }
    }

    private fun observeUpdateRequestResponse() {
        lifecycleScope.launchWhenStarted {
            updateEventViewModel!!.updateRequestResponse.observe(viewLifecycleOwner, Observer {
                val value = it.getContent()
                when (value) {
                    is Resource.Success -> {
                        hideLoading()
                        value.data?.also { event -> setEventAndNavigateBack(event) }
                    }
                    is Resource.Error -> {
                        hideLoading()
                        runUiControllerAction {
                            value.error?.also { err ->
                                uiController.showShortToast(err)
                            }
                        }
                    }
                    else -> {
                    }
                }
            })
        }
    }

    private fun observeDeleteRequestResponse() {
        lifecycleScope.launchWhenStarted {
            updateEventViewModel!!.deleteRequestResponse.observe(viewLifecycleOwner, Observer {
                hideLoading()
                val value = it.getContent()
                when (value) {
                    is Resource.Success -> navigateToUserEvents()
                    is Resource.Error -> {
                        runUiControllerAction {
                            value.error?.also { err ->
                                uiController.showShortToast(err)
                            }
                        }
                    }
                    else -> {
                    }
                }
            })
        }
    }

    private fun observeLoading() {
        lifecycleScope.launchWhenStarted {
            updateEventViewModel!!.loading.observe(viewLifecycleOwner, Observer {
                when (it.getContent()) {
                    true -> showLoading()
                    else -> hideLoading()
                }
            })
        }
    }

    private fun showLoading() {
        binding.pbUpdateEvent.show()
    }

    private fun hideLoading() {
        binding.pbUpdateEvent.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_event_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.applyEventChanges -> updateEventViewModel!!.attemptToUpdateEvent()
            R.id.deleteEvent -> showDeleteDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.AlertDialogTheme
        ).setTitle(DELETE_EVENT)
            .setIcon(R.drawable.ic_baseline_delete_forever_24)
            .setPositiveButton(DELETE) { dialogInterface, i -> updateEventViewModel!!.deleteEvent() }
            .setNegativeButton(Cancel) { dialogInterface, i -> }
            .show()
    }

    private fun navigateToUserEvents() {
        (activity as MainActivity).navigateToUserEvents()
    }

    private fun setEventAndNavigateBack(event: ProgrammingEvent) {
        args.event.apply {
            happensAt = event.happensAt
            tags = event.tags
            image = event.image
            icon = event.icon
            description = event.description
        }
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.clAddDate -> showDateDialog()
            R.id.ivAddTag -> showAddTagDialog()
            R.id.clAddEventImage -> requestEventImage()
            R.id.clAddEventIcon -> requestEventIcon()
            R.id.btnUpdateEvent -> updateEventViewModel!!.attemptToUpdateEvent()
        }
    }
}