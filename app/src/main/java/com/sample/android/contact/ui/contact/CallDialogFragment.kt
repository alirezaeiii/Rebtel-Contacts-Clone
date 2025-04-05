package com.sample.android.contact.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sample.android.contact.databinding.FragmentDialogCallBinding
import com.sample.android.contact.util.ContactUtils

class CallDialogFragment(
    private val contactName: String,
    private val phoneNumber: String,
    private val flagResId: Int
) : BottomSheetDialogFragment() {

    private var _binding: FragmentDialogCallBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactName.text = contactName
        binding.phoneNumber.text = phoneNumber
        binding.flagItem.setImageResource(flagResId)
        binding.callBtn.setOnClickListener {
            ContactUtils.call(context, phoneNumber)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}