package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.databinding.PersonEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.synthetic.main.main_activity.*


class PersonEditorDialogFragment : DialogFragment() {

    companion object {

        const val TAG = "PersonEditorDialogFragment"

        private const val KEY_EDITING = "KEY_EDITING"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_PERSON = "KEY_PERSON"
        private const val KEY_GROUP_NAME = "KEY_GROUP_NAME"
        private const val KEY_GROUPS = "KEY_GROUPS"

        fun newInstance(editing: Boolean, title: String, person: Person, groupName: String, groups:List<Group>): PersonEditorDialogFragment {
            val args = Bundle()
            args.putBoolean(KEY_EDITING, editing)
            args.putString(KEY_TITLE, title)
            args.putParcelable(KEY_PERSON, person)
            args.putString(KEY_GROUP_NAME, groupName)
            args.putParcelableArrayList(KEY_GROUPS, ArrayList<Group>(groups))
            val fragment = PersonEditorDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: PersonEditorFragmentBinding
    private lateinit var viewModel: PersonEditorViewModel
    private lateinit var alertDialog: AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null || activity?.layoutInflater == null) {
            return super.onCreateDialog(savedInstanceState)
        }

        binding = DataBindingUtil.inflate(activity?.layoutInflater!!, R.layout.person_editor_fragment, container, false)
        val db = QTMDatabase.getInstance(activity!!)
        val personRepository = PersonRepository.getInstance(db.personDAO)
        val groupRepository = GroupRepository.getInstance(db.groupDAO)
        val factory = PersonEditorViewModelFactory(personRepository, groupRepository)
        viewModel = ViewModelProvider(this, factory).get(PersonEditorViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.personNameEt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val enabled = s.isNotBlank()
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = enabled
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).isEnabled = enabled
            }
        })


        viewModel.setData(arguments?.getParcelable(KEY_PERSON), arguments?.getString(KEY_GROUP_NAME), arguments?.getParcelableArrayList<Group>(KEY_GROUPS),getString(R.string.defaultGroupName))
        val editing = arguments?.getBoolean(KEY_EDITING) == true
        val builder = AlertDialog.Builder(activity)
            .setTitle(arguments?.getString(KEY_TITLE))
            .setView(binding.root)
            .setPositiveButton(if (editing) R.string.edit else R.string.add) { _, _ -> if (editing) viewModel.edit() else viewModel.add() }
            .setNegativeButton(android.R.string.cancel, null)
        if (arguments?.getBoolean(KEY_EDITING) != true) {
            builder.setNeutralButton(R.string.addAndContinue, null)

        }
        alertDialog = builder.create()
        alertDialog.setOnShowListener { _ ->
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener { _ ->
                viewModel.add()
            }

            val enabled = !arguments?.getParcelable<Person>(KEY_PERSON)?.name.isNullOrBlank()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = enabled
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).isEnabled = enabled

        }
        return alertDialog
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

}