package com.example.suportstudy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.suportstudy.model.Note

@Dao
interface NoteDAO {

    //INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notes: List<Note>)

    //QUERY
    @Query("SELECT * FROM NOTE WHERE IS_GROUP_NOTE = 0")
    suspend fun getSelfNote(): List<Note>

    @Query("SELECT USER_IMAGE FROM NOTE")
    suspend fun getUserAva():String
}