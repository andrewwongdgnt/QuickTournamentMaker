package com.dgnt.quickTournamentMaker.ui.main.management

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.GroupDeleteFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class GroupDeleteDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: GroupDeleteViewModelFactory by instance()
    private val binding by viewBinding(GroupDeleteFragmentBinding::inflate)
    private lateinit var viewModel: GroupDeleteViewModel

    companion object {

        const val TAG = "GroupDeleteDialogFragment"

        private const val KEY_SELECTED_GROUPS = "KEY_SELECTED_GROUPS"
        private const val KEY_GROUPS = "KEY_GROUPS"

        fun newInstance(selectedGroups: List<Group>, groups: List<Group>) =
            GroupDeleteDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_SELECTED_GROUPS, ArrayList<Group>(selectedGroups))
                    putParcelableArrayList(KEY_GROUPS, ArrayList<Group>(groups))
                }
            }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->

            viewModel = ViewModelProvider(this, viewModelFactory)[GroupDeleteViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.messageEvent.observe(this) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    dismiss()
                }
            }

            val selectedGroups = arguments?.getParcelableArrayList<Group>(KEY_SELECTED_GROUPS)!!
            val groups = arguments?.getParcelableArrayList<Group>(KEY_GROUPS)!!
            val visibleGroups = groups.subtract(selectedGroups)
            viewModel.setData(visibleGroups.toList().sorted())
            binding.groupDeleteMsgTv.text = getString(R.string.deleteGroupMsg, selectedGroups.size)
            binding.spinnerGroup.visibility = if (visibleGroups.isEmpty()) View.GONE else View.VISIBLE

            MaterialAlertDialogBuilder(activity, R.style.MyDialogTheme)
                .setTitle(getString(R.string.deletingGroups, selectedGroups.size))
                .setView(binding.root)
                .setPositiveButton(R.string.deleteWithPlayers) { _, _ ->
                    viewModel.deleteWithPersons(selectedGroups.map { it.toEntity() }, getString(R.string.deleteGroupSuccessfulMsg, selectedGroups.size))
                }
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.deleteThenMovePlayers) { _, _ ->
                    viewModel.deleteThenMove(selectedGroups.map { it.toEntity() }, getString(R.string.deleteGroupSuccessfulMsg, selectedGroups.size))
                }.create()
                .apply {
                    setOnShowListener { _ ->
                        getButton(AlertDialog.BUTTON_NEUTRAL).isEnabled = visibleGroups.isNotEmpty()
                    }
                }
        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

}