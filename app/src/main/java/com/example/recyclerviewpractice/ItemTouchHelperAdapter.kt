package com.example.recyclerviewpractice

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition : Int, toPosition : Int)
    fun onItemSwiped(position : Int, direction : Int)
}