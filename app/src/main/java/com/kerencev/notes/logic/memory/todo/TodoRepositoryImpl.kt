package com.kerencev.notes.logic.memory.todo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kerencev.notes.logic.Callback
import java.util.*

private const val KEY_MESSAGE = "message"
private const val KEY_CREATED_AT = "createdAt"
private const val KEY_MY_DATE = "myDate"

class TodoRepositoryImpl: TodoRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override fun getAll(
        repository: String,
        direction: Query.Direction,
        callback: Callback<List<TodoEntity>>
    ) {
        firestore.collection(repository)
            .orderBy("createdAt", direction)
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                val result = ArrayList<TodoEntity>()
                for (documentSnapshot in queryDocumentSnapshots.documents) {
                    val id = documentSnapshot.id
                    val message = documentSnapshot.getString(KEY_MESSAGE)
                    val createdAt = documentSnapshot.getDate(KEY_CREATED_AT)
                    val myDate = documentSnapshot.getString(KEY_MY_DATE)
                    message?.let { createdAt?.let { myDate?.let {
                        val todo = TodoEntity(id = id, message = message, createdAt = createdAt, myDate = myDate)
                        result.add(todo)
                    } } }
                }
                callback.onSuccess(result)
            }
    }

    override fun addTodo(
        message: String,
        createdAt: Date,
        myDate: String,
        callback: Callback<TodoEntity>
    ) {

    }

    override fun removeTodo(todo: TodoEntity, callback: Callback<Void>) {

    }
}