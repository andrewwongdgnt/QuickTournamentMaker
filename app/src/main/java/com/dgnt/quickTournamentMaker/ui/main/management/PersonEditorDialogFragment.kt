package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.PersonEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
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
                    putParcelableArrayList(KEY_GROUPS, ArrayList<Group>(groups))
                }
            }
    }

    private lateinit var binding: PersonEditorFragmentBinding
    private lateinit var viewModel: PersonEditorViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        activity?.let { activity ->

            binding = PersonEditorFragmentBinding.inflate(activity.layoutInflater)

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


            viewModel.setData(arguments?.getParcelable(KEY_PERSON), arguments?.getString(KEY_GROUP_NAME), arguments?.getParcelableArrayList(KEY_GROUPS), getString(R.string.defaultGroupName))
            val editing = arguments?.getBoolean(KEY_EDITING) == true
            AlertDialog.Builder(activity)
                .setTitle(arguments?.getString(KEY_TITLE))
                .setView(binding.root)
                .setPositiveButton(if (editing) R.string.save else R.string.add, null)
                .setNegativeButton(android.R.string.cancel, null)
                .also {
                    if (arguments?.getBoolean(KEY_EDITING) != true) {
                        it.setNeutralButton(R.string.addAndContinue, null)
                    } else {
                        it
                    }
                }
                .create()
                .apply {
                    setOnShowListener { _ ->
                        getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener { _ ->
                            viewModel.add(getString(R.string.addSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value), forceOpen = true, forceErase = true)
                        }
                        getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { _ ->
                            if (editing) {
                                viewModel.edit(getString(R.string.editSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value))
                            } else {
                                viewModel.add(getString(R.string.addSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value), forceOpen = false, forceErase = false)
                            }
                        }

                        val enabled = !arguments?.getParcelable<Person>(KEY_PERSON)?.name.isNullOrBlank()
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