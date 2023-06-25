package com.example.my_tetris.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.example.my_tetris.constants.CellConstants
import com.example.my_tetris.GameActivity
import com.example.my_tetris.constants.FieldConstants
import com.example.my_tetris.models.AppModel
import com.example.my_tetris.models.Block

class TetrisView : View {
    private val paint = Paint()
    private var lastMove: Long = 0
    private var model: AppModel? = null
    private var activity: GameActivity? = null
    private val viewHandler = ViewHandler(this)
    private var cellSize: Dimension = Dimension(0, 0)
    private var frameOffset: Dimension = Dimension(0, 0)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int):
            super(context, attrs, defStyle)

    companion object {
        private val DELAY = 500
        private val BLOCL_OFFSET = 2
        private val FRAME_OFFSET_BASE = 10
    }

    private class ViewHandler (private val owner: TetrisView): Handler(){
        override fun handleMessage (message: Message){
         if (message.what == 0) {
               if (owner.model != null) {
                if (owner.model!!.isGameOver()){
                    owner.model?.endGame()
                    Toast.makeText(owner.activity, "Ты проиграл", Toast.LENGTH_LONG).show();
                }
                   if (owner.model!!.isGameActive()) {
                       owner.setGameCommandWithDelay(AppModel.Motioins.DOWN)
                   }

            }
        }
        }
        fun sleep (delay: Long){
            this.removeMessages(0)
            sendMessageDelayed(obtainMessage(0), delay)
        }
    }

    private data class Dimension(val width: Int, val hegth: Int)

    fun setModel(model: AppModel){
        this.model = model
    }

    fun setActivity (gameActivity: GameActivity){
        this.activity = gameActivity
    }

    fun setGameCommand (move: AppModel.Motioins) {
        if (null != model && (model?.currentState ==
                    AppModel.Statuses.ACTIVE.name)) {
            if (AppModel.Motioins.DOWN == move) {
                model?.generateField(move.name)
                invalidate()
                return
            }
            setGameCommandWithDelay(move)
        }
    }

    fun setGameCommandWithDelay(move: AppModel.Motioins){
        val now = System.currentTimeMillis()
        if (now - lastMove > DELAY) {
            model?.generateField(move.name)
            invalidate()
            lastMove = now
        }
        updateScore()
        viewHandler.sleep(DELAY.toLong())
    }

    private fun updateScore() {
        activity?.tvCurrentScore?.text = "${model?.score}"
        activity?.tvHignScore?.text = "${activity?.appPreferences?.getHiScore()}"
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawFrame(canvas)
        if (model != null){
            for (i in 0 until FieldConstants.ROW_COUNT.value){
                for (j in 0 until FieldConstants.COLOMN_COUNT.value){
                    drawCell (canvas, i , j)
                }
            }
        }
    }
    private fun drawFrame (canvas: Canvas){
        paint.color = Color.LTGRAY
        canvas.drawRect(frameOffset.width.toFloat(),
        frameOffset.hegth.toFloat(), width -
        frameOffset.width.toFloat(),
        height - frameOffset.hegth.toFloat(), paint)
    }
    private fun drawCell(canvas: Canvas, row: Int, col: Int){
        val cellStatus = model?.getCellStatuses(row,col)
        if (CellConstants.EMPTY.value != cellStatus){
            val color = if (CellConstants.EPHEMERAL.value == cellStatus){
                model?.currentBlock?.color
            }else {
                Block.getColor(cellStatus as Byte)
            }
            drawCell(canvas,col, color as Int)
        }
    }
    private fun drawCell(canvas: Canvas, x: Int, y: Int, rgbColor: Int){
        paint.color = rgbColor
        val top: Float = (frameOffset.hegth + y * cellSize.hegth + BLOCL_OFFSET).toFloat()
        val left: Float = (frameOffset.width + x * cellSize.width + BLOCL_OFFSET).toFloat()
        val bottom: Float = (frameOffset.hegth + (y + 1) * cellSize.hegth + BLOCL_OFFSET).toFloat()
        val right: Float = (frameOffset.width + (x + 1)* cellSize.width + BLOCL_OFFSET).toFloat()
        val rectangle = RectF(left,top,right,bottom)
        canvas.drawRoundRect(rectangle, 4F, 4F, paint)
    }

    override fun onSizeChanged(width: Int, hegth: Int, previousWidth: Int, previousHeigth: Int) {
        super.onSizeChanged(width, hegth, previousWidth, previousHeigth)
        val cellWidth = (width - 2 * FRAME_OFFSET_BASE) / FieldConstants.COLOMN_COUNT.value
        val  cellHeigth = (hegth - 2 * FRAME_OFFSET_BASE) / FieldConstants.ROW_COUNT.value
        val n = Math.min(cellWidth, cellHeigth)
        this.cellSize = TetrisView.Dimension(n, n)
        val offsetX = (width - FieldConstants.COLOMN_COUNT.value * n)/ 2
        val offsetY = (hegth - FieldConstants.ROW_COUNT.value * n) / 2
        this.frameOffset = TetrisView.Dimension(offsetX, offsetY)
    }
}