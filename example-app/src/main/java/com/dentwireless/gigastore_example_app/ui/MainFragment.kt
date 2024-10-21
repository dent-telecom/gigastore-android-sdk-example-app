package com.dentwireless.gigastore_example_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.dentwireless.gigastore_example_app.BuildConfig
import com.dentwireless.gigastore_example_app.R
import com.dentwireless.gigastore_example_app.databinding.FragmentMainBinding
import com.dentwireless.Gigastore
import com.dentwireless.gigastore_sdk.models.GigastoreESIMProfile

typealias OpenAllItemsCompletion = () -> Unit
typealias ProfileDetailCompletion = (GigastoreESIMProfile) -> Unit

class MainFragment : BaseFragment() {

    // region Properties

    var openAllItemsFragmentCompletion: OpenAllItemsCompletion? = null
    var openProfileDetailFragmentCompletion: ProfileDetailCompletion? = null


    private var binding: FragmentMainBinding? = null

    private var itemValue: String = ""
    private var profileUID: String = ""
    private var metatagValue: String = "foo_tag"
    private var userTokenValue: String = "foo_token"

    private var profile: GigastoreESIMProfile? = null

    private val eSimCapabilityTextView: TextView?
        get() = binding?.textViewEsimCapability

    private val userTokenEditText: EditText?
        get() = binding?.editTextUserToken

    private val setUserTokenButton: Button?
        get() = binding?.buttonSetUserToken

    private val profileUIDTextField: EditText?
        get() = binding?.editTextProfileUid

    private val loadProfileButton: Button?
        get() = binding?.buttonLoadProfile

    private val inventoryItemEditText: EditText?
        get() = binding?.editTextInventoryItem

    private val metaTagEditText: EditText?
        get() = binding?.editTextMetaTag

    private val activateItemButton: Button?
        get() = binding?.buttonActivateItem

    private val loadAllProfilesButton: Button?
        get() = binding?.textViewLoadAllProfiles

    private val versionNumberTextView: TextView?
        get() = binding?.textViewVersionNumber

    private val progressBar: View?
        get() = binding?.mainFragmentProgressBar

    private var loading = false
        set(value) {
            field = value
            if (value) {
                progressBar?.visibility = View.VISIBLE
            } else {
                progressBar?.visibility = View.GONE
            }
        }

    // endretion

    // region Public API

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
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
        setupEsimCapabilityView()
        setupUserTokenViews()
        setupGetProfileViews()

        setupInventoryItemEditText()
        setupMetaTagEditText()

        setupActivateProfileButton()

        setupLoadAllProfilesButton()

        setupVersionView()
    }

    private fun setupEsimCapabilityView() {
        Gigastore.isEsimCapable { isCapable ->
            val summary = if (isCapable) "yes" else "no"
            eSimCapabilityTextView?.text = getString(R.string.esim_capability_template, summary)
        }
    }

    private fun setupUserTokenViews() {
        userTokenEditText?.setText(userTokenValue)
        setUserTokenButton?.setOnClickListener {
            val userToken = userTokenEditText?.text?.toString() ?: ""
            Gigastore.setUserToken(userToken)
        }
    }

    private fun setupGetProfileViews() {
        profileUIDTextField?.setText(profileUID)
        loadProfileButton?.setOnClickListener {
            val profileUid = profileUIDTextField?.text?.toString() ?: ""
            val currentContext = context ?: return@setOnClickListener

            loading = true

            Gigastore.getProfile(profileUid, currentContext)  { profile, error ->
                loading = false

                if (profile != null) {
                    this.profile = profile
                    Toast.makeText(
                        currentContext,
                        R.string.profile_loaded_successfully,
                        Toast.LENGTH_SHORT
                    ).show()

                    openProfileDetailFragmentCompletion?.invoke(profile)
                }

                if (error != null) {
                    activeError = error
                }
            }

        }
    }

    private fun setupInventoryItemEditText() {
        inventoryItemEditText?.setText(itemValue)
        inventoryItemEditText?.doAfterTextChanged { editableText ->
            val stringText = editableText?.toString()
            itemValue = stringText ?: ""
        }
    }

    private fun setupMetaTagEditText() {
        metaTagEditText?.setText(metatagValue)
        metaTagEditText?.doAfterTextChanged { editableText ->
            val stringText = editableText?.toString()
            metatagValue = stringText ?: ""
        }
    }

    private fun setupActivateProfileButton() {
        activateItemButton?.setOnClickListener {
            val currentContext = context ?: return@setOnClickListener

            loading = true

            Gigastore.activateItem(
                currentContext,
                inventoryItem = itemValue,
                metaTag = metatagValue
            ) { profile, error ->
                loading = false

                if (profile != null) {
                    this.profile = profile
                    Toast.makeText(
                        currentContext,
                        R.string.item_activated_successfully,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (error != null) {
                    activeError = error
                }
            }
        }
    }

    private fun setupLoadAllProfilesButton() {
        loadAllProfilesButton?.setOnClickListener {
            openAllItemsFragmentCompletion?.invoke()
        }
    }

    private fun setupVersionView() {
        val versionName = BuildConfig.VERSION_NAME
        val versionText = getString(R.string.version_template, versionName)

        versionNumberTextView?.text = versionText
    }

    // endregion
}