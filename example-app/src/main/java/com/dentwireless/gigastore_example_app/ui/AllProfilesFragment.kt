package com.dentwireless.gigastore_example_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dentwireless.gigastore_example_app.databinding.FragmentAllProfilesBinding
import com.dentwireless.gigastore_example_app.ui.adapter.AllProfilesRecyclerAdapter
import com.dentwireless.Gigastore
import com.dentwireless.gigastore_example_app.R
import com.dentwireless.gigastore_example_app.ui.adapter.EsimProfileView
import com.dentwireless.gigastore_sdk.models.GigastoreESIMProfile

class AllProfilesFragment : BaseFragment() {

    // region Properties

    private var binding: FragmentAllProfilesBinding? = null

    private val allProfilesRecyclerView: RecyclerView?
        get() = binding?.allProfilesRecyclerView

    private val emptyPlaceholder: TextView?
        get() = binding?.allProfilesEmptyPlaceholder

    private val progressView: ProgressBar?
        get() = binding?.allProfilesLoadingIndicator

    private val adapter by lazy {
        val retVal = AllProfilesRecyclerAdapter()
        retVal.profileSelectionListener = object : EsimProfileView.Listener {
            override fun onEsimInstallationSelected(profile: GigastoreESIMProfile?) {
                this@AllProfilesFragment.handleEsimInstallation(profile)
            }
        }

        retVal
    }

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
        binding = FragmentAllProfilesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        refreshGigastoreProfiles()
    }

    // endregion

    // region Private API

    private fun setupViews() {
        allProfilesRecyclerView?.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        allProfilesRecyclerView?.adapter = adapter

        refreshGigastoreProfiles()
    }

    private fun refreshGigastoreProfiles() {
        val currentContext = context ?: return

        loading = true

        Gigastore.getAllProfiles(currentContext) { profiles, error ->
            loading = false

            adapter.items = profiles
            adapter.notifyDataSetChanged()
            changeEmptyViewVisibility()
        }
    }

    private fun changeEmptyViewVisibility() {
        if (adapter.itemCount > 0) {
            emptyPlaceholder?.visibility = View.GONE
        } else {
            emptyPlaceholder?.visibility = View.VISIBLE
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