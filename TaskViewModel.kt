package com.example.todoapp.newmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Query
import com.example.todoapp.models.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)
    val taskStateFlow get() = taskRepository.taskStateFlow
    val statusLiveData get() = taskRepository.statusLiveData
     fun getTaskList() {
         taskRepository.getTaskList()
     }

    fun insertTask(task: Task){
        taskRepository.insertTask(task)
    }
    fun deleteTask(task: Task){
        taskRepository.deleteTask(task)
    }
    fun updateTask(task: Task){
      taskRepository.updateTask(task)
    }
    fun updateTaskParticularField(taskId: String,title:String,describe:String){
        taskRepository.updateTaskParticularField(taskId,title,describe)
    }
    fun searchTaskList(query: String){
        taskRepository.searchTaskList(query)
    }

}
