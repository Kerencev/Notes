package com.kerencev.notes.logic.memory.todo

import com.google.firebase.firestore.Query
import com.kerencev.notes.logic.Callback
import com.kerencev.notes.logic.Note
import java.util.*

interface TodoRepository {
    fun getAll(repository: String, direction: Query.Direction, callback: Callback<List<TodoEntity>>)

    fun addTodo(
        message: String,
        createdAt: Date,
        myDate: String,
        callback: Callback<TodoEntity>
    )

    fun removeTodo(todo: TodoEntity, callback: Callback<Void>)
}