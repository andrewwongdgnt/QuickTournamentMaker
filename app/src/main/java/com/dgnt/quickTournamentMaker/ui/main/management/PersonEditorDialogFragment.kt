package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.PersonEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class PersonEditorDialogFragment(
    private val editing: Boolean,
    private val title: String,
    private val person: Person,
    private val groupName: String,
    private val groups: List<Group>
) : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: PersonEditorViewModelFactory by instance()

    companion object {

        const val TAG = "PersonEditorDialogFragment"

        fun newInstance(
            fragmentManager: FragmentManager,
            editing: Boolean,
            title: String,
            person: Person,
            groupName: String,
            groups: List<Group>
        ) =
            PersonEditorDialogFragment(editing, title, person, groupName, groups).show(fragmentManager, TAG)
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


            viewModel.setData(person, groupName, groups, getString(R.string.defaultGroupName))

            AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(binding.root)
                .setPositiveButton(if (editing) R.string.save else R.string.add, null)
                .setNegativeButton(android.R.string.cancel, null)
                .also {
                    if (!editing) {
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

                        val enabled = person.name.isNotBlank()
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