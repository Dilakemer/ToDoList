package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ViewTaskLayoutBinding
import com.example.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRVVBListAdapter(
    private  val deleteUpdateCallback:(type:String,position:Int,task:Task)->Unit
) :
    ListAdapter<Task,TaskRVVBListAdapter.ViewHolder>(DiffCallBack()){


    // Görünüm bağlayıcısı (ViewHolder)
    class ViewHolder(val viewTaskLayoutBinding: ViewTaskLayoutBinding) :
        RecyclerView.ViewHolder(viewTaskLayoutBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewTaskLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)

        holder.viewTaskLayoutBinding.titleTxt.text = task.title
        holder.viewTaskLayoutBinding.descrTxt.text = task.describe

        // Tarihi doğru formatta gösterin
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Formatı düzeltin
        holder.viewTaskLayoutBinding.dateTxt.text = dateFormat.format(task.date)
        holder.viewTaskLayoutBinding.deleteImg.setOnClickListener{
            if(holder.adapterPosition !=1){
                deleteUpdateCallback("delete",holder.adapterPosition,task)
            }
        }
        holder.viewTaskLayoutBinding.editImg.setOnClickListener{
            if(holder.adapterPosition !=1){
                deleteUpdateCallback("update",holder.adapterPosition,task)
            }
        }
    }


    class DiffCallBack : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
          return  oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
           return oldItem==newItem
        }

    }
}
