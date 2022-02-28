package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.AddChoiceFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person


class AddChoiceDialogFragment : DialogFragment() {


    companion object {

        const val TAG = "AddChoiceDialogFragment"

        private const val KEY_GROUPS = "KEY_GROUPS"

        fun newInstance(group: List<Group>): AddChoiceDialogFragment =
            AddChoiceDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_GROUPS, ArrayList<Group>(group))
                }
            }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        activity?.let { activity ->
            val binding = AddChoiceFragmentBinding.inflate(activity.layoutInflater)
            AlertDialog.Builder(activity)
                .setTitle(getString(R.string.addWhich))
                .setView(binding.root)
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .apply {


                    setOnShowListener {
                        binding.personBtn.setOnClickListener { _ ->
                            PersonEditorDialogFragment.newInstance(false, getString(R.string.addingPlayer), Person("", "", ""), "", arguments?.getParcelableArrayList(KEY_GROUPS) ?: listOf()).show(activity.supportFragmentManager, PersonEditorDialogFragment.TAG)
                            dismiss()
                        }

                        binding.groupBtn.setOnClickListener { _ ->
                            GroupEditorDialogFragment.newInstance(false, getString(R.string.addingGroup), Group("", "", "", false)).show(activity.supportFragmentManager, GroupEditorDialogFragment.TAG)
                            dismiss()
                        }
                    }

                }
        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

}