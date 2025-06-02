package com.example.todoapp.utils

import androidx.core.app.NotificationCompat.MessagingStyle.Message

sealed class Resource<T>(val status: Status,val data: T?=null,val message: String?=null) {

    class Success<T>(message:String,data: T?=null):Resource<T>(Status.SUCCESS,data,message)
    class Error<T>(message: String?,data: T?):Resource<T>(Status.ERROR,data,message)
    class Loading<T>:Resource<T>(Status.LOADING)
}