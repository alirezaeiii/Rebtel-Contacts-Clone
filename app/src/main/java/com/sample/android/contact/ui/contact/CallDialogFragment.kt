package com.sample.android.contact.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sample.android.contact.databinding.FragmentDialogCallBinding
import com.sample.android.contact.util.ContactUtils.call

class CallDialogFragment: BottomSheetDialogFragment() {

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
        val phoneNumber = arguments?.getString(PHONE_NUMBER)
        binding.contactName.text = arguments?.getString(CONTACT_NAME)
        binding.phoneNumber.text = phoneNumber
        arguments?.getInt(FLAG_RES_ID)?.let { binding.flagItem.setImageResource(it) }
        binding.callBtn.setOnClickListener {
            call(context, phoneNumber)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(contactName: String,
                        phoneNumber: String,
                        flagResId: Int): CallDialogFragment {
            val fragment = CallDialogFragment()
            val args = Bundle()
            args.putString(CONTACT_NAME, contactName)
            args.putString(PHONE_NUMBER, phoneNumber)
            args.putInt(FLAG_RES_ID, flagResId)
            fragment.arguments = args
            return fragment
        }
        private const val CONTACT_NAME = "contact_name"
        private const val PHONE_NUMBER = "phone_number"
        private const val FLAG_RES_ID = "flag_res_id"
    }
}