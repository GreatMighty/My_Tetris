package com.example.my_tetris

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.my_tetris.Storage.AppPreferences

class GameActivity: AppCompatActivity() {
    var tvHignScore: TextView? = null
    var tvCurrentScore: TextView? =null
    var appPreferences: AppPreferences? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)
        appPreferences = AppPreferences(this)

        val btnRestart = findViewById<Button>(R.id.btn_reset)
        tvHignScore = findViewById<TextView>(R.id.tv_high_score)
        tvCurrentScore = findViewById<TextView>(R.id.tv_current_score)

        updateHighScore()
        updateCurrentScore()
    }

    private fun updateHighScore(){
        tvHignScore?.text = "${appPreferences?.getHiScore()}"
    }
    private fun updateCurrentScore(){
        tvCurrentScore?.text = "0"
    }
}