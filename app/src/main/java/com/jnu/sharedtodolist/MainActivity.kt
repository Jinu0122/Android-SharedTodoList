package com.jnu.sharedtodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TodoRecyclerviewInterface {

    companion object{
        const val TAG: String = "로그"
    }

    private  var myTodoList = ArrayList<Todo>()

    private lateinit var todoListRecyclerViewAdapter: TodoListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"MainActivity - onCreate() called")

        // 버튼 클릭 했을때
        add_todo_btn.setOnClickListener {
            Log.d(TAG,"MainActivity - 할일 추가 버튼 클릭")

//            if(new_todo_edit_text.text.isEmpty()){
//
//
//            }


            val newTodoContentText = new_todo_edit_text.text.toString()

            val newTodo = Todo(newTodoContentText)

            new_todo_edit_text.setText("")

            //
            myTodoList.add(newTodo)

            // 추가 데이터 저장된
            // 뮤터블은 어레이 리스트를 포함하기때문에 따로 형변환이 필요가 없다.
            SharedManager.storeTodoList(myTodoList)

            // 안넣어도됨                //데이터가 변경되었다는걸 알려준다.
            todoListRecyclerViewAdapter.notifyDataSetChanged()

            // 새로 추가 하면 맨위에 목록을 보여준다.
           todo_list_recycler_view.scrollToPosition(todoListRecyclerViewAdapter.itemCount -1)

        }

//        myTodoList.add(Todo("hoho"))
//        myTodoList.add(Todo("hoho"))
//        myTodoList.add(Todo("hoho"))
//
//        // 데이터가 없어서 오류나기때문에 잠시만 하나 넣어준다
//         SharedManager.storeTodoList(myTodoList)

        // 저장된 목록 가져오기
        // myTodoList 는 ArrayList 이고 getTodoList은  MutableList 이기때문에 형변환해줘야함
        myTodoList = SharedManager.getTodoList() as ArrayList<Todo>


        // 리사이클러뷰 어답터 준비
        todoListRecyclerViewAdapter = TodoListRecyclerViewAdapter(this)

//        todoListRecyclerViewAdapter.submitTodoList(myTodoList)
        // 밑에와 같은의미이다 아래로쓸꺼면 어뎁터의 서브밋 함수를 만들지 않아도 된다.
        // 기관총에 장전함
        todoListRecyclerViewAdapter.todoList = myTodoList

        // 데이터를 맨위부터 채운다.
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        linearLayoutManager.stackFromEnd = true

        todo_list_recycler_view.apply {
            adapter = todoListRecyclerViewAdapter
            layoutManager = linearLayoutManager
            // 맨처음 실행했을때 제일 최신 목록이 제일 위로 쫙 올라오게 한다.
            this.scrollToPosition(todoListRecyclerViewAdapter.itemCount -1)
        }
//        todo_list_recycler_view.adapter = todoListRecyclerViewAdapter
//        todo_list_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // 위에거랑 같은거다.

    }

    // 특정 할일 아이템이 변경되었다.
    override fun onTodoItemChanged(position: Int, isDone: Boolean) {
        Log.d(TAG,"MainActivity - onTodoItemChanged() called / position: $position / isDone: $isDone")

        this.myTodoList[position].isDone = isDone

        // 변경된 배열을 저장한다.
        SharedManager.storeTodoList(this.myTodoList)

    }
}