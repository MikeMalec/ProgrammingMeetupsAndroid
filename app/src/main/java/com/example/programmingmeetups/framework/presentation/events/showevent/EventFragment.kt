package com.example.programmingmeetups.framework.presentation.events.showevent

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.databinding.EventFragmentBinding
import com.example.programmingmeetups.framework.presentation.UIController
import com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog.ParticipantsDialog
import com.example.programmingmeetups.framework.utils.*
import com.example.programmingmeetups.framework.utils.extensions.view.hide
import com.example.programmingmeetups.framework.utils.extensions.view.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class EventFragment(var eventViewModel: EventViewModel? = null) :
    Fragment(R.layout.event_fragment) {

    private var binding: EventFragmentBinding? = null

    private val args: EventFragmentArgs by navArgs()

    private val programmingEvent: ProgrammingEvent
        get() = args.event

    private lateinit var menu: Menu

    private lateinit var uiController: UIController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setSharedViewsAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.event_menu, menu)
        menu.findItem(R.id.participants).actionView.setOnClickListener {
            showParticipantsDialog()
        }
        observeEvent()
        observeAmountOfParticipants()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.showEventComments -> showEventComments()
            R.id.editEvent -> navigateToEditFragment()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showEventComments() {
        EventFragmentDirections.actionEventFragmentToEventCommentsFragment(programmingEvent).run {
            findNavController().navigate(this)
        }
    }

    private fun navigateToEditFragment() {
        EventFragmentDirections.actionEventFragmentToUpdateEventFragment(programmingEvent).run {
            findNavController().navigate(this)
        }
    }

    private fun setSharedViewsAnimation() {
        val moveAnimation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        moveAnimation.duration = 300
        sharedElementEnterTransition = moveAnimation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setBinding(view)
        observeResponseError()
        observeLoading()
        setMainButtonClick()
    }

    private fun setBinding(view: View) {
        binding = EventFragmentBinding.bind(view)
        binding!!.lifecycleOwner = this
        binding!!.eventViewModel = eventViewModel!!
    }

    private fun setMainButtonClick() {
        binding!!.btnEventAction.setOnClickListener {
            when (eventViewModel!!.eventActionButtonState.value) {
                is EventAction.EditEvent -> navigateToEditFragment()
                is EventAction.LeaveEvent -> eventViewModel!!.leaveEvent()
                is EventAction.JoinEvent -> eventViewModel!!.joinEvent()
                else -> {
                }
            }
        }
    }

    private fun observeEvent() {
        lifecycleScope.launchWhenStarted {
            eventViewModel!!.event.observe(viewLifecycleOwner, Observer {
                hideLoading()
                runMenuAction {
                    setEditIcon()
                }
            })
        }
    }

    private fun observeAmountOfParticipants() {
        lifecycleScope.launchWhenStarted {
            eventViewModel?.amountOfParticipants?.observe(viewLifecycleOwner, Observer {
                setAmountOfParticipants(it)
            })
        }
    }

    private fun setViewModel() {
        eventViewModel =
            eventViewModel ?: ViewModelProvider(this).get(EventViewModel::class.java)
        lifecycleScope.launchWhenStarted {
            eventViewModel!!.setEvent(programmingEvent)
            eventViewModel!!.fetchAmountOfParticipants()
            eventViewModel!!.checkIfUserIsParticipant()
        }
    }

    private fun observeResponseError() {
        lifecycleScope.launchWhenStarted {
            eventViewModel!!.responseError.observe(viewLifecycleOwner, Observer {
                hideLoading()
                it.getContent()
                    ?.also { err -> runUiControllerAction { uiController.showShortToast(err) } }
            })
        }
    }

    private fun observeLoading() {
        lifecycleScope.launchWhenStarted {
            eventViewModel!!.loading.observe(viewLifecycleOwner, Observer {
                when (it.getContent()) {
                    true -> showLoading()
                    else -> hideLoading()
                }
            })
        }
    }

    private fun showLoading() {
        binding!!.pbEventLoading.show()
    }

    private fun hideLoading() {
        binding!!.pbEventLoading.hide()
    }

    // Had to do it like this for UI tests
    private fun runMenuAction(action: () -> Unit) {
        if (::menu.isInitialized) {
            action()
        } else {
            lifecycleScope.launch {
                delay(150)
                if (::menu.isInitialized) {
                    action()
                }
            }
        }
    }

    private fun setEditIcon() {
        if (programmingEvent.organizer!!.id == eventViewModel!!.user!!.id) {
            val editIcon = menu.findItem(R.id.editEvent)
            editIcon.isVisible = true
        }
    }

    private fun showParticipantsDialog() {
        ParticipantsDialog(eventViewModel!!).show(
            requireFragmentManager(),
            PARTICIPANTS_DIALOG
        )
    }

    private fun setAmountOfParticipants(amount: Int) {
        val participantsActionView = menu.findItem(R.id.participants).actionView
        val cv = participantsActionView.findViewById<CardView>(R.id.cvAmountOfParticipants)
        val tv = participantsActionView.findViewById<TextView>(R.id.tvAmountOfParticipants)
        if (amount in 1..99) {
            cv.show()
            tv.text = amount.toString()
        } else if (amount > 99) {
            cv.show()
            tv.text = "99+"
        } else {
            cv.hide()
        }
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