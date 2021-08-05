package com.example.suportstudy.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.suportstudy.R
import com.example.suportstudy.adapter.NoteAdapter
import com.example.suportstudy.databinding.FragmentGroupNoteBinding
import com.example.suportstudy.databinding.FragmentSelfNoteBinding
import com.example.suportstudy.until.Constrain
import com.example.suportstudy.viewmodel.NoteViewModel

class GroupNoteFragment : Fragment() {
    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: FragmentGroupNoteBinding
    private var noteAdapter = NoteAdapter()
    private lateinit var myUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel =
            ViewModelProvider(this).get(NoteViewModel::class.java)
        binding = FragmentGroupNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getNote()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        subscribeUI()
        getNote()
        setDataToNoteRecyclerView()
        binding.swRefresh.setOnRefreshListener {
            binding.swRefresh.isRefreshing = false
            getNote()
        }
    }
    companion object {

    }

    private fun init() {
        var userSharedPreferences = activity?.getSharedPreferences(
            Constrain.SHARED_REF_USER,
            AppCompatActivity.MODE_PRIVATE
        )
        myUid = userSharedPreferences!!.getString(Constrain.KEY_ID, "")!!


    }

    private fun subscribeUI() {
        viewModel.apply {
            /** Handle message fail **/
            messageFail.observe(viewLifecycleOwner, Observer {
                messageFail.value = null
                Constrain.showErrorMessage(it, requireContext())
            })

            /** List note response **/
            liveDataNoteResponse.observe(viewLifecycleOwner, Observer {
                it?.let {
                    liveDataNoteResponse.value = null
                    if (!it.data.isNullOrEmpty()) {
                        viewModel.insertGroupNote(it.data)
                    }
                }
            })

            /** Observe data from DB **/
            liveDataNote.observe(viewLifecycleOwner, Observer {
                it?.let {
                    liveDataNote.value = null
                    if (!it.isNullOrEmpty()) {
                        noteAdapter.notes = it.toMutableList()
                    }
                }
            })

        }
    }

    private fun getNote() {
        viewModel.getListNote(isGroupNote = 1, myUid)
    }

    private fun setDataToNoteRecyclerView() {
        binding.rcvDocument.apply {
            adapter = noteAdapter
            hasFixedSize()
        }
    }
}