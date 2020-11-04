package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.databinding.PersonEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.synthetic.main.main_activity.*

class PersonEditorDialogFragment : DialogFragment() {

    companion object {

        const val TAG = "PersonEditorDialogFragment"

        private const val KEY_EDITING = "KEY_EDITING"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_PERSON = "KEY_PERSON"
        private const val KEY_GROUP_NAME = "KEY_GROUP_NAME"

        fun newInstance(editing: Boolean, title: String, person: Person, groupName: String): PersonEditorDialogFragment {
            val args = Bundle()
            args.putBoolean(KEY_EDITING, editing)
            args.putString(KEY_TITLE, title)
            args.putParcelable(KEY_PERSON, person)
            args.putString(KEY_GROUP_NAME, groupName)
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

        viewModel.setData(arguments?.getParcelable(KEY_PERSON), arguments?.getString(KEY_GROUP_NAME))

        val builder = AlertDialog.Builder(activity)
        .setTitle(arguments?.getString(KEY_TITLE))
        .setView(binding.root)
        .setPositiveButton(if (arguments?.getBoolean(KEY_EDITING) == true) R.string.edit else R.string.add) { _, _ -> viewModel.add() }
        .setNegativeButton(android.R.string.cancel,null)
        if (arguments?.getBoolean(KEY_EDITING) != true) {
            builder.setNeutralButton(R.string.addAndContinue, null)

        }
        alertDialog = builder.create()
        alertDialog.setOnShowListener { d ->
            (d as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener { _ ->
                viewModel.add()
            }

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