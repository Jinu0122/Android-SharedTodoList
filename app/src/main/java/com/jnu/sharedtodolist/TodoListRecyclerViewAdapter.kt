package com.jnu.sharedtodolist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_todo_item.view.*

class TodoListRecyclerViewAdapter(todoRecyclerviewInterface: TodoRecyclerviewInterface): RecyclerView.Adapter<TodoItemViewHolder>() {

    companion object{
        const val TAG: String = "로그"
    }

    var todoList = ArrayList<Todo>()

    private var todoRecyclerviewInterface : TodoRecyclerviewInterface? = null
    init {
        Log.d(TAG,"TodoListRecyclerViewAdapter - init() called")
        this.todoRecyclerviewInterface = todoRecyclerviewInterface

    }

    // 어떤 레이아웃
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        return TodoItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_todo_item, parent, false)
            , this.todoRecyclerviewInterface!!
        )

    }

    // 목록의 갯수
    override fun getItemCount(): Int {
        return this.todoList.size
    }

    // 뿌려주는곳
    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {

        //데이터가 묶인다.
        val todoItem = this.todoList[position]

        //        val todoItem = this.todoList[0]
        holder.itemView.todo_content_text.text = todoItem.content
        holder.bindWithView(todoItem)

    }

    fun submitTodoList(todoListFromActivity: ArrayList<Todo>){
        this.todoList = todoListFromActivity
    }


}