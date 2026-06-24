package com.cso.claudetest.data.repository

import com.cso.claudetest.data.model.TodoEntity
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun observeAll(): Flow<List<TodoEntity>>
    fun observeById(id: Long): Flow<TodoEntity?>
    suspend fun insert(todo: TodoEntity): Long
    suspend fun update(todo: TodoEntity)
    suspend fun delete(todo: TodoEntity)
}
