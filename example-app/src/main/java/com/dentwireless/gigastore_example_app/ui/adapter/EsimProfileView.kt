package com.dentwireless.gigastore_example_app.ui.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.dentwireless.gigastore_example_app.R
import com.dentwireless.gigastore_example_app.databinding.ViewProfileItemBinding
import com.dentwireless.gigastore_sdk.models.GigastoreESIMProfile

class EsimProfileView : ConstraintLayout {

    // region Types

    interface Listener {
        fun onEsimInstallationSelected(profile: GigastoreESIMProfile?)
    }

    // region Properties

    val binding: ViewProfileItemBinding

    var listener: Listener? = null

    val profileIdTextView: TextView
        get() = binding.profileIdTextView

    val imsiTextView: TextView
        get() = binding.profileImsiTextView

    val iccidTextView: TextView
        get() = binding.profileIccidTextView

    val stateTextView: TextView
        get() = binding.profileStateTextView

    val installButton: Button
        get() = binding.buttonInstallProfile

    // endregion

    // region Construction

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        View.inflate(context, R.layout.view_profile_item, this)

        binding = ViewProfileItemBinding.bind(this)
    }

    // endregion

    // region Public API

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        updateLayoutParams<MarginLayoutParams> {
            width = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    fun setupWithEsimProfile(profile: GigastoreESIMProfile?, listener: Listener?) {
        profileIdTextView.text = profile?.id
        imsiTextView.text = profile?.imsi
        iccidTextView.text = profile?.iccid
        stateTextView.text = profile?.state?.rawValue ?: "Unknown"

        installButton.setOnClickListener {
            if (profile?.state != GigastoreESIMProfile.State.Installed) {
                this.listener?.onEsimInstallationSelected(profile)
                installButton.isEnabled = false
            } else {
                Toast.makeText(context, R.string.profile_already_installed, Toast.LENGTH_SHORT).show()
            }
        }

        this.listener = listener
    }

    // endregion
}