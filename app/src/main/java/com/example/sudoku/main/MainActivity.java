package com.example.sudoku.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.sudoku.R;
import com.example.sudoku.about.AboutGameActivity;
import com.example.sudoku.constants.ItemConstant;
import com.example.sudoku.constants.LevelConstant;
import com.example.sudoku.game.GameActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSelectLevel(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_bottom_sheet, findViewById(R.id.bottomSheetContainer));

        bottomSheetView.findViewById(R.id.buttonEasy).setOnClickListener(view1 -> {
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra(ItemConstant.LEVEL, LevelConstant.EASY);
            intent.putExtra(ItemConstant.HIDDEN_CELLS, 45);
            startActivity(intent);
            finish();
        });

        bottomSheetView.findViewById(R.id.buttonMedium).setOnClickListener(view12 -> {
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra(ItemConstant.LEVEL, LevelConstant.MEDIUM);
            intent.putExtra(ItemConstant.HIDDEN_CELLS, 51);
            startActivity(intent);
            finish();
        });

        bottomSheetView.findViewById(R.id.buttonHard).setOnClickListener(view13 -> {
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra(ItemConstant.LEVEL, LevelConstant.HARD);
            intent.putExtra(ItemConstant.HIDDEN_CELLS, 57);
            startActivity(intent);
            finish();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void onClickAboutGame(View view) {
        Intent intent = new Intent(getApplicationContext(), AboutGameActivity.class);
        startActivity(intent);
    }

    public void onClickExit(View view) {
        finish();
    }
}