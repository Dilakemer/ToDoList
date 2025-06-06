package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ViewTaskLayoutBinding
import com.example.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRVViewBindingAdapter(
    private  val deleteUpdateCallback:(type:String,position:Int,task:Task)->Unit
) :
    RecyclerView.Adapter<TaskRVViewBindingAdapter.ViewHolder>() {

    private val taskList = arrayListOf<Task>()

    // Görünüm bağlayıcısı (ViewHolder)
    class ViewHolder(val viewTaskLayoutBinding: ViewTaskLayoutBinding) :
        RecyclerView.ViewHolder(viewTaskLayoutBinding.root)

    // Görev listesini ekleyin ve sadece yeni eklenen öğeleri bildirin
    fun addAllTask(newTaskList: List<Task>) {
        taskList.clear()  // Önceki listeyi temizle
        taskList.addAll(newTaskList)  // Yeni listeyi ekle
        notifyDataSetChanged()  // Tüm listeyi yeniden çiz
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewTaskLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]

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

    override fun getItemCount(): Int {
        return taskList.size // Liste boyutunu döndürün
    }
}
