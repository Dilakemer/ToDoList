package com.example.todoapp
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import kotlin.lazy
import com.example.todoapp.databinding.ActivityMainBinding
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import com.example.todoapp.adapters.TaskRVVBListAdapter

import com.example.todoapp.models.Task
import com.example.todoapp.newmodels.TaskViewModel
import com.example.todoapp.utils.Status
import com.example.todoapp.utils.StatusResult
import com.example.todoapp.utils.StatusResult.*
import com.example.todoapp.utils.clearEditText
import com.example.todoapp.utils.hidekeyboard
import com.example.todoapp.utils.longToastShow
import com.example.todoapp.utils.setupDialog
import com.example.todoapp.utils.validateEditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
class MainActivity : AppCompatActivity() {

    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val addTaskDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.add_task_dialog)
        }
    }
    private val updateTaskDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.update_task_dialog)
        }
    }
    private val loadingDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }
    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)


        // Add task
        val addCloseImg = addTaskDialog.findViewById<ImageView>(R.id.closeImg)
        addCloseImg.setOnClickListener { addTaskDialog.dismiss() }

        val addETTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val addETTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        addETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETTitle, addETTitleL)
            }
        })

        val addETDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val addETDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        addETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETDesc, addETDescL)
            }
        })

        mainBinding.addTaskFABtn.setOnClickListener {
            clearEditText(addETDesc, addETDescL)
            clearEditText(addETTitle, addETTitleL)
            addTaskDialog.show()
        }

        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.saveTaskBtn)
        saveTaskBtn.setOnClickListener {
            if (validateEditText(addETDesc, addETDescL)
                && validateEditText(addETTitle, addETTitleL)) {

                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
                    Date()
                )
                hidekeyboard(it)
                addTaskDialog.dismiss()

                taskViewModel.insertTask(newTask)
            }
        }

        // Update task
        val updateETTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val updateETTitleL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        updateETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETTitle, updateETTitleL)
            }
        })

        val updateETDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val updateETDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        updateETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETDesc, updateETDescL)
            }
        })

        val updateCloseImg = updateTaskDialog.findViewById<ImageView>(R.id.closeImg)
        updateCloseImg.setOnClickListener { updateTaskDialog.dismiss() }

        val updateTaskBtn = updateTaskDialog.findViewById<Button>(R.id.updateTaskBtn)

        val taskRVVBListAdapter=TaskRVVBListAdapter() { type, position, task ->
            if (type == "delete") {
                taskViewModel
                    .deleteTask(task)

            } else if (type == "update") {
                updateETTitle.setText(task.title)
                updateETDesc.setText(task.describe)
                updateTaskBtn.setOnClickListener {
                    if (validateEditText(updateETTitle, updateETTitleL) && validateEditText(updateETDesc, updateETDescL)) {
                        val updateTask=Task(
                            task.id,
                            updateETTitle.text.toString().trim(),
                            updateETDesc.text.toString().trim(),
                            Date()
                        )
                        hidekeyboard(it)
                        updateTaskDialog.dismiss()
                        loadingDialog.show()
                        taskViewModel
                            .updateTaskParticularField(task.id,
                                updateETTitle.text.toString().trim(),
                                updateETDesc.text.toString().trim(),
                                )

                    }
                }
                updateTaskDialog.show()
            }
        }
        mainBinding.taskRV.adapter=taskRVVBListAdapter
        taskRVVBListAdapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver()
        {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                mainBinding.taskRV.smoothScrollToPosition(positionStart)
            }
        })
        // Get task list
        callGetTaskList(taskRVVBListAdapter)
        taskViewModel.getTaskList()
        statusCallBack()
        callSearch()


    }

    private fun callSearch() {
        mainBinding.edSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun afterTextChanged(query: Editable) {
                if(query.toString().isNotEmpty()){
                    taskViewModel.searchTaskList(query.toString())
                }else{
                    taskViewModel.getTaskList()
                }
            }

        })
        mainBinding.edSearch.setOnEditorActionListener {v,actionId,event->
            if (actionId== EditorInfo.IME_ACTION_SEARCH){
                hidekeyboard(v)
                return@setOnEditorActionListener true

            }
            false
        }
    }

    private fun statusCallBack(){
        taskViewModel.statusLiveData
            .observe(this){
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        when(it.data as StatusResult) {
                            Added -> {
                                Log.d("StatusResult","Added")
                            }
                            Deleted->{
                                Log.d("StatusResult","Deleted")
                            }
                            Updated->{
                                Log.d("StatusResult","Updated")
                            }
                        }
                        it.message?.let { it1 -> longToastShow(it1) }
                    }
                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }
                    }

                }
            }

    }



    private fun callGetTaskList(taskRecyclerViewAdapter: TaskRVVBListAdapter) {

        CoroutineScope(Dispatchers.Main).launch {
            taskViewModel.taskStateFlow
                .collectLatest{
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.collect{taskList->

                            taskRecyclerViewAdapter.submitList(taskList)
                        }
                    }
                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }
                    }

                }


            }

        }


    }
}
