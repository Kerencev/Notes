package com.kerencev.notes

import com.kerencev.notes.logic.memory.todo.TodoRepository
import com.kerencev.notes.logic.memory.todo.TodoRepositoryImpl
import com.kerencev.notes.ui.todo.TodoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //Repository
    single<TodoRepository> { TodoRepositoryImpl() }

    //View Models
    viewModel { TodoViewModel(get()) }
}