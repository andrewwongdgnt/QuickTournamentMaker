package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.GroupEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class GroupEditorDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: GroupEditorViewModelFactory by instance()

    companion object {

        const val TAG = "GroupEditorDialogFragment"

        private const val KEY_EDITING = "KEY_EDITING"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_GROUP = "KEY_GROUP"

        fun newInstance(editing: Boolean, title: String, group: Group): GroupEditorDialogFragment =
            GroupEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(KEY_EDITING, editing)
                    putString(KEY_TITLE, title)
                    putParcelable(KEY_GROUP, group)
                }
            }
    }

    private lateinit var binding: GroupEditorFragmentBinding
    private lateinit var viewModel: GroupEditorViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->

            binding = GroupEditorFragmentBinding.inflate(layoutInflater)

            viewModel = ViewModelProvider(this, viewModelFactory).get(GroupEditorViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.resultEvent.observe(activity, Observer {
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

            viewModel.setData(arguments?.getParcelable(KEY_GROUP))
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
                            if (editing)
                                viewModel.edit(getString(R.string.editSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value))
                            else
                                viewModel.add(getString(R.string.addSuccessfulMsg, viewModel.name.value), getString(R.string.duplicateMsg, viewModel.name.value), forceOpen = false, forceErase = false)
                        }

                        val enabled = !arguments?.getParcelable<Group>(KEY_GROUP)?.name.isNullOrBlank()
                        getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = enabled
                        getButton(AlertDialog.BUTTON_NEUTRAL).isEnabled = enabled

                    }

                    binding.groupNameEt.addTextChangedListener(object : TextWatcher {
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