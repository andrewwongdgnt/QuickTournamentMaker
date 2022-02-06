package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.AddChoiceFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person


class AddChoiceDialogFragment(
    private val groups: List<Group>
) : DialogFragment() {


    companion object {

        const val TAG = "AddChoiceDialogFragment"

        fun newInstance(
            fragmentManager: FragmentManager,
            groups: List<Group>
        ) =
            AddChoiceDialogFragment(groups).show(fragmentManager, TAG)

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
                        binding.personBtn.setOnClickListener {
                            PersonEditorDialogFragment.newInstance(
                                activity.supportFragmentManager,
                                false,
                                getString(R.string.addingPlayer),
                                Person("", "", ""),
                                "",
                                groups
                            )
                            dismiss()
                        }

                        binding.groupBtn.setOnClickListener {
                            GroupEditorDialogFragment.newInstance(
                                activity.supportFragmentManager,
                                false,
                                getString(R.string.addingGroup),
                                Group("", "", "", false)
                            )
                            dismiss()
                        }
                    }

                }
        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

}