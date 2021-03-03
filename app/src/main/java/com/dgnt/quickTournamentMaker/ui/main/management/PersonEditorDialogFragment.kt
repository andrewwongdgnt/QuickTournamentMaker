package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.PersonEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.synthetic.main.main_activity.*
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class PersonEditorDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: PersonEditorViewModelFactory by instance()

    companion object {

        const val TAG = "PersonEditorDialogFragment"

        private const val KEY_EDITING = "KEY_EDITING"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_PERSON = "KEY_PERSON"
        private const val KEY_GROUP_NAME = "KEY_GROUP_NAME"
        private const val KEY_GROUPS = "KEY_GROUPS"

        fun newInstance(editing: Boolean, title: String, person: Person, groupName: String, groups: List<Group>): PersonEditorDialogFragment =
            PersonEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(KEY_EDITING, editing)
                    putString(KEY_TITLE, title)
                    putParcelable(KEY_PERSON, person)
                    putString(KEY_GROUP_NAME, groupName)
                }
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

        viewModel = ViewModelProvider(this, viewModelFactory).get(PersonEditorViewModel::class.java)
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

        viewModel.resultEvent.observe(activity!!, Observer {
            it.getContentIfNotHandled()?.let { triple ->
                Toast.makeText(context, triple.second, Toast.LENGTH_LONG).show()
                if (triple.first) {
                    dismiss()
                }
                if (triple.third) {
                    viewModel.name.value = ""
                    viewModel.note.value = ""
                }
            }
        })


        viewModel.setData(arguments?.getParcelable(KEY_PERSON), arguments?.getString(KEY_GROUP_NAME), arguments?.getParcelableArrayList<Group>(KEY_GROUPS), getString(R.string.defaultGroupName))
        val editing = arguments?.getBoolean(KEY_EDITING) == true
        val builder = AlertDialog.Builder(activity)
            .setTitle(arguments?.getString(KEY_TITLE))
            .setView(binding.root)
            .setPositiveButton(if (editing) R.string.save else R.string.add, null)
            .setNegativeButton(android.R.string.cancel, null)
        if (arguments?.getBoolean(KEY_EDITING) != true) {
            builder.setNeutralButton(R.string.addAndContinue, null)
        }
        alertDialog = builder.create()
        alertDialog.setOnShowListener { _ ->
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener { _ ->
                viewModel.add(getString(R.string.addSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value), forceOpen = true, forceErase = true)
            }
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { _ ->
                if (editing) {
                    viewModel.edit(getString(R.string.editSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value))
                } else {
                    viewModel.add(getString(R.string.addSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value), forceOpen = false, forceErase = false)
                }
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