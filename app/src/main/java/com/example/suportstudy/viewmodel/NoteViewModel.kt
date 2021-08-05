package com.example.suportstudy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.suportstudy.apibodymodel.AddNoteBody
import com.example.suportstudy.apibodymodel.GetNoteBody
import com.example.suportstudy.apiresponsemodel.AddNoteResponse
import com.example.suportstudy.apiresponsemodel.NoteResponse
import com.example.suportstudy.database.AppDatabase
import com.example.suportstudy.model.Note
import com.example.suportstudy.service.NoteAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private var noteDAO = AppDatabase.getInstance(application).getNoteDao()
    val liveDataNoteResponse = MutableLiveData<NoteResponse>()
    val liveDataAddNoteResponse = MutableLiveData<AddNoteResponse>()
    val messageFail = MutableLiveData<String>()
    val liveDataNote = MutableLiveData<List<Note>>()
    val liveDataUserAva = MutableLiveData<String>()

    //Get Note from API
    fun getListNote(isGroupNote: Int, userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            NoteAPI().getListNote(isGroupNote, userId).enqueue(object : Callback<NoteResponse> {
                override fun onResponse(
                    call: Call<NoteResponse>,
                    response: Response<NoteResponse>
                ) {
                    if (response.isSuccessful) {
                        liveDataNoteResponse.postValue(response.body())
                    } else {
                        messageFail.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                    messageFail.postValue(t.message)
                }

            })
        }
    }

    //Add Note to server
    fun addNote(addNoteBody: AddNoteBody) {
        CoroutineScope(Dispatchers.IO).launch {
            NoteAPI().addNote(addNoteBody).enqueue(object : Callback<AddNoteResponse> {
                override fun onResponse(
                    call: Call<AddNoteResponse>,
                    response: Response<AddNoteResponse>
                ) {
                    if (response.isSuccessful) {
                        liveDataAddNoteResponse.postValue(response.body())
                    } else {
                        messageFail.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<AddNoteResponse>, t: Throwable) {
                    messageFail.postValue(t.message)
                }

            })
        }
    }

    //Insert Note to DB
    fun insertNote(listNote: List<Note>) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDAO.insert(listNote)
            liveDataNote.postValue(noteDAO.getSelfNote())
        }
    }

    fun insertGroupNote(listNote: List<Note>) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDAO.insert(listNote)
            liveDataNote.postValue(noteDAO.getGroupNote())
        }
    }

    // Get info User
    fun getUserAva() {
        CoroutineScope(Dispatchers.IO).launch {
            noteDAO.getUserAva()
            liveDataUserAva.postValue(noteDAO.getUserAva())
        }
    }
}