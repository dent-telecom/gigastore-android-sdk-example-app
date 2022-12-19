package com.dentwireless.gigastore_example_app.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dentwireless.gigastore_example_app.R

abstract class BaseActivity: AppCompatActivity() {

    // region Properties

    open val showInitialBackButton: Boolean = false

    // endregion

    // region Public API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_container)
        setupActionBarBackButton(showInitialBackButton)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            setupActionBarBackButton(showInitialBackButton)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    protected fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(0, 0)
            .replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack("Example app")
            setupActionBarBackButton(true)
        }

        transaction.commit()
    }

    // endregion

    // region Private API

    private fun setupActionBarBackButton(showBackButton: Boolean) {
        if (supportActionBar == null) {
            return
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(showBackButton)
        supportActionBar?.setDisplayShowHomeEnabled(showBackButton)
    }

    // endregion
}