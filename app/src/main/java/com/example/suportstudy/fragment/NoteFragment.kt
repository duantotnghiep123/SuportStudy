package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseDetailActivity
import com.example.suportstudy.adapter.DocumentAdapter
import com.example.suportstudy.adapter.NoteAdapter
import com.example.suportstudy.apibodymodel.GetNoteBody
import com.example.suportstudy.databinding.FragmentNoteBinding
import com.example.suportstudy.model.Document
import com.example.suportstudy.model.Note
import com.example.suportstudy.service.QuestionAPI
import com.example.suportstudy.until.ConnectionManager
import com.example.suportstudy.until.Constrain
import com.example.suportstudy.viewmodel.NoteViewModel
import org.bson.types.ObjectId
import kotlin.collections.ArrayList


class NoteFragment : Fragment() {
    companion object {

    }

    private lateinit var binding: FragmentNoteBinding
    private lateinit var viewModel: NoteViewModel
    private var noteAdapter = NoteAdapter()
    private lateinit var myUid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(NoteViewModel::class.java)
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        subscribeUI()
        subscribeUserAva()
        getNote()
        setDataToNoteRecyclerView()
//        Constrain.hideKeyBoard(activity as Activity)
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
                        viewModel.insertNote(it.data)
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

            liveDataUserAva.observe(viewLifecycleOwner, {
                it?.let { imageUrl ->
                    Constrain.checkShowImage(
                        requireContext(),
                        R.drawable.avatar_default,
                        imageUrl,
                        binding.IVProfile
                    )
                }
            })
        }
    }

    private fun subscribeUserAva() {

    }

    private fun getNote() {
        viewModel.getSelfNote(isGroupNote = 0, myUid)
    }

    private fun setDataToNoteRecyclerView() {
        binding.rcvDocument.apply {
            adapter = noteAdapter
            hasFixedSize()
        }
    }
}