package com.example.my_tetris.models

enum class Shape(val frameCount: Int, val startPosition: Int) {

    Tetromino1(1, 1) {
        override fun getFrame(frameNumber: Int): Frame {
            return Frame(2)
                .addRow("11")
                .addRow("11")
        }
    },

    Tetromino2(2, 1) {
        override fun getFrame(frameNumber: Int): Frame {
            val f = frameNumber % frameCount
            return when (f) {
                0 -> Frame(3)
                    .addRow("110")
                    .addRow("011")
                1 -> Frame(2)
                    .addRow("01")
                    .addRow("11")
                    .addRow("10")
                else -> Frame(3) // никогда сюда не попадём
            }
        }
    },

    Tetromino3(2, 1) {
        override fun getFrame(frameNumber: Int): Frame {
            val f = frameNumber % frameCount
            return when (f) {
                0 -> Frame(3)
                    .addRow("110")
                    .addRow("011")
                1 -> Frame(2)
                    .addRow("01")
                    .addRow("11")
                    .addRow("10")
                else -> Frame(3)
            }
        }
    },

    Tetromino4(2, 2) {
        override fun getFrame(frameNumber: Int): Frame {
            val f = frameNumber % frameCount
            return when (f) {
                0 -> Frame(4).addRow("1111")
                1 -> Frame(2)
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                else -> Frame(4)
            }
        }
    },

    Tetromino5(4, 1) {
        override fun getFrame(frameNumber: Int): Frame {
            val f = frameNumber % frameCount
            return when (f) {
                0 -> Frame(3)
                    .addRow("010")
                    .addRow("111")
                1 -> Frame(2)
                    .addRow("10")
                    .addRow("11")
                    .addRow("10")
                2 -> Frame(3)
                    .addRow("1")
                    .addRow("1")
                3 -> Frame(2)
                    .addRow("01")
                    .addRow("11")
                    .addRow("01")
                else -> Frame(3)
            }
        }
    },

    Tetromino6(4, 1) {
        override fun getFrame(frameNumber: Int): Frame {
            val f = frameNumber % frameCount
            return when (f) {
                0 -> Frame(3)
                    .addRow("100")
                    .addRow("111")
                1 -> Frame(2)
                    .addRow("11")
                    .addRow("10")
                    .addRow("10")
                2 -> Frame(3)
                    .addRow("111")
                    .addRow("001")
                3 -> Frame(2)
                    .addRow("01")
                    .addRow("01")
                    .addRow("11")
                else -> Frame(3)
            }
        }
    },

    Tetromino7(4, 1) {
        override fun getFrame(frameNumber: Int): Frame {
            val f = frameNumber % frameCount
            return when (f) {
                0 -> Frame(3)
                    .addRow("001")
                    .addRow("111")
                1 -> Frame(2)
                    .addRow("10")
                    .addRow("10")
                    .addRow("11")
                2 -> Frame(3)
                    .addRow("111")
                    .addRow("100")
                3 -> Frame(2)
                    .addRow("11")
                    .addRow("01")
                    .addRow("01")
                else -> Frame(3)
            }
        }
    };

    abstract fun getFrame(frameNumber: Int): Frame
}
