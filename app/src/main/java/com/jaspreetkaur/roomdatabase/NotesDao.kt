package com.jaspreetkaur.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {

    @Insert
    fun InsertNotes(vararg notes :Notes)

    @Query("select * from Notes")
    fun getNotes() : List<Notes>

    @Delete
    fun delete(vararg notes :Notes)


}