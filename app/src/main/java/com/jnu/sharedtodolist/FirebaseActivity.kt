package com.jnu.sharedtodolist

import android.database.sqlite.SQLiteDoneException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_firebase.*
import kotlinx.android.synthetic.main.activity_main.*

class FirebaseActivity : AppCompatActivity(), TodoRecyclerviewInterface {

    companion object{
        const val TAG: String = "로그"
    }

    private lateinit var database: DatabaseReference
    private lateinit var todoReference: DatabaseReference
    private  var myTodoList = ArrayList<Todo>()

    private lateinit var todoListRecyclerViewAdapter: TodoListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase)

        Log.d(TAG,"FirebaseActivity - onCreate() called")



        //데이터베이스 가져오기
        database = FirebaseDatabase.getInstance().reference

        todoReference = database.child("todos")



        // 리사이클러뷰 어답터 준비
        todoListRecyclerViewAdapter = TodoListRecyclerViewAdapter(this)

//        todoListRecyclerViewAdapter.submitTodoList(myTodoList)
        // 밑에와 같은의미이다 아래로쓸꺼면 어뎁터의 서브밋 함수를 만들지 않아도 된다.
        // 기관총에 장전함
        todoListRecyclerViewAdapter.todoList = myTodoList

        // 데이터를 맨위부터 채운다.
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        linearLayoutManager.stackFromEnd = true

        todo_list_recycler_view_firebase.apply {
            adapter = todoListRecyclerViewAdapter
            layoutManager = linearLayoutManager
            // 맨처음 실행했을때 제일 최신 목록이 제일 위로 쫙 올라오게 한다.
            this.scrollToPosition(todoListRecyclerViewAdapter.itemCount -1)
        }

        add_todo_btn_firebase.setOnClickListener {
            val contentString = new_todo_edit_text_firebase.text.toString()

            addNewTodo(contentString)

            new_todo_edit_text_firebase.setText("")

//        todo_list_recycler_view.adapter = todoListRecyclerViewAdapter
//        todo_list_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            // 위에거랑 같은거다.
        }

        // 데이터 변경에 대한 리스너 설정 - 파이어베이스
        todoReference.addValueEventListener(object : ValueEventListener {
            // 데이터 3개를 가져온다.
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                myTodoList.clear()

                for (todoSnapshot in dataSnapshot.children) {
                    val todoItem = todoSnapshot.getValue(Todo::class.java) as Todo
                    todoItem.uid = todoSnapshot.key
                    myTodoList.add(todoItem)
                    Log.d(TAG,"FirebaseActivity - onDataChange() called / todoItem : ${todoItem.content} ")
                    Log.d(TAG,"FirebaseActivity - onDataChange() called/ todoItem : ${todoItem.uid} ")
                    // 데이터 변경되었따는걸 알려준다.
                    // 리사이클러뷰에서 데이터가 변경되었다는걸 어답터한테 알려주는게 notifyDataSetChanged 이다.
                    // notifyDataSetChanged 하면 전체 데이터가 변경되었다는걸 바로 알려준다.
                }
                todoListRecyclerViewAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })

    }

    private fun addNewTodo(content: String) {

        val newTodo = Todo(content)
        database.child("todos").push().setValue(newTodo).addOnSuccessListener {
            Log.d(TAG,"FirebaseAtivity - 할일 추가 성공")
        }.addOnFailureListener {
            Log.d(TAG,"FirebaseActivity - 할일 추가 실패")
        }
        // 데이터 변경에 대한 리스너 설정 - 파이어 베이스
        // My top posts by number of stars


        // 글자가 있을떄만 추가하기 버튼 나오기
       new_todo_edit_text_firebase.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                Log.d("텍스트입력", "FirebaseActivity - afterTextChanged() called")
                if(!new_todo_edit_text_firebase.text.isNullOrEmpty()){
                    add_todo_btn_firebase.visibility = View.VISIBLE
                } else {
                    add_todo_btn_firebase.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("텍스트입력", "FirebaseActivity - beforeTextChanged() called")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("텍스트입력", "FirebaseActivity - onTextChanged() called")
            }

        })



    }


    // 아이템 값 변경 - 파이어 베이스
    private fun updateTodoItem(uid: String, isDone: Boolean){
        todoReference.child(uid).child("done").setValue(isDone)
    }


    // 아이템 변경
    override fun onTodoItemChanged(position: Int, isDone: Boolean) {
        Log.d(TAG,"FirebaseActivity - onTodoItemChanged() called / $position ")

        val selectItemUidString = this.myTodoList[position].uid as String

        // 파이어베이스 아이템 값 설정
       updateTodoItem(selectItemUidString, isDone)
    }

    // 아이템 삭제 버튼 클릭
    override fun onTodoItemDeleted(position: Int) {
        Log.d(TAG,"FirebaseActivity - onTodoItemDeleted() called / $position ")

        val selectItemUidString = this.myTodoList[position].uid as String

        todoReference.child(selectItemUidString).removeValue().addOnSuccessListener { 
            Toast.makeText(this, " 성공적으로 삭제 했습니다", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { 
            Log.d(TAG,"FirebaseActivity - 실패 : itprintStackTrace() : ${it.printStackTrace()}")
        }

    }
}