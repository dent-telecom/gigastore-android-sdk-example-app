package com.dentwireless.gigastore_example_app.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dentwireless.gigastore_sdk.models.GigastoreESIMProfile

class AllProfilesRecyclerAdapter : RecyclerView.Adapter<AllProfilesRecyclerAdapter.ViewHolder>() {

    // region Types

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    // endregion

    // region Properties

    var profileSelectionListener: EsimProfileView.Listener? = null
    var items: List<GigastoreESIMProfile>? = null

    // endregion

    // region Public API

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = EsimProfileView(parent.context, null)

        view.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        val view = holder.view as? EsimProfileView ?: return
        view.setupWithEsimProfile(item, profileSelectionListener)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    // endregion
}