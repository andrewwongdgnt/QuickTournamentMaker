package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.synthetic.main.main_activity.*


class AddChoiceDialogFragment : DialogFragment() {



    companion object {

        const val TAG = "AddChoiceDialogFragment"

        private const val KEY_GROUP_NAMES = "KEY_GROUP_NAMES"

        fun newInstance(groupNames:List<String>): AddChoiceDialogFragment{
            val args = Bundle()
            args.putStringArrayList(KEY_GROUP_NAMES, ArrayList<String>(groupNames))
            val fragment = AddChoiceDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null || activity?.layoutInflater == null) {
            return super.onCreateDialog(savedInstanceState)
        }

        return AlertDialog.Builder(activity)
            .setTitle(getString(R.string.addWhich))
            .setView(activity?.layoutInflater?.inflate(R.layout.add_choice_fragment, container, false))
            .setPositiveButton(R.string.continue_) { v, _ ->
                if ((v as AlertDialog).findViewById<RadioButton>(R.id.person_rb).isChecked)
                    PersonEditorDialogFragment.newInstance(false, getString(R.string.addingPerson), Person("", "", ""), "", arguments?.getStringArrayList(KEY_GROUP_NAMES) ?: listOf()).show(activity?.supportFragmentManager!!, PersonEditorDialogFragment.TAG)
                else
                    GroupEditorDialogFragment.newInstance(false, getString(R.string.addingGroup), Group("", "", "",false)).show(activity?.supportFragmentManager!!, GroupEditorDialogFragment.TAG)


            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }
}