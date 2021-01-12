package com.example.tasks_to_do

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Insert
    suspend fun Insert(todoModel: TodoModel):Long
    @Delete
    suspend fun delete(todo:TodoModel)

    @Query("Select * from TodoModel where isFinished == -1")
    fun getTask():LiveData<List<TodoModel>>

    @Query("Update TodoModel Set isFinished=1 where id=:uid")
    fun finishTask(uid:Long)

    @Query("Delete from TodoModel where  id=:uid")
    fun DeleteTask(uid:Long)






}