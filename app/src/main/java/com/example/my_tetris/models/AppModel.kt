package com.example.my_tetris.models

import android.graphics.Point
import com.example.my_tetris.constants.FieldConstants
import com.example.my_tetris.helpers.array2dOfByte
import com.example.my_tetris.Storage.AppPreferences
import com.example.my_tetris.constants.CellConstants

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

    private fun boostScore(){
        score += 10
        if (score > preferences?.getHiScore() as Int)
            preferences?.saveHighScore(score)
    }

    private fun generateNextBlock(){
        currentBlock = Block.createBlock()
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

    private fun validTransition(position: Point, shape: Array<ByteArray>):Boolean {
        return if (position.y < 0 || position.x >0){
            false
        }else if (position.y + shape.size > FieldConstants.ROW_COUNT.value){
            false
        }else if (position.x + shape[0].size > FieldConstants.COLOMN_COUNT.value){
            false
        }else {
            for (i in 0 until shape.size){
                for (j in 0 until shape[i].size) {
                    val y = position.y + i
                    val x = position.x + j
                    if (CellConstants.EMPTY.value != shape[i][i] &&
                        CellConstants.EMPTY.value != field[y][x]) {
                        return false
                    }
                }
            }
            true
        }
    }

    private fun moveValid(position: Point, frameNumber: Int?): Boolean{
        val shape: Array<ByteArray>? = currentBlock?.getShape(frameNumber as Int)
        return validTransition(position,shape as Array<ByteArray>)
    }

    fun generateField (action: String){
        if (isGameActive()){
            resetField()
            var frameNumber: Int? = currentBlock?.frameNumber
            val coordinate: Point? = Point()
            coordinate?.x = currentBlock?.position?.x
            coordinate?.y = currentBlock?.position?.y

            when(action) {
                Motioins.LEFT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.minus(1)
                }

                Motioins.RIGHT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.plus(1)
                }

                Motioins.DOWN.name -> {
                    coordinate?.y = currentBlock?.position?.y?.plus(1)
                }

                Motioins.ROTATE.name -> {
                    frameNumber = frameNumber?.plus(1)
                    if (frameNumber != null) {
                        if (frameNumber >= currentBlock?.frameNumber as Int) {
                            frameNumber = 0
                        }
                    }

                }
            }
            if (!moveValid(coordinate as Point, frameNumber)){
            translateBlock(currentBlock?.position as Point,currentBlock?.frameNumber as Int)
            if (Motioins.DOWN.name == action){
                boostScore()
                persisCellData()
                assessField()
                generateNextBlock()
                if (!blockAddiyionPossible()){
                    currentState = Statuses.OVER.name;
                    currentBlock = null;
                    resetField(false);
                }
            }
         }else {
             if (frameNumber != null){
                 translateBlock(coordinate, frameNumber)
                 currentBlock?.setState(frameNumber, coordinate)
             }
            }
        }
    }

    private fun resetField (aphemeralCellsOnly: Boolean = true){
        for (i in 0 until FieldConstants.ROW_COUNT.value){
            (0 until FieldConstants.COLOMN_COUNT.value)
                .filter { !aphemeralCellsOnly || field[i][it] == CellConstants.EPHEMERAL.value}
                .forEach { field[i][it] = CellConstants.EMPTY.value }
        }
    }

    private fun persisCellData() {
        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
                var status = getCellStatuses(i, j)
                if (status == CellConstants.EPHEMERAL.value){
                    status = currentBlock?.staticValue
                    setCellStatus(i, j , status)
                }
            }
        }
    }
    private fun assessField() {
        for (i in 0 until field.size){
            var emptyCells = 0;
            for (j in 0 until field[i].size){
                val status = getCellStatuses(i, j)
                val isEmpty = CellConstants.EMPTY.value == status
                if (isEmpty)
                    emptyCells++
            }
            if (emptyCells == 0)
                shiftRows(i)
        }
    }

    private fun translateBlock(position: Point, frameNumber: Int){
        synchronized(field){
            val shape: Array<ByteArray>? = currentBlock?.getShape(frameNumber)
            if (shape != null) {
                for (i in shape.indices){
                    for (j in 0 until shape[i].size){
                        val y = position.y + i
                        val x = position.x + j
                        if (CellConstants.EMPTY.value != shape[i][j]){
                            field[y][x] = shape [i][j]
                        }
                    }
                }
            }
        }
    }

    private fun blockAddiyionPossible(): Boolean{
        if (!moveValid(currentBlock?.position as Point,currentBlock?.frameNumber)){
            return false
        }
        return true
    }

    private fun shiftRows(nToRow: Int) {
        if (nToRow > 0) {
            for (j in nToRow - 1 downTo 0) {
                for (m in 0 until field[j].size) {
                    setCellStatus(j + 1, m, getCellStatuses(j, m))
                }
            }
        }
        for (j in 0 until field[0].size){
            setCellStatus(0,j,CellConstants.EMPTY.value)
        }
    }

    fun startGame(){
        if (!isGameActive()){
            currentState = Statuses.ACTIVE.name
            generateNextBlock()
        }
    }
    fun restartGame(){
        resetModel()
        startGame()
    }
    fun endGame(){
        score = 0
        currentState = AppModel.Statuses.OVER.name
    }
    private fun resetModel() {
        resetField(false)
        currentState = Statuses.AWAITING_START.name
        score = 0
    }

}
