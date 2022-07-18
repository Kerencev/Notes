package com.kerencev.notes.ui.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import com.kerencev.notes.AppState
import com.kerencev.notes.logic.Callback
import com.kerencev.notes.logic.memory.todo.TodoEntity
import com.kerencev.notes.logic.memory.todo.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    private val _data = MutableLiveData<AppState>()
    val data: LiveData<AppState> get() = _data

    fun getAllTodo() {
        _data.value = AppState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAll(
                "todo",
                Query.Direction.ASCENDING,
                object : Callback<List<TodoEntity>> {
                    override fun onSuccess(data: List<TodoEntity>) {
                        _data.postValue(AppState.Success(data))
                    }

                    override fun onError(exception: Throwable?) {
                        _data.postValue(AppState.Error)
                    }
                })
        }
    }
}