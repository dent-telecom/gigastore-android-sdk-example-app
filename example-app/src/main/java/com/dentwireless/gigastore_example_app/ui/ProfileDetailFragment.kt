package com.dentwireless.gigastore_example_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.dentwireless.Gigastore
import com.dentwireless.gigastore_example_app.R
import com.dentwireless.gigastore_example_app.databinding.FragmentProfileDetailBinding
import com.dentwireless.gigastore_example_app.ui.adapter.EsimProfileView
import com.dentwireless.gigastore_sdk.models.GigastoreESIMProfile

class ProfileDetailFragment : BaseFragment() {

    companion object {
        fun newInstance(esimProfile: GigastoreESIMProfile): ProfileDetailFragment {
            return ProfileDetailFragment().apply {
                this.esimProfile = esimProfile
            }
        }
    }

    // region Properties

    private var esimProfile: GigastoreESIMProfile? = null

    private var binding: FragmentProfileDetailBinding? = null

    private val esimProfileView: EsimProfileView?
        get() = binding?.profileDetailEsimProfileView

    private val emptyView: View?
        get() = binding?.profileDetailEmptyPlaceholder

    private val progressView: ProgressBar?
        get() = binding?.profileDetailLoadingIndicator

    private var loading = false
        set(value) {
            field = value
            if (value) {
                progressView?.visibility = View.VISIBLE
            } else {
                progressView?.visibility = View.GONE
            }
        }

    // endregion

    // region Public API

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    // endregion

    // region Private API

    private fun setupViews() {
        if (esimProfile == null) {
            esimProfileView?.isVisible = false
            emptyView?.isVisible = true
        } else {
            esimProfileView?.isVisible = true
            emptyView?.isVisible = false

            esimProfileView?.setupWithEsimProfile(esimProfile, object : EsimProfileView.Listener {
                override fun onEsimInstallationSelected(profile: GigastoreESIMProfile?) {
                    handleEsimInstallation(profile)
                }
            })
        }
    }

    private fun handleEsimInstallation(profile: GigastoreESIMProfile?) {
        val currentActivity = activity ?: return
        val profileNotNull = profile ?: return
        loading = true

        Gigastore.installProfile(
            currentActivity,
            profileNotNull
        ) { installedProfile, installationError ->
            loading = false

            val currentContext = context
            if (installedProfile != null && currentContext != null) {
                Toast.makeText(
                    currentContext,
                    R.string.profile_installed_successfully,
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (installationError != null) {
                activeError = installationError
            }
        }
    }

    // endregion
}