package com.jnu.sharedtodolist

import android.content.Context
import android.util.Log
import com.google.gson.Gson

object SharedManager {

    // companion 은 클래스 안에서 사용하는 오브젝트를 뜻하는데 지금 오브젝트이기때문에 안써도 된다.
//    companion object{
//        const val TAG: String = "로그"
//    }
    private const val TAG: String = "로그"
    private const val SHARED_TODO_LIST = "shared_todo_list"
    private const val KEY_TODO_LIST = "key_todo_list"

    //할일 목록 저장
    // 일반배열인 ArrayList 가 아닌 MutableList 로 넣는이유는 반환할때 Gson 을 사용할때 반환하면 MutableList 로 나오기떄문이다
    // 또 MutableList 를 ArrayList 바꿀수있다. 원래 ArrayList 크기를 마음대로 바꿀수없다.
    fun storeTodoList(todoList: MutableList<Todo>){
        Log.d(TAG,"SharedManager - storeTodoList() called")

        // 문자열로 변환된 배열
        val todoListString = Gson().toJson(todoList)

        // 쉐어드 가져오기
        val shared = App.instance.getSharedPreferences("SHARED_TODO_LIST", Context.MODE_PRIVATE)

        // 쉐어드에 에디터 가져오기
        val editor = shared.edit()

        // 에디터에 데이터를 넣고
        // 키, 값
        editor.putString(KEY_TODO_LIST, todoListString)

        // 에디터 변경 사항을 적용
        editor.apply()

    }

    // 할일목록 가져오기
    fun getTodoList() : MutableList<Todo> {
        Log.d(TAG,"SharedManager - storeTodoList() called")



        // 쉐어드 가져오기
        val shared = App.instance.getSharedPreferences("SHARED_TODO_LIST", Context.MODE_PRIVATE)
        // 저장되어있던 문자열
        val storedTodoListString = shared.getString(KEY_TODO_LIST,"")!!
        // 가져온 문자열 배열로 변환
        var storedTodoList = ArrayList<Todo>()

        if(storedTodoListString.isNotEmpty()){

            storedTodoList = Gson().fromJson(storedTodoListString, Array<Todo>::class.java).toMutableList() as ArrayList<Todo>

        }

        return storedTodoList

    }

}