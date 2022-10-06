package com.example.sudoku.sudokucreators.solver;

import java.util.Arrays;

public class SolverSudoku {
    private static final int SIZE = 9;
    private static final int SUBSECTION_SIZE = 3;
    private static final int NO_VALUE = 0;
    private static final int CONSTRAINTS = 4;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;
    private static final int COVER_START_INDEX = 1;

    private static DancingLinks dlx;

    public void solve(int[][] board) {
        boolean[][] cover = initializeExactCoverBoard(board);
        dlx = new DancingLinks(cover);
        dlx.runSolver();
    }

    private int getIndex(int row, int column, int num) {
        return (row - 1) * SIZE * SIZE + (column - 1) * SIZE + (num - 1);
    }

    private boolean[][] createExactCoverBoard() {
        boolean[][] coverBoard = new boolean[SIZE * SIZE * MAX_VALUE][SIZE * SIZE * CONSTRAINTS];

        int header = 0;
        header = checkCellConstraint(coverBoard, header);
        header = checkRowConstraint(coverBoard, header);
        header = checkColumnConstraint(coverBoard, header);
        checkSubsectionConstraint(coverBoard, header);

        return coverBoard;
    }

    private int checkSubsectionConstraint(boolean[][] coverBoard, int header) {
        for (int row = COVER_START_INDEX; row <= SIZE; row += SUBSECTION_SIZE) {
            for (int column = COVER_START_INDEX; column <= SIZE; column += SUBSECTION_SIZE) {
                for (int n = COVER_START_INDEX; n <= SIZE; n++, header++) {
                    for (int rowDelta = 0; rowDelta < SUBSECTION_SIZE; rowDelta++) {
                        for (int columnDelta = 0; columnDelta < SUBSECTION_SIZE; columnDelta++) {
                            int index = getIndex(row + rowDelta, column + columnDelta, n);
                            coverBoard[index][header] = true;
                        }
                    }
                }
            }
        }
        return header;
    }

    private int checkColumnConstraint(boolean[][] coverBoard, int header) {
        for (int column = COVER_START_INDEX; column <= SIZE; column++) {
            for (int n = COVER_START_INDEX; n <= SIZE; n++, header++) {
                for (int row = COVER_START_INDEX; row <= SIZE; row++) {
                    int index = getIndex(row, column, n);
                    coverBoard[index][header] = true;
                }
            }
        }
        return header;
    }

    private int checkRowConstraint(boolean[][] coverBoard, int header) {
        for (int row = COVER_START_INDEX; row <= SIZE; row++) {
            for (int n = COVER_START_INDEX; n <= SIZE; n++, header++) {
                for (int column = COVER_START_INDEX; column <= SIZE; column++) {
                    int index = getIndex(row, column, n);
                    coverBoard[index][header] = true;
                }
            }
        }
        return header;
    }

    private int checkCellConstraint(boolean[][] coverBoard, int header) {
        for (int row = COVER_START_INDEX; row <= SIZE; row++) {
            for (int column = COVER_START_INDEX; column <= SIZE; column++, header++) {
                for (int n = COVER_START_INDEX; n <= SIZE; n++) {
                    int index = getIndex(row, column, n);
                    coverBoard[index][header] = true;
                }
            }
        }
        return header;
    }

    private boolean[][] initializeExactCoverBoard(int[][] board) {
        boolean[][] coverBoard = createExactCoverBoard();
        for (int row = COVER_START_INDEX; row <= SIZE; row++) {
            for (int column = COVER_START_INDEX; column <= SIZE; column++) {
                int n = board[row - 1][column - 1];

                if (n != NO_VALUE) {
                    for (int num = MIN_VALUE; num <= MAX_VALUE; num++) {
                        if (num != n) {
                            Arrays.fill(coverBoard[getIndex(row, column, num)], false);
                        }
                    }
                }
            }
        }
        return coverBoard;
    }

    public int[][] getSolution() {
        return dlx.getSolution();
    }
}
