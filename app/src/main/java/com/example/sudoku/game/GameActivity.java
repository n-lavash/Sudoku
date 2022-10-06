package com.example.sudoku.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sudoku.finish.FinishActivity;
import com.example.sudoku.main.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.constants.ItemConstant;
import com.example.sudoku.constants.LevelConstant;
import com.example.sudoku.customview.SudokuBoardSolver;
import com.example.sudoku.customview.SudokuBoardView;
import com.example.sudoku.sudokucreators.generator.SudokuGenerator;
import com.example.sudoku.sudokucreators.solver.SolverSudoku;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity {

    // количество скрытых ячеек
    private int hiddenCells;
    // текущий уровень
    private String level;

    private TextView textViewLevel;
    private TextView textViewMistakes;
    private Chronometer timer;
    private TextView textViewHint;

    private SudokuBoardView sudokuBoard;
    private SudokuBoardSolver sudokuBoardSolver;

    private SudokuGenerator generator;
    private SolverSudoku solver;

    private int[][] board;
    private int[][] copyBoard;
    private int[][] solveBoard;

    private final int size = 9;
    private int countMistakes = 0;
    private int hintCount;

    private Dialog gameDialogFinish;
    private Dialog gameDialogReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        if (intent.hasExtra(ItemConstant.LEVEL) && intent.hasExtra(ItemConstant.HIDDEN_CELLS)) {
            level = intent.getStringExtra(ItemConstant.LEVEL);
            hiddenCells = intent.getIntExtra(ItemConstant.HIDDEN_CELLS, 0);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.fail_intent), Toast.LENGTH_SHORT).show();
            finish();
        }

        gameDialogFinish = new Dialog(GameActivity.this);
        gameDialogFinish.setContentView(R.layout.layout_dialog_game_finish);

        gameDialogReset = new Dialog(GameActivity.this);
        gameDialogReset.setContentView(R.layout.layout_dialog_game_reset);

        textViewLevel = findViewById(R.id.textViewLevel);
        textViewMistakes = findViewById(R.id.textViewMistakes);
        timer = findViewById(R.id.textViewTimer);
        textViewHint = findViewById(R.id.textViewHint);

        initialization();

        sudokuBoard = findViewById(R.id.sudokuBoard);
        sudokuBoard.setSize(size);
        sudokuBoardSolver = sudokuBoard.getSolver();

        generator = new SudokuGenerator(size, hiddenCells);
        generator.fillValues();
        board = generator.getMat();

        setCopyBoard();

        setSolveBoard();

        sudokuBoardSolver.setBoard(board);
        generateSudoku();

        StringBuilder boar = new StringBuilder();
        StringBuilder resBoard = new StringBuilder();
        boar.append("\n");
        resBoard.append("\n");
        for (int[] ints : board) {
            for (int j = 0; j < board.length; j++) {
                boar.append(ints[j]).append(" ");
            }
            boar.append("\n");
        }

        for (int[] ints : solveBoard) {
            for (int j = 0; j < solveBoard.length; j++) {
                resBoard.append(ints[j]).append(" ");
            }
            resBoard.append("\n");
        }

        Log.d("MyLog", boar.toString());
        Log.d("MyLog", resBoard.toString());

        timer.start();
    }

    private void initialization() {
        textViewLevel.setText(String.format(getString(R.string.text_view_level), level));
        timer.setBase(SystemClock.elapsedRealtime());
        switch (level) {
            case LevelConstant.EASY:
                hintCount = 10;
                textViewMistakes.setText(String.format(getString(R.string.text_view_mistakes_easy), countMistakes));
                break;
            case LevelConstant.MEDIUM:
                hintCount = 5;
                textViewMistakes.setText(String.format(getString(R.string.text_view_mistakes_another), countMistakes, 10));
                break;
            case LevelConstant.HARD:
                hintCount = 3;
                textViewMistakes.setText(String.format(getString(R.string.text_view_mistakes_another), countMistakes, 3));
        }
        textViewHint.setText(String.format(getString(R.string.btn_hint), hintCount));
    }

    private void setCopyBoard() {
        copyBoard = new int[board.length][board.length];

        for (int i = 0; i < copyBoard.length; i++) {
            System.arraycopy(board[i], 0, copyBoard[i], 0, copyBoard.length);
        }
    }

    private void setSolveBoard() {
        solveBoard = new int[board.length][board.length];

        for (int i = 0; i < solveBoard.length; i++) {
            System.arraycopy(board[i], 0, solveBoard[i], 0, solveBoard.length);
        }
    }

    public void onClickOne(View view) {
        actionWithNumber(1);
    }

    public void onClickTwo(View view) {
        actionWithNumber(2);
    }

    public void onClickThree(View view) {
        actionWithNumber(3);
    }

    public void onClickFour(View view) {
        actionWithNumber(4);
    }

    public void onClickFive(View view) {
        actionWithNumber(5);
    }

    public void onClickSix(View view) {
        actionWithNumber(6);
    }

    public void onClickSeven(View view) {
        actionWithNumber(7);
    }

    public void onClickEight(View view) {
        actionWithNumber(8);
    }

    public void onClickNine(View view) {
        actionWithNumber(9);
    }

    private void actionWithNumber(int number) {
        switch (level) {
            case LevelConstant.EASY:
                setNumber(number);
                break;
            case LevelConstant.MEDIUM:
                setNumber(number);
                if (countMistakes == 10) {
                    dialogShow(10);
                }
                break;
            case LevelConstant.HARD:
                setNumber(number);
                if (countMistakes == 3) {
                    dialogShow(3);
                }
                break;
        }
    }

    private void dialogShow(int mistakes) {
        gameDialogFinish.show();
        Button buttonYes = gameDialogFinish.findViewById(R.id.buttonYes);
        buttonYes.setOnClickListener(view -> {
            board = copyBoard;
            sudokuBoardSolver.setBoard(board);

            generateSudoku();
            gameDialogFinish.cancel();
        });

        Button buttonNo = gameDialogFinish.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(view -> finishGame(false, String.format(getString(R.string.end_game_mistakes), mistakes)));
    }

    private void generateSudoku() {
        solver = new SolverSudoku();
        solver.solve(solveBoard);
        board = solver.getSolution();
        solveBoard = solver.getSolution();
        countMistakes = 0;
        initialization();
    }

    private void setNumber(int number) {
        if (!sudokuBoardSolver.setNumber(sudokuBoard, number, solveBoard)) {
            countMistakes++;
            if (level.equals(LevelConstant.EASY)) {
                textViewMistakes.setText(String.format(getString(R.string.text_view_mistakes_easy), countMistakes));
            } else if (level.equals(LevelConstant.MEDIUM)){
                textViewMistakes.setText(String.format(getString(R.string.text_view_mistakes_another), countMistakes, 10));
            } else {
                textViewMistakes.setText(String.format(getString(R.string.text_view_mistakes_another), countMistakes, 3));
            }
            Toast.makeText(getApplicationContext(), getString(R.string.mistake_play), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickFinishGame(View view) {
        if (Arrays.deepEquals(board, solveBoard)) {
            Toast.makeText(getApplicationContext(), getString(R.string.text_view_win), Toast.LENGTH_SHORT).show();
            finishGame(true, getString(R.string.text_view_win));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_incorrect_finish), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSolve(View view) {
        sudokuBoardSolver.setBoard(solveBoard);
        finishGame(false, getString(R.string.text_view_lose));
        finish();
    }

    public void onCLickClear(View view) {
        int row = sudokuBoardSolver.getSelectedRow() - 1;
        int column = sudokuBoardSolver.getSelectedColumn() - 1;

        board[row][column] = 0;
        sudokuBoard.invalidate();
    }

    public void onClickHint(View view) {
        if (this.hintCount  > 0) {
            int row = sudokuBoardSolver.getSelectedRow() - 1;
            int column = sudokuBoardSolver.getSelectedColumn() - 1;
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.toast_hint), solveBoard[row][column]), Toast.LENGTH_SHORT).show();
            this.hintCount--;
            textViewHint.setText(String.format(getString(R.string.btn_hint), this.hintCount));
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.fail_hint), Toast.LENGTH_SHORT).show();
        }
    }

    private void finishGame(boolean result, String title) {
        Intent intent = new Intent(getApplicationContext(), FinishActivity.class);
        intent.putExtra(ItemConstant.RESULT, result);
        intent.putExtra(ItemConstant.TITLE, title);
        intent.putExtra(ItemConstant.TIMER, timer.getText().toString());
        intent.putExtra(ItemConstant.LEVEL, level);
        intent.putExtra(ItemConstant.MISTAKE, countMistakes);
        startActivity(intent);
    }

    public void onClickResetGame(View view) {
        gameDialogReset.show();
        Button buttonYes = gameDialogReset.findViewById(R.id.buttonYes);
        buttonYes.setOnClickListener(view1 -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            GameActivity.this.finish();
        });
        Button buttonNo = gameDialogReset.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(view12 -> gameDialogReset.cancel());
    }
}