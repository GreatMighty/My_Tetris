package com.example.my_tetris.models

import android.graphics.Point
import com.example.my_tetris.constants.FieldConstants
import com.example.my_tetris.helpers.array2dOfByte
import com.example.my_tetris.Storage.AppPreferences

class AppModel {
    var score: Int = 0
    private var preferences: AppPreferences? = null

    var currentBlock: Block? = null
    var currentState: String = Statuses.AWAITING_START.name

    private var field: Array<ByteArray> = array2dOfByte(
        FieldConstants.ROW_COUNT.value,
        FieldConstants.COLOMN_COUNT.value
    )
    fun isGameOver(): Boolean{
        return currentState == Statuses.OVER.name
    }
    fun isGameActive(): Boolean{
        return currentState == Statuses.ACTIVE.name
    }
    fun isGameWaitingStart(): Boolean{
        return  currentState == Statuses.AWAITING_START.name
    }
    enum class Statuses {
        AWAITING_START, ACTIVE, INSACTATE, OVER
    }

    enum class Motioins {
        LEFT, RIGHT, DOWN, ROTATE
    }
    fun setPreferences(preferences: AppPreferences?){
        this.preferences = preferences
    }

    fun getCellStatuses(row: Int, column: Int): Byte? {
        return field[row][column]
    }
    private fun setCellStatus (row: Int,column: Int,status: Byte?){
        if (status != null)
            field[row][column] = status
    }

    private fun boostScore(){
        score += 10
        if (score > preferences?.getHiScore() as Int)
            preferences?.saveHighScore(score)
    }
    private fun generateNextBlock(){
        currentBlock = Block.createBlock()
    }

}