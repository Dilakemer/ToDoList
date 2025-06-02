package com.example.todoapp.models
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Task(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "taskId")
    val id:String,
    @ColumnInfo(name = "taskTitle")
    val title:String,
    val describe:String,
    val date: java.util.Date
)
