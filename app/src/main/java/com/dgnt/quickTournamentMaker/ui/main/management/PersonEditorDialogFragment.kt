package com.dgnt.quickTournamentMaker.ui.main.management

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.PersonEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        private const val KEY_GROUP = "KEY_GROUP"
        private const val KEY_GROUPS = "KEY_GROUPS"

        fun newInstance(editing: Boolean, title: String, person: Person, group: Group, groups: List<Group>): PersonEditorDialogFragment =
            PersonEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(KEY_EDITING, editing)
                    putString(KEY_TITLE, title)
                    putParcelable(KEY_PERSON, person)
                    putParcelable(KEY_GROUP, group)
                    putParcelableArrayList(KEY_GROUPS, ArrayList<Group>(groups))
                }
            }
    }

    private val binding by viewBinding(PersonEditorFragmentBinding::inflate)
    private lateinit var viewModel: PersonEditorViewModel

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putParcelable(KEY_PERSON, Person(viewModel.personId, viewModel.name.value!!, viewModel.note.value!!))
            putParcelable(KEY_GROUP, viewModel.newGroup.value)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        activity?.let { activity ->

            viewModel = ViewModelProvider(this, viewModelFactory)[PersonEditorViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.resultEvent.observe(this) {
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
            }

            val person = savedInstanceState?.getParcelable<Person>(KEY_PERSON) ?: arguments?.getParcelable(KEY_PERSON)
            val group = savedInstanceState?.getParcelable<Group>(KEY_GROUP) ?: arguments?.getParcelable(KEY_GROUP)

            viewModel.setData(
                person,
                group,
                arguments?.getParcelableArrayList(KEY_GROUPS),
                getString(R.string.defaultGroupName)
            )

            val editing =  arguments?.getBoolean(KEY_EDITING) == true
            MaterialAlertDialogBuilder(activity, R.style.MyDialogTheme)
                .setTitle(arguments?.getString(KEY_TITLE))
                .setView(binding.root)
                .setPositiveButton(if (editing) R.string.save else R.string.add, null)
                .setNegativeButton(android.R.string.cancel, null)
                .also {
                    if (!editing) {
                        it.setNeutralButton(R.string.addAndContinue, null)
                    }
                }
                .create()
                .apply {
                    setOnShowListener {
                        getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                            viewModel.add(getString(R.string.addSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value), forceOpen = true, forceErase = true)
                        }
                        getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                            if (editing) {
                                viewModel.edit(getString(R.string.editSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value))
                            } else {
                                viewModel.add(getString(R.string.addSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value), forceOpen = false, forceErase = false)
                            }
                        }

                        val enabled = !person?.name.isNullOrBlank()
                        getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = enabled
                        getButton(AlertDialog.BUTTON_NEUTRAL).isEnabled = enabled

                    }

                    binding.personNameEt.addTextChangedListener(object : TextWatcher {
                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        }

                        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                        }

                        override fun afterTextChanged(s: Editable) {
                            val enabled = s.isNotBlank()
                            getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = enabled
                            getButton(AlertDialog.BUTTON_NEUTRAL).isEnabled = enabled
                        }
                    })
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