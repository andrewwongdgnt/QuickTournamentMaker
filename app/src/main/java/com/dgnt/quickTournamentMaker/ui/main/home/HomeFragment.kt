package com.dgnt.quickTournamentMaker.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.databinding.HomeFragmentBinding
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import kotlinx.android.synthetic.main.component_tournament_type_editor.*
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class HomeFragment : Fragment(), DIAware {


    override val di by di()

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (context == null) {
            return
        }

        val preferenceService: IPreferenceService by instance()

        val db = QTMDatabase.getInstance(context!!)
        val personRepository = PersonRepository.getInstance(db.personDAO)
        val factory = HomeViewModelFactory(personRepository, preferenceService)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        binding.vm = viewModel

        binding.lifecycleOwner = this
        viewModel.tournamentType.value = elimination_rb.id
        viewModel.seedType.value = randomSeed_rb.id
        sameSeed_rb.visibility = View.GONE
        viewModel.tournamentType.observe(viewLifecycleOwner, Observer {
            viewModel.showRankConfig.value = when (it) {
                elimination_rb.id, doubleElimination_rb.id, survival_rb.id -> false
                else -> true
            }
            viewModel.showSeedType.value = when (it) {
                survival_rb.id -> false
                else -> true
            }

            viewModel.handleTournamentTypeChange(it, roundRobin_rb.id, swiss_rb.id, compareRankFromPriority_rb.id, compareRankFromScore_rb.id)


        })
        viewModel.rankConfig.observe(viewLifecycleOwner, Observer {
            viewModel.showPriorityContent.value = it == compareRankFromPriority_rb.id

            viewModel.showScoringContent.value = it == compareRankFromScore_rb.id

            viewModel.handleRankConfigChange(it == compareRankFromPriority_rb.id, roundRobin_rb.id, swiss_rb.id)
        })
    }


}