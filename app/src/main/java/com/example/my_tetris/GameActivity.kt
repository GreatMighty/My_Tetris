package com.example.my_tetris

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.my_tetris.models.AppModel
import com.example.my_tetris.Storage.AppPreferences
import com.example.my_tetris.view.TetrisView

class GameActivity: AppCompatActivity() {
    var tvHignScore: TextView? = null
    var tvCurrentScore: TextView? =null

    private lateinit var tetrisView: TetrisView
    var appPreferences: AppPreferences? = null
    private val appModel: AppModel = AppModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)
        appPreferences = AppPreferences(this)
        appModel.setPreferences(appPreferences)

        val btnRestart = findViewById<Button>(R.id.btn_reset)
        tvHignScore = findViewById<TextView>(R.id.tv_high_score)
        tvCurrentScore = findViewById<TextView>(R.id.tv_current_score)
        tetrisView = findViewById<TetrisView>(R.id.view_tetris)
        tetrisView.setActivity(this)
        tetrisView.setModel(appModel)
        tetrisView.setOnTouchListener(this::onTetrisViewTouch)
        btnRestart.setOnClickListener(this::btnRestartClick)
        updateHighScore()
        updateCurrentScore()
    }
    private fun btnRestartClick(view: View){
        appModel.restartGame()
    }

    private fun onTetrisViewTouch(view: View, event: MotionEvent):
            Boolean{
        if (appModel.isGameOver() || appModel.isGameWaitingStart()){
            appModel.startGame()
            tetrisView.setGameCommandWithDelay(AppModel.Motioins.DOWN)
        }else if (appModel.isGameActive()){
            when(resolveTouchDirection(view, event)){
                0-> moveTetromino(AppModel.Motioins.LEFT)
                1-> moveTetromino(AppModel.Motioins.ROTATE)
                2-> moveTetromino(AppModel.Motioins.DOWN)
                3-> moveTetromino(AppModel.Motioins.RIGHT)
            }
        }
        return true
    }
    private fun resolveTouchDirection (view: View, event: MotionEvent):Int{
        val x = event.x / view.width
        val y = event.y / view.height
        val direction: Int
        direction =  if (y > x) {
            if (x > 1 - y) 2 else 0
        }
        else{
            if (x > 1 - y ) 3 else 1
        }
        return direction
    }

    private fun moveTetromino (motion: AppModel.Motioins){
        if (appModel.isGameActive()){
            tetrisView.setGameCommand(motion)
        }
    }

    private fun updateHighScore(){
        tvHignScore?.text = "${appPreferences?.getHiScore()}"
    }
    private fun updateCurrentScore(){
        tvCurrentScore?.text = "0"
    }
}