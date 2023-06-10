package com.example.my_tetris

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.my_tetris.Storage.AppPreferences
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    var tvHiScore: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnNewGame = findViewById<Button>(R.id.button_newGame)
        val btnResetGame = findViewById<Button>(R.id.button_resetScore)
        val btnExit = findViewById<Button>(R.id.button_exit)
        tvHiScore = findViewById<TextView>(R.id.tv_high_score)

        btnNewGame.setOnClickListener(this::onBtnNewGameClick)
        btnResetGame.setOnClickListener(this::onBtnResetScoreClick)
        btnExit.setOnClickListener(this::onBtnExitClick)
    }

    private fun onBtnNewGameClick (view: View){
        val intent = Intent(this@MainActivity, GameActivity::class.java)
        startActivity(intent)
    }

    private fun onBtnResetScoreClick(view: View){
         val preferences = AppPreferences(this)
        preferences.clearHiScore()
        Snackbar.make(view,"Score successfully reset",
        Snackbar.LENGTH_SHORT).show()
    }

    private fun onBtnExitClick(view: View){
        System.exit(0)
    }
}
//114 страница, середина