package com.example.programmingmeetups.framework.presentation.events.createevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.programmingmeetups.R
import com.example.programmingmeetups.databinding.AddTagDialogBinding
import com.example.programmingmeetups.framework.presentation.events.common.EventCrudViewModel
import com.example.programmingmeetups.framework.utils.ENTER_TAG
import com.example.programmingmeetups.framework.utils.extensions.view.shortToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TagBottomDialog(private val createEventViewModel: EventCrudViewModel) :
    BottomSheetDialogFragment() {

    private lateinit var binding: AddTagDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddTagDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()
        binding.run {
            cancelTagBtn.setOnClickListener { dismiss() }
            createTagBtn.setOnClickListener {
                if (etTag.text!!.isEmpty()) {
                    shortToast(ENTER_TAG)
                } else {
                    createEventViewModel.addTag(etTag.text.toString())
                    dismiss()
                }
            }
        }
    }

    private fun showKeyboard() {
        binding.etTag.requestFocus()
    }
}