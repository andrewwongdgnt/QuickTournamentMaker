package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.synthetic.main.main_activity.*


class AddChoiceDialogFragment : DialogFragment() {


    companion object {

        const val TAG = "AddChoiceDialogFragment"

        private const val KEY_GROUPS = "KEY_GROUPS"

        fun newInstance(group: List<Group>): AddChoiceDialogFragment {
            val args = Bundle()
            args.putParcelableArrayList(KEY_GROUPS, ArrayList<Group>(group))
            val fragment = AddChoiceDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null || activity?.layoutInflater == null) {
            return super.onCreateDialog(savedInstanceState)
        }


        val alertDialog = AlertDialog.Builder(activity)
            .setTitle(getString(R.string.addWhich))
            .setView(activity?.layoutInflater?.inflate(R.layout.add_choice_fragment, container, false))
            .setNegativeButton(android.R.string.cancel, null)
            .create()


        alertDialog.setOnShowListener {
            alertDialog.findViewById<Button>(R.id.person_btn).setOnClickListener { _ ->
                PersonEditorDialogFragment.newInstance(false, getString(R.string.addingPerson), Person("", "", ""), "", arguments?.getParcelableArrayList<Group>(KEY_GROUPS) ?: listOf()).show(activity?.supportFragmentManager!!, PersonEditorDialogFragment.TAG)
                dismiss()
            }

            alertDialog.findViewById<Button>(R.id.group_btn)?.setOnClickListener { _ ->
                GroupEditorDialogFragment.newInstance(false, getString(R.string.addingGroup), Group("", "", "", false)).show(activity?.supportFragmentManager!!, GroupEditorDialogFragment.TAG)
                dismiss()
            }
        }

        return alertDialog
    }
}