package com.cso.claudetest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cso.claudetest.data.model.TodoEntity

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
