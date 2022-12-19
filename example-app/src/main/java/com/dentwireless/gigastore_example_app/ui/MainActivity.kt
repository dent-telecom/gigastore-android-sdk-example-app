package com.dentwireless.gigastore_example_app.ui

import android.os.Bundle
import com.dentwireless.gigastore_example_app.R

class MainActivity : BaseActivity() {

    // region Properties

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSdkKeyFragment()
    }

    private fun setupSdkKeyFragment() {
        val fragment = SetSDKKeyFragment()

        fragment.listener = object: SetSDKKeyFragment.Listener {
            override fun sdkKeyWasSet() {
                setupAndShowMainFragment()
            }
        }

        showFragment(fragment, false)
    }

    private fun setupAndShowMainFragment() {
        val fragment = MainFragment()

        fragment.openAllItemsFragmentCompletion = {
            val allProfilesFragment = AllProfilesFragment()
            showFragment(allProfilesFragment)
        }

        showFragment(fragment)
    }
}