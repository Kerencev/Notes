package com.kerencev.notes.logic.memory.todo

import java.util.*

data class TodoEntity(
    val id: String?,
    val message: String,
    val createdAt: Date,
    val myDate: String
)
