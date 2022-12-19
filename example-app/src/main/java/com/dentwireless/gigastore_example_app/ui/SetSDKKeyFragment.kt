package com.dentwireless.gigastore_example_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dentwireless.Gigastore
import com.dentwireless.gigastore_example_app.databinding.FragmentSetSdkKeyBinding

class SetSDKKeyFragment : BaseFragment() {

    // region Types

    interface Listener {
        fun sdkKeyWasSet()
    }

    // endregion

    // region Properties

    var listener: Listener? = null
    private var binding: FragmentSetSdkKeyBinding? = null
    private var sdkKey: String = ""

    private val sdkKeyEditText: EditText?
        get() = binding?.editTextSdkKey

    private val sdkSetupButton: Button?
        get() = binding?.buttonSetSdkKey

    // endretion

    // region Public API

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetSdkKeyBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    // endregion

    // region Private API

    private fun setupViews() {
        setupSdkKeyViews()
    }

    private fun setupSdkKeyViews() {
        sdkKeyEditText?.setText(sdkKey)
        sdkSetupButton?.setOnClickListener {
            val sdkKey = sdkKeyEditText?.text?.toString()
            if (sdkKey.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "please set the sdk key", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            Gigastore.load(context, sdkKey)
            listener?.sdkKeyWasSet()
        }
    }

    // endregion
}