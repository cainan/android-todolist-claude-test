package com.cso.claudetest.di

import android.content.Context
import androidx.room.Room
import com.cso.claudetest.data.db.TodoDao
import com.cso.claudetest.data.db.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase =
        Room.databaseBuilder(context, TodoDatabase::class.java, "todo_db").build()

    @Provides
    fun provideTodoDao(db: TodoDatabase): TodoDao = db.todoDao()
}
