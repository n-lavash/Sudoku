package com.example.sudoku.customview;

import java.util.ArrayList;

public class SudokuBoardSolver {
    private int[][] board;
    private final ArrayList<ArrayList<Object>> emptyBoxIndex;
    private int selectedRow;
    private int selectedColumn;
    private int size;

    public SudokuBoardSolver(int size) {
        selectedColumn = -1;
        selectedRow = -1;

        this.size = size;

        this.board = new int[this.size][this.size];

        emptyBoxIndex = new ArrayList<>();
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    private void getEmptyBoxIndex() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (this.board[row][column] == 0) {
                    this.emptyBoxIndex.add(new ArrayList<>());
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size() - 1).add(row);
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size() - 1).add(column);
                }
            }
        }
    }

    public ArrayList<ArrayList<Object>> getEmptyBox() {
        return emptyBoxIndex;
    }

    public void setNumberPosition(int num) {
        if (this.selectedColumn != -1 && this.selectedRow != -1) {
            if (this.board[this.selectedRow - 1][this.selectedColumn - 1] != num) {
                this.board[this.selectedRow - 1][this.selectedColumn - 1] = num;
            }
        }
    }

    public boolean setNumber(SudokuBoardView display, int number, int[][] solveBoard) {
        if (getSelectedColumn() != -1 && getSelectedRow() != -1)
            if (solveBoard[getSelectedRow() - 1][getSelectedColumn() - 1] != number) {
                board[getSelectedRow() - 1][getSelectedColumn() - 1] = number;
                display.invalidate();
                return false;
            }
        board[getSelectedRow() - 1][getSelectedColumn() - 1] = number;
        display.invalidate();
        return true;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }
}
