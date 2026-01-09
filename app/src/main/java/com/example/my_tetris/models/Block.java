package com.example.my_tetris.models;

import android.graphics.Color;
import android.graphics.Point;
import androidx.annotation.NonNull;

import com.example.my_tetris.constants.FieldConstants;

import java.util.Random;

public class Block {
    private int shapeIndex;
    private int frameNumber;
    private BlockColor color;
    private Point position;

    // Конструктор
    private Block(int shapeIndex, BlockColor blockColor){
        this.shapeIndex = shapeIndex;
        this.color = blockColor;
        this.frameNumber = 0;
        this.position = new Point(
                FieldConstants.COLOMN_COUNT.getValue() / 2 - Shape.values()[shapeIndex].getStartPosition(),
                0
        );
    }

    // Создание нового случайного блока
    public static Block createBlock(){
        Random random = new Random();
        int shapeIndex = random.nextInt(Shape.values().length);
        BlockColor blockColor = BlockColor.values()[random.nextInt(BlockColor.values().length)];
        return new Block(shapeIndex, blockColor);
    }

    // Цвет блока
    public enum BlockColor {
        PINK(Color.rgb(255,105,180), (byte) 2),
        GREEN(Color.rgb(0,128,0), (byte) 3),
        ORANGE(Color.rgb(255,140,0), (byte) 4),
        YELLOW(Color.rgb(255,255,0), (byte) 5),
        CYAN(Color.rgb(0,255,255), (byte) 6);

        private final int rgbValue;
        private final byte byteValue;

        BlockColor(int rgbValue, byte value){
            this.rgbValue = rgbValue;
            this.byteValue = value;
        }

        public int getRgbValue() { return rgbValue; }
        public byte getByteValue() { return byteValue; }
    }

    // Получить RGB по byteValue
    public static int getRgbColor(byte value) {
        for (BlockColor color : BlockColor.values()){
            if (value == color.getByteValue()){
                return color.getRgbValue();
            }
        }
        return Color.BLACK;
    }

    // Получить byteValue по byteValue (может пригодиться)
    public static byte getByteValue(byte value) {
        for (BlockColor color : BlockColor.values()){
            if (value == color.getByteValue()){
                return color.getByteValue();
            }
        }
        return 0;
    }

    // Установка состояния блока (рамка и позиция)
    public final void setState(int frame, Point position){
        this.frameNumber = frame;
        this.position = new Point(position); // создаем копию, чтобы избежать мутации внешнего объекта
    }

    // Получить форму блока в виде 2D массива byte
    @NonNull
    public final byte[][] getShape(int frameNumber){
        int normalizedFrame = frameNumber % Shape.values()[shapeIndex].getFrameCount();
        return Shape.values()[shapeIndex].getFrame(normalizedFrame).as2dByteArray();
    }

    // Текущая позиция блока
    public Point getPosition() {
        return new Point(this.position); // возвращаем копию для безопасности
    }

    // Текущий frameNumber
    public final int getFrameNumber(){
        return this.frameNumber;
    }

    // Цвет RGB для отрисовки
    public int getColor(){
        return color.getRgbValue();
    }

    // Статическое значение для ячеек поля
    public byte getStaticValue() {
        return color.getByteValue();
    }
}
