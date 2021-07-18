package com.example.suportstudy.service

import com.example.suportstudy.apibodymodel.AddNoteBody
import com.example.suportstudy.apibodymodel.GetNoteBody
import com.example.suportstudy.apiresponsemodel.NoteResponse
import com.example.suportstudy.model.Note
import com.example.suportstudy.until.Constrain
import org.bson.types.ObjectId
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NoteAPI {
    @POST("/api/note/addnote")
    fun addNote(@Body addNoteBody: AddNoteBody)

    @GET("/api/note/getByType")
    fun getSelfNote(
        @Query("isGroupNote") isGroupNote: Int,
        @Query("userId") userId: String
    ): Call<NoteResponse>

    companion object {
        operator fun invoke(): NoteAPI {
            return Constrain.createRetrofit(NoteAPI::class.java)
        }
    }
}