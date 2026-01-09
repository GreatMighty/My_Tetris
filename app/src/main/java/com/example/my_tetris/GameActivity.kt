package com.example.my_tetris

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.my_tetris.models.AppModel
import com.example.my_tetris.view.TetrisView
import android.widget.TextView
import android.widget.Button
import android.view.View

class GameActivity : AppCompatActivity() {

    private lateinit var tetrisView: TetrisView
    private lateinit var appModel: AppModel
    private lateinit var tvCurrentScore: TextView
    private lateinit var tvHighScore: TextView
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity) // убедись, что имя layout совпадает!

        // Находим элементы
        tetrisView = findViewById(R.id.view_tetris)
        tvCurrentScore = findViewById(R.id.tv_current_score)
        tvHighScore = findViewById(R.id.tv_high_score)
        btnReset = findViewById(R.id.btn_reset)

        // Создаем модель игры
        appModel = AppModel()
        tetrisView.setAppModel(appModel)

        // Запускаем игру
        appModel.startGame()
        tetrisView.refresh()
        updateScoreViews()

        // Обработка кнопки сброса
        btnReset.setOnClickListener {
            appModel.restartGame()
            tetrisView.refresh()
            updateScoreViews()
        }

        // Здесь можно добавить обработку свайпов/клавиш для движения блока
        // Например: moveLeft(), moveRight(), moveDown(), rotate()
    }

    private fun updateScoreViews() {
        tvCurrentScore.text = appModel.score.toString()
        // если используешь AppPreferences для рекорда
        tvHighScore.text = appModel.getHighScore().toString()
    }
}
