package com.example.programmingmeetups.framework.presentation.events.createevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.programmingmeetups.R
import com.example.programmingmeetups.databinding.DateDialogBinding
import com.example.programmingmeetups.framework.presentation.events.common.EventCrudViewModel
import com.example.programmingmeetups.framework.presentation.map.MapViewModel
import com.example.programmingmeetups.utils.CHOOSE_DATE_FROM_FUTURE
import com.example.programmingmeetups.utils.extensions.view.shortToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class DateBottomDialog(private val createEventViewModel: EventCrudViewModel) :
    BottomSheetDialogFragment(),
    View.OnClickListener {

    private lateinit var binding: DateDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DateDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClicks()
    }

    private fun setClicks() {
        binding.btnSetDate.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSetDate -> setDate()
        }
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        binding.run {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                calendar.set(
                    datePicker.year, datePicker.month, datePicker.dayOfMonth,
                    timePicker.hour, timePicker.minute, 0
                )
            }
            if (calendar.before(Calendar.getInstance())) {
                shortToast(CHOOSE_DATE_FROM_FUTURE)
            } else {
                createEventViewModel.setDate(calendar.timeInMillis)
                dismiss()
            }
        }
    }
}