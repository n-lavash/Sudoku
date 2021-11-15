package com.example.mysudoku.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class SudokuBoardView extends View {

    private int cellSize = 3;
    private int size = 9;
    private float sizeCellPixel = 0;
    private int[][] numberArray = new int[size][size];

    private final Paint thickLinePaint = new Paint();

    private final Paint thinLinePaint = new Paint();

    private final Paint selectedCellPaint = new Paint();

    private final Paint conflictCellPaint = new Paint();

    private final Paint numberPaint = new Paint();

    private int selectRow = -1;
    private int selectCol = -1;

    public SudokuBoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int sizePixel = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizePixel, sizePixel);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        sizeCellPixel = (getWidth() / size);

        thickLinePaint.setStyle(Paint.Style.STROKE);
        thickLinePaint.setColor(Color.BLACK);
        thickLinePaint.setStrokeWidth(4);

        thinLinePaint.setStyle(Paint.Style.STROKE);
        thinLinePaint.setColor(Color.BLACK);
        thinLinePaint.setStrokeWidth(2);

        selectedCellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectedCellPaint.setColor(Color.rgb(176, 224, 230));

        conflictCellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        conflictCellPaint.setColor(Color.rgb(192, 192, 192));

        numberPaint.setStyle(Paint.Style.FILL);
        numberPaint.setColor(Color.BLACK);
        numberPaint.setAntiAlias(true);
        numberPaint.setTextSize(100);

        fillCells(canvas);
        drawLine(canvas);
        drawNumbers(canvas);
    }

    private void drawNumbers(Canvas canvas) {

        createField();


        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                    canvas.drawText(Integer.toString(numberArray[i][j]), j*115+50, i*120+100, numberPaint);
            }
        }
    }

    private void createField() {
        // заполняем массив числами от 1 до 9
        initializeArray(numberArray);

        // сдигаем числа в таблице
        Random randomCount = new Random();
        Random randomRow = new Random();

        for (int i = 0; i < 9; i++) {
            int r1 = randomCount.nextInt(9);
            int r2 = randomRow.nextInt(9);
            shiftNumbers(r1, r2);
        }

        // транспонируем матрицу
        transposingArray(numberArray);

        // перемешиваем числа
        shakeRowInsideArea();

        // снова транспонируем матрицу
        transposingArray(numberArray);

    }

    private void shakeRowInsideArea() {
        int i = 0;
        do{
            int[] tempArray = numberArray[i];
            int[] tempArray2 = numberArray[i+1];

            numberArray[i] = numberArray[i+2];
            numberArray[i+1] = tempArray;
            numberArray[i+2] = tempArray2;
            i=i+3;
        }while(i < size);
    }

    private void transposingArray(int[][] array) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i; j++) {
                int tmp = array[i][j];
                array[i][j] = array[j][i];
                array[j][i] = tmp;
            }
        }
    }

    private void shiftNumbers(int count, int row) {
        int index;
        for(int j = 0; j < size; j++){
            index = (j+count)%9+1;
            numberArray[row][j] = index;
        }
    }

    private void initializeArray(int[][] array) {
        for(int i = 0; i < size; i++){
            for(int j = 0; j< size; j++){
                numberArray[i][j] = j+1;
            }
        }
    }

    private void drawLine(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), thickLinePaint);
        Paint tempLinePaint;

        for (int i = 1; i <= size; i++) {
            if (i%cellSize==0) {
                tempLinePaint = thickLinePaint;
            } else {
                tempLinePaint = thinLinePaint;
            }

            canvas.drawLine(
                    i*sizeCellPixel,
                    0,
                    i*sizeCellPixel,
                    getHeight(),
                    tempLinePaint);

            canvas.drawLine(
                    0,
                    i * sizeCellPixel,
                    getWidth(),
                    i*sizeCellPixel,
                    tempLinePaint
            );
        }
    }

    private void fillCells(Canvas canvas) {
        if (selectRow == -1 || selectCol == -1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == selectRow && j == selectCol) {
                    fillCells(canvas, i, j, selectedCellPaint);
                } else if (i == selectRow || j == selectCol){
                    fillCells(canvas, i, j, conflictCellPaint);
                } else if (i / cellSize == selectRow / cellSize && j / cellSize == selectCol / cellSize) {
                    fillCells(canvas, i, j, conflictCellPaint);
                }
            }
        }
    }

    private void fillCells(Canvas canvas, int row, int column, Paint selectedPaint) {
        canvas.drawRect(column * sizeCellPixel, row * sizeCellPixel, (column + 1) * sizeCellPixel, (row + 1) * sizeCellPixel, selectedPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                handleTouchEvent(event.getX(), event.getY());
                return true;
            default :
                return false;
        }
    }

    private void handleTouchEvent(float x, float y) {
        selectRow = (int) (y / sizeCellPixel);
        selectCol = (int) (x / sizeCellPixel);
        invalidate();
    }


}

