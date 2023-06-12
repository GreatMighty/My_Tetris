package com.example.my_tetris.models

enum class Shape (val frameCount: Int, val startPosition: Int) {
    Tetromino(2, 2) {
        override fun getFrame(frameNumber: Int, ): Frame{
            return when (frameNumber) {
                0 -> Frame(4).addRow("1111")
                1 -> Frame(1)
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number")
            }
        }
    };
    abstract fun getFrame(frameNumber: Int): Frame


}