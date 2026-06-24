package com.cso.claudetest.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cso.claudetest.data.model.TodoEntity
import com.cso.claudetest.data.repository.TodoRepository
import com.cso.claudetest.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val todoId: Long = checkNotNull(savedStateHandle[Screen.TodoDetail.ARG_TODO_ID])

    val todo: StateFlow<TodoEntity?> = repository.observeById(todoId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun save(title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todo.value?.let { repository.update(it.copy(title = title, description = description)) }
        }
    }
}
