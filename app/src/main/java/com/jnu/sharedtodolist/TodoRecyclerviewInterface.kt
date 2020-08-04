package com.jnu.sharedtodolist

interface TodoRecyclerviewInterface {

    // 할일 아이템이 변경되었을때
    fun onTodoItemChanged(position: Int, isDone: Boolean)

}