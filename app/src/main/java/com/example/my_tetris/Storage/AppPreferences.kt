package com.example.my_tetris.Storage

import android.content.Context
import android.content.SharedPreferences

class AppPreferences (ctx: Context){

    var data: SharedPreferences = ctx.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

    fun getHiScore():Int{
        return data.getInt("HIGH_SCORE",0)
    }
    fun clearHiScore(){
        data.edit().putInt("HIGH_SCORE",0).apply()
    }
}
