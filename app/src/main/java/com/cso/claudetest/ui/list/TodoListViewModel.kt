package com.cso.claudetest.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cso.claudetest.data.model.TodoEntity
import com.cso.claudetest.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    val todos: StateFlow<List<TodoEntity>> = repository.observeAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    suspend fun createTodo(): Long = withContext(Dispatchers.IO) {
        repository.insert(TodoEntity())
    }

    fun toggleDone(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(todo.copy(isDone = !todo.isDone))
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(todo)
        }
    }
}
