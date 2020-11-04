package com.dgnt.quickTournamentMaker.ui.main.management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.databinding.ManagementFragmentBinding

class ManagementFragment : Fragment() {
    companion object {
        fun newInstance() = ManagementFragment()
    }

    private lateinit var binding: ManagementFragmentBinding
    private lateinit var viewModel: ManagementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.management_fragment, container, false)
        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (context == null) {
            return
        }
        val db = QTMDatabase.getInstance(context!!)
        val personRepository = PersonRepository.getInstance(db.personDAO)
        val groupRepository = GroupRepository.getInstance(db.groupDAO)
        val factory = ManagementViewModelFactory(personRepository, groupRepository)
        viewModel = ViewModelProvider(this, factory).get(ManagementViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToPersonDetails.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { triple ->// Only proceed if the event has never been handled
                val person = triple.first
                val groupName = triple.second
                val editing = triple.third

                PersonEditorDialogFragment.newInstance(editing, if (editing) getString(R.string.editing, person.name) else getString(R.string.adding), person, groupName).show(activity?.supportFragmentManager!!, PersonEditorDialogFragment.TAG)

            }
        })

        displayPersonList()
    }

    private fun displayPersonList() {
        viewModel.persons.observe(viewLifecycleOwner, Observer {
            Log.i("DGNTTAG", it.toString())
        })
    }


}