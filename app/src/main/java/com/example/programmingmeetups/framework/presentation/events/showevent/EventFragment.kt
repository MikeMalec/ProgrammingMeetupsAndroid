package com.example.programmingmeetups.framework.presentation.events.showevent

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.databinding.EventFragmentBinding
import com.example.programmingmeetups.framework.presentation.UIController
import com.example.programmingmeetups.framework.presentation.events.common.BaseFragment
import com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog.ParticipantsDialog
import com.example.programmingmeetups.framework.utils.*
import com.example.programmingmeetups.framework.utils.extensions.view.gone
import com.example.programmingmeetups.framework.utils.extensions.view.hide
import com.example.programmingmeetups.framework.utils.extensions.view.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class EventFragment(var eventViewModel: EventViewModel? = null) :
    BaseFragment(R.layout.event_fragment) {

    private var binding: EventFragmentBinding? = null

    private val args: EventFragmentArgs by navArgs()

    private val programmingEvent: ProgrammingEvent
        get() = args.event

    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setViewModel()
        binding = EventFragmentBinding.inflate(inflater, container, false)
        setShimmer()
        setBinding()
        observeResponseError()
        observeLoading()
        setMainButtonClick()
        return binding!!.root
    }

    private fun setBinding() {
        binding!!.lifecycleOwner = this
        binding!!.eventViewModel = eventViewModel!!
    }

    private fun showShimmer() {
        binding!!.eventShimmer.startShimmer()
    }

    private fun hideShimmer() {
        binding!!.apply {
            eventShimmer.stopShimmer()
            eventShimmer.gone()
            clEventFragment.show()
        }
    }

    private fun setShimmer() {
        if (eventViewModel!!.didShimmer) {
            hideShimmer()
        } else {
            eventViewModel!!.didShimmer = true
            showShimmer()
            lifecycleScope.launchWhenStarted {
                delay(600)
                hideShimmer()
            }
        }
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
        ParticipantsDialog(eventViewModel!!, ::navigateToUserProfile).show(
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

    private fun navigateToUserProfile(user: User) {
        EventFragmentDirections.actionEventFragmentToEventUserProfileFragment(user).run {
            findNavController().navigate(this)
        }
    }

    override fun onStart() {
        super.onStart()
        binding?.eventShimmer?.startShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}