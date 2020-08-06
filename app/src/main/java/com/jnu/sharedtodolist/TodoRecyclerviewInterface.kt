package com.jnu.sharedtodolist

interface TodoRecyclerviewInterface {

    // 할일 아이템이 변경되었을때
    fun onTodoItemChanged(position: Int, isDone: Boolean)

    // 할일 아이템이 삭제버튼 클릭
    fun onTodoItemDeleted(position: Int)

}