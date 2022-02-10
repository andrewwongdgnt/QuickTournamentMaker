package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.LoadTournamentFilterOptionsFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.toIntOr
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class LoadTournamentFilterOptionsDialogFragment(
    private val listener: OnEditListener<Unit>
) : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: LoadTournamentFilterOptionsViewModelFactory by instance()

    companion object {

        const val TAG = "LoadTournamentFilterOptionsDialogFragment"

        fun newInstance(listener: OnEditListener<Unit>): LoadTournamentFilterOptionsDialogFragment =
            LoadTournamentFilterOptionsDialogFragment(listener)

    }

    private lateinit var binding: LoadTournamentFilterOptionsFragmentBinding
    private lateinit var viewModel: LoadTournamentFilterOptionsViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        activity?.let { activity ->

            binding = LoadTournamentFilterOptionsFragmentBinding.inflate(activity.layoutInflater)

            viewModel = ViewModelProvider(this, viewModelFactory)[LoadTournamentFilterOptionsViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.init()

            binding.apply {
                val dateFormat = DateTimeFormat.mediumDate()

                val invokeDatePickerDialog = { mutableLiveDataDate: MutableLiveData<String>,
                                               mutableLiveDataDateBacking: MutableLiveData<LocalDateTime> ->
                    mutableLiveDataDateBacking.value?.let { passedInDate ->
                        DatePickerDialog(
                            activity,
                            { _, year, monthOfYear, dayOfMonth ->
                                val date = LocalDateTime(year, monthOfYear + 1, dayOfMonth, 0, 0)
                                mutableLiveDataDate.value = dateFormat.print(date)
                                mutableLiveDataDateBacking.value = date
                            },
                            passedInDate.year, passedInDate.monthOfYear - 1, passedInDate.dayOfMonth
                        ).show()
                    }
                }

                earliestCreatedDateEt.setOnClickListener { invokeDatePickerDialog(viewModel.earliestCreatedDate, viewModel.earliestCreatedDateBacking) }
                latestCreatedDateEt.setOnClickListener { invokeDatePickerDialog(viewModel.latestCreatedDate, viewModel.latestCreatedDateBacking) }
                earliestModifiedDateEt.setOnClickListener { invokeDatePickerDialog(viewModel.earliestModifiedDate, viewModel.earliestModifiedDateBacking) }
                latestModifiedDateEt.setOnClickListener { invokeDatePickerDialog(viewModel.latestModifiedDate, viewModel.latestModifiedDateBacking) }
            }

            return AlertDialog.Builder(activity)
                .setTitle(getString(R.string.filter))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    viewModel.run {
                        eliminationFilteredOn.value?.let { setFilteredOnTournamentType(TournamentType.ELIMINATION, it) }
                        doubleEliminationFilteredOn.value?.let { setFilteredOnTournamentType(TournamentType.DOUBLE_ELIMINATION, it) }
                        roundRobinFilteredOn.value?.let { setFilteredOnTournamentType(TournamentType.ROUND_ROBIN, it) }
                        swissFilteredOn.value?.let { setFilteredOnTournamentType(TournamentType.SWISS, it) }
                        survivalFilteredOn.value?.let { setFilteredOnTournamentType(TournamentType.SURVIVAL, it) }

                        minParticipantsFilteredOn.value?.let { setFilteredOnMinimumParticipants(it) }
                        minParticipants.value?.let { setMinimumParticipantsToFilterOn(it.toIntOr(0)) }
                        maxParticipantsFilteredOn.value?.let { setFilteredOnMaximumParticipants(it) }
                        maxParticipants.value?.let { setMaximumParticipantsToFilterOn(it.toIntOr(0)) }

                        earliestCreatedDateFilteredOn.value?.let { setFilteredOnEarliestCreatedDate(it) }
                        earliestCreatedDateBacking.value?.let { setEarliestCreatedDateToFilterOn(it) }
                        latestCreatedDateFilteredOn.value?.let { setFilteredOnLatestCreatedDate(it) }
                        latestCreatedDateBacking.value?.let { setLatestCreatedDateToFilterOn(it) }

                        earliestModifiedDateFilteredOn.value?.let { setFilteredOnEarliestModifiedDate(it) }
                        earliestModifiedDateBacking.value?.let { setEarliestModifiedDateToFilterOn(it) }
                        latestModifiedDateFilteredOn.value?.let { setFilteredOnLatestModifiedDate(it) }
                        latestModifiedDateBacking.value?.let { setLatestModifiedDateToFilterOn(it) }
                    }

                    listener.onEdit(Unit)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

}