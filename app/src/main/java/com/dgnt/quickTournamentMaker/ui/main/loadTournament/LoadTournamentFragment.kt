package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R

class LoadTournamentFragment : Fragment() {

    companion object {
        fun newInstance() = LoadTournamentFragment()
    }

    private lateinit var viewModel: LoadTournamentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.load_tournament_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoadTournamentViewModel::class.java)

    }

}