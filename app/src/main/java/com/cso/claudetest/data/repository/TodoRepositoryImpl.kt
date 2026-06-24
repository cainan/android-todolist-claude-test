package com.cso.claudetest.data.repository

import com.cso.claudetest.data.db.TodoDao
import com.cso.claudetest.data.model.TodoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {
    override fun observeAll(): Flow<List<TodoEntity>> = dao.observeAll()
    override fun observeById(id: Long): Flow<TodoEntity?> = dao.observeById(id)
    override suspend fun insert(todo: TodoEntity): Long = dao.insert(todo)
    override suspend fun update(todo: TodoEntity) = dao.update(todo)
    override suspend fun delete(todo: TodoEntity) = dao.delete(todo)
}
