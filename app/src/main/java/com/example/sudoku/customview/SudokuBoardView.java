package com.example.sudoku.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.sudoku.R;

import java.util.ArrayList;

public class SudokuBoardView extends View {

    private final int boardColor;
    private final Paint boardColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellHighlightColorPaint = new Paint();
    private final int cellFillColor;
    private final int cellHighlightColor;
    private int size = 9;

    public void setSize(int size) {
        this.size = size;
    }

    private int cellSize;

    private int letterColor;
    private Paint letterPaint = new Paint();
    private int wrongLetterColor;
    private final int letterColorSolve;

    private final Paint letterColorPaint = new Paint();
    private final Rect letterPaintBounds = new Rect();

    private final SudokuBoardSolver boardSolver = new SudokuBoardSolver(size);


    public SudokuBoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray array = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SudokuBoardView, 0, 0);

        try {
            boardColor = array.getInteger(R.styleable.SudokuBoardView_boardColor, 0);
            cellFillColor = array.getInteger(R.styleable.SudokuBoardView_cellFillColor, 0);
            cellHighlightColor = array.getInteger(R.styleable.SudokuBoardView_cellHighlightColor, 0);
            letterColor = array.getInteger(R.styleable.SudokuBoardView_letterColor, 0);
            wrongLetterColor = array.getInteger(R.styleable.SudokuBoardView_wrongLetterColor, 0);
            letterColorSolve = array.getInteger(R.styleable.SudokuBoardView_letterColorSolve, 0);
        } finally {
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int sizePixel = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());

        cellSize = sizePixel / 9;

        setMeasuredDimension(sizePixel, sizePixel);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setColor(cellFillColor);
        cellFillColorPaint.setAntiAlias(true);

        cellHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellHighlightColorPaint.setColor(cellHighlightColor);
        cellHighlightColorPaint.setAntiAlias(true);

        letterColorPaint.setStyle(Paint.Style.FILL);
        letterColorPaint.setAntiAlias(true);
        letterColorPaint.setColor(letterColor);

        colorCell(canvas, boardSolver.getSelectedRow(), boardSolver.getSelectedColumn());
        canvas.drawRect(0, 0, getWidth(), getHeight(), boardColorPaint);
        drawBoard(canvas);
        drawNumber(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isValid;

        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            boardSolver.setSelectedRow((int) Math.ceil(y / cellSize));
            boardSolver.setSelectedColumn((int) Math.ceil(x / cellSize));
            isValid = true;
        } else {
            isValid = false;
        }

        return isValid;
    }

    private void drawNumber(Canvas canvas) {
        letterColorPaint.setTextSize(cellSize);

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (boardSolver.getBoard()[row][column] != 0) {
                    setText(canvas, row, column);
                }
            }
        }

        letterColorPaint.setColor(wrongLetterColor);

        for (ArrayList<Object> letter:
                boardSolver.getEmptyBox()) {
            int row = (int) letter.get(0);
            int column = (int) letter.get(1);

            setText(canvas, row, column);
        }
    }

    private void setText(Canvas canvas, int row, int column) {
        String text = Integer.toString(boardSolver.getBoard()[row][column]);
        float width, height;
        letterColorPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
        width = letterColorPaint.measureText(text);
        height = letterPaintBounds.height();

        canvas.drawText(text, (column * cellSize) + ((cellSize - width) / 2),
                (row * cellSize + cellSize) - ((cellSize - height) / 2),
                letterColorPaint);
    }

    private void colorCell(Canvas canvas, int row, int column) {
        if (boardSolver.getSelectedRow() != -1 && boardSolver.getSelectedColumn() != -1) {
            canvas.drawRect((column - 1) * cellSize, 0, column * cellSize, cellSize * 9, cellHighlightColorPaint);
            canvas.drawRect(0, (row - 1) * cellSize, cellSize * 9, row * cellSize, cellHighlightColorPaint);
            canvas.drawRect((column - 1) * cellSize, (row - 1) * cellSize, column * cellSize, row * cellSize, cellFillColorPaint);
        }
        invalidate();
    }

    // рисует толстую линию
    private void drawThickLine() {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(10);
        boardColorPaint.setColor(boardColor);
    }

    // рисует тонкую линию
    private void drawThinLine() {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(4);
        boardColorPaint.setColor(boardColor);
    }

    private void drawBoard(Canvas canvas) {
        for (int column = 0; column < size + 1; column++) {
            if (column % 3 == 0) drawThickLine();
            else drawThinLine();
            canvas.drawLine(cellSize * column, 0,
                    cellSize * column, getWidth(),
                    boardColorPaint);
        }

        for (int row = 0; row < size + 1; row++) {
            if (row % 3 == 0) drawThickLine();
            else drawThinLine();
            canvas.drawLine(0, cellSize * row,
                    getWidth(), cellSize * row,
                    boardColorPaint);
        }
    }

    public SudokuBoardSolver getSolver() {
        return this.boardSolver;
    }
}

