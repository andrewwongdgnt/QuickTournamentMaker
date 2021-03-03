package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.EditTournamentFragmentBinding
import kotlinx.android.synthetic.main.main_activity.*

class EditTournamentDialogFragment : DialogFragment() {

    companion object {

        const val TAG = "EditTournamentFragment"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_DESCRIPTION = "KEY_DESCRIPTION"

        fun newInstance(title: String, description: String): EditTournamentDialogFragment =
            EditTournamentDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TITLE, title)
                    putString(KEY_DESCRIPTION, description)
                }

            }

    }
    internal lateinit var listener: IEditTournamentDialogFragmentListener

    private lateinit var binding: EditTournamentFragmentBinding
    private lateinit var viewModel: EditTournamentViewModel
    private lateinit var alertDialog: AlertDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IEditTournamentDialogFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement IDialogFragmentListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?
    ): Dialog {

        if (activity == null || activity?.layoutInflater == null) {
            return super.onCreateDialog(savedInstanceState)
        }

        binding = DataBindingUtil.inflate(activity?.layoutInflater!!, R.layout.edit_tournament_fragment, container, false)

        viewModel = ViewModelProvider(this).get(EditTournamentViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.setData(arguments?.getString(KEY_TITLE)!!, arguments?.getString(KEY_DESCRIPTION)!!)

        return AlertDialog.Builder(activity)
            .setTitle(R.string.editTournament)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->

                listener.onEditTournament(viewModel.title.value?:"", viewModel.description.value?:"")
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()

    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }
}