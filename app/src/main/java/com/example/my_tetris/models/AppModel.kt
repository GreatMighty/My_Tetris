package com.example.my_tetris.models

import android.graphics.Point
import com.example.my_tetris.constants.CellConstants
import com.example.my_tetris.constants.FieldConstants
import com.example.my_tetris.helpers.array2dOfByte
import com.example.my_tetris.Storage.AppPreferences

class AppModel {

    var score: Int = 0
        private set
    private var preferences: AppPreferences? = null

    var currentBlock: Block? = null
        private set
    var currentState: Statuses = Statuses.AWAITING_START
        private set

    private var field: Array<ByteArray> = array2dOfByte(
        FieldConstants.ROW_COUNT.value,
        FieldConstants.COLOMN_COUNT.value
    )

    enum class Statuses {
        AWAITING_START, ACTIVE, OVER
    }

    enum class Motions {
        LEFT, RIGHT, DOWN, ROTATE
    }

    fun isGameOver() = currentState == Statuses.OVER
    fun isGameActive() = currentState == Statuses.ACTIVE
    fun isGameWaitingStart() = currentState == Statuses.AWAITING_START

    fun setPreferences(preferences: AppPreferences) {
        this.preferences = preferences
    }

    private fun boostScore() {
        score += 10
        preferences?.let {
            if (score > it.getHiScore()) it.saveHighScore(score)
        }
    }

    private fun generateNextBlock() {
        currentBlock = Block.createBlock()
    }

    fun getCellStatus(row: Int, column: Int) = field[row][column]

    private fun setCellStatus(row: Int, column: Int, status: Byte) {
        field[row][column] = status
    }

    private fun validTransition(position: Point, shape: Array<ByteArray>): Boolean {
        // Проверка выхода за границы поля
        if (position.x < 0 || position.y < 0) return false
        if (position.x + shape[0].size > FieldConstants.COLOMN_COUNT.value) return false
        if (position.y + shape.size > FieldConstants.ROW_COUNT.value) return false

        // Проверка столкновений с другими блоками
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                val y = position.y + i
                val x = position.x + j
                if (shape[i][j] != CellConstants.EMPTY.value && field[y][x] != CellConstants.EMPTY.value) {
                    return false
                }
            }
        }
        return true
    }

    private fun moveValid(position: Point, frameNumber: Int) =
        currentBlock?.getShape(frameNumber)?.let { validTransition(position, it) } ?: false

    fun generateField(action: Motions) {
        if (!isGameActive()) return

        resetField() // удалить эпемерные ячейки
        var frameNumber = currentBlock?.frameNumber ?: 0
        val coordinate = Point(currentBlock?.position ?: Point(0, 0))

        // Рассчитываем новые координаты или вращение
        when (action) {
            Motions.LEFT -> coordinate.x--
            Motions.RIGHT -> coordinate.x++
            Motions.DOWN -> coordinate.y++
            Motions.ROTATE -> {
                frameNumber++
                val maxFrame = currentBlock?.getFrameCount() ?: 1
                if (frameNumber >= maxFrame) frameNumber = 0
            }
        }

        if (!moveValid(coordinate, frameNumber)) {
            // Если движение недопустимо
            translateBlock(currentBlock!!.position, currentBlock!!.frameNumber)
            if (action == Motions.DOWN) {
                boostScore()
                persistCellData()
                assessField()
                generateNextBlock()
                if (!blockAdditionPossible()) {
                    currentState = Statuses.OVER
                    currentBlock = null
                    resetField(false)
                }
            }
        } else {
            // Перемещаем или вращаем блок
            translateBlock(coordinate, frameNumber)
            currentBlock?.setState(frameNumber, coordinate)
        }
    }

    private fun resetField(onlyEphemeral: Boolean = true) {
        for (i in field.indices) {
            for (j in field[i].indices) {
                if (!onlyEphemeral || field[i][j] == CellConstants.EPHEMERAL.value) {
                    field[i][j] = CellConstants.EMPTY.value
                }
            }
        }
    }

    private fun persistCellData() {
        currentBlock?.let { block ->
            val shape = block.getShape(block.frameNumber)
            val pos = block.position
            for (i in shape.indices) {
                for (j in shape[i].indices) {
                    val y = pos.y + i
                    val x = pos.x + j
                    if (shape[i][j] != CellConstants.EMPTY.value) {
                        field[y][x] = block.getStaticValue()
                    }
                }
            }
        }
    }

    private fun assessField() {
        for (i in field.indices) {
            if (field[i].all { it != CellConstants.EMPTY.value }) {
                shiftRows(i)
            }
        }
    }

    private fun translateBlock(position: Point, frameNumber: Int) {
        currentBlock?.getShape(frameNumber)?.let { shape ->
            for (i in shape.indices) {
                for (j in shape[i].indices) {
                    val y = position.y + i
                    val x = position.x + j
                    if (shape[i][j] != CellConstants.EMPTY.value) {
                        field[y][x] = CellConstants.EPHEMERAL.value
                    }
                }
            }
        }
    }

    private fun blockAdditionPossible(): Boolean {
        val block = currentBlock ?: return false
        return moveValid(block.position, block.frameNumber)
    }

    private fun shiftRows(rowToShift: Int) {
        for (i in rowToShift downTo 1) {
            field[i] = field[i - 1].copyOf()
        }
        field[0] = ByteArray(FieldConstants.COLOMN_COUNT.value) { CellConstants.EMPTY.value }
    }

    fun startGame() {
        if (!isGameActive()) {
            currentState = Statuses.ACTIVE
            generateNextBlock()
        }
    }

    fun restartGame() {
        resetModel()
        startGame()
    }

    fun endGame() {
        score = 0
        currentState = Statuses.OVER
    }

    private fun resetModel() {
        resetField(false)
        score = 0
        currentState = Statuses.AWAITING_START
        currentBlock = null
    }
}
