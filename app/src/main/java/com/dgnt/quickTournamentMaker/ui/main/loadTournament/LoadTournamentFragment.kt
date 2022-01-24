package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.databinding.LoadTournamentFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.ui.tournament.MoreInfoDialogFragment
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class LoadTournamentFragment : Fragment(), DIAware {
    override val di by di()
    private val viewModelFactory: LoadTournamentViewModelFactory by instance()

    companion object {
        fun newInstance() = LoadTournamentFragment()
    }

    private lateinit var binding: LoadTournamentFragmentBinding
    private lateinit var viewModel: LoadTournamentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoadTournamentFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragment = this
        context?.apply {
            viewModel = ViewModelProvider(fragment, viewModelFactory)[LoadTournamentViewModel::class.java]
            binding.vm = viewModel

            binding.lifecycleOwner = viewLifecycleOwner

            viewModel.tournamentLiveData.observe(viewLifecycleOwner, {
                Log.d("DGNTTAG", "tournament info: $it")

                binding.tournamentRv.adapter = RestoredTournamentInformationRecyclerViewAdapter(
                    it,
                    { restoredTournamentInformation ->
                        MoreInfoDialogFragment.newInstance(restoredTournamentInformation.extendedTournamentInformation, tournamentEditListener).show(activity?.supportFragmentManager!!, MoreInfoDialogFragment.TAG)
                    },
                    { restoredTournamentInformation ->
                        startActivity(TournamentActivity.createIntent(this, restoredTournamentInformation))
                    }
                )
            })
        }

    }

    private val tournamentEditListener = object : OnEditListener<TournamentInformation> {
        override fun onEdit(editedValue: TournamentInformation) {
            viewModel.updateTournament(editedValue)
        }
    }

}