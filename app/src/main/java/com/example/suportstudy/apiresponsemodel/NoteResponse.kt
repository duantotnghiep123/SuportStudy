package com.example.suportstudy.apiresponsemodel

import com.example.suportstudy.model.Note
import com.google.gson.annotations.SerializedName
import retrofit2.SkipCallbackExecutor

data class NoteResponse(
    @SerializedName("payload")
    var data: List<Note>
) {
}