package com.example.programmingmeetups.framework.presentation.events.updateevent

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.programmingmeetups.R
import com.example.programmingmeetups.databinding.UpdateEventFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateEventFragment : Fragment(R.layout.update_event_fragment) {
    private val updateEventViewModel: UpdateEventViewModel by viewModels()

    private lateinit var binding: UpdateEventFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UpdateEventFragmentBinding.bind(view)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_event_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.applyEventChanges -> {
            }
            R.id.deleteEvent -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}