package com.example.sudoku.finish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sudoku.main.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.constants.ItemConstant;

public class FinishActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewInfoLevel;
    private TextView textViewCurrentTime;
    private TextView textViewBestTime;
    private TextView textViewCurrentMistakes;
    private TextView textViewBestMistakes;
    private Button buttonNewGame;
    private ImageView imageViewLogoFinish;

    private String level;
    private String time;
    private boolean result;
    private int countMistakes;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewInfoLevel = findViewById(R.id.textViewInfoLevel);
        textViewCurrentTime = findViewById(R.id.textViewCurrentTime);
        textViewBestTime = findViewById(R.id.textViewBestTime);
        textViewCurrentMistakes = findViewById(R.id.textViewCurrentMistakes);
        textViewBestMistakes = findViewById(R.id.textViewBestMistakes);
        buttonNewGame = findViewById(R.id.buttonNewGame);
        imageViewLogoFinish = findViewById(R.id.imageViewLogoFinish);

        Intent intent = getIntent();
        if (intent.hasExtra(ItemConstant.RESULT) && intent.hasExtra(ItemConstant.TIMER) &&
                intent.hasExtra(ItemConstant.LEVEL) && intent.hasExtra(ItemConstant.MISTAKE) && intent.hasExtra(ItemConstant.TITLE)) {
            result = intent.getBooleanExtra(ItemConstant.RESULT, true);
            level = intent.getStringExtra(ItemConstant.LEVEL);
            time = intent.getStringExtra(ItemConstant.TIMER);
            countMistakes = intent.getIntExtra(ItemConstant.MISTAKE, 0);
            title = intent.getStringExtra(ItemConstant.TITLE);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.fail_intent), Toast.LENGTH_SHORT).show();
            finish();
        }

        textViewTitle.setText(title);
        if (result) {
            imageViewLogoFinish.setImageResource(R.drawable.win_logo);
            textViewInfoLevel.setText(String.format(getString(R.string.text_view_level), level));

            textViewCurrentTime.setText(String.format(getString(R.string.text_view_current_time), time));

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            String bestTime = preferences.getString(ItemConstant.BEST_TIME, "");
            if (!bestTime.equals("")) {
                String[] best = bestTime.split(":");
                int bestMin = Integer.parseInt(best[0]);
                int bestSec = Integer.parseInt(best[1]);

                String[] current = time.split(":");
                int curMin = Integer.parseInt(current[0]);
                int curSec = Integer.parseInt(current[1]);

                if (bestMin < curMin && bestSec < curSec)
                    textViewBestTime.setText(String.format(getString(R.string.text_view_best_time), bestTime));
                else if (bestMin == curMin && bestSec < curSec)
                    textViewBestTime.setText(String.format(getString(R.string.text_view_best_time), bestTime));
                else {
                    textViewBestTime.setText(String.format(getString(R.string.text_view_best_time), time));
                    preferences.edit().putString(ItemConstant.BEST_TIME, time).apply();
                }
            } else {
                textViewBestTime.setText(String.format(getString(R.string.text_view_best_time), time));
                preferences.edit().putString(ItemConstant.BEST_TIME, time).apply();
            }

            textViewCurrentMistakes.setText(String.format(getString(R.string.text_view_current_mistakes), countMistakes));
            int bestCountMistakes = preferences.getInt(ItemConstant.BEST_MISTAKE, 0);
            if (bestCountMistakes != 0) {
                textViewBestMistakes.setText(String.format(getString(R.string.text_view_best_mistakes), bestCountMistakes));
            } else {
                textViewBestMistakes.setText(String.format(getString(R.string.text_view_best_mistakes), countMistakes));
                preferences.edit().putInt(ItemConstant.BEST_MISTAKE, countMistakes).apply();
            }
        } else {
            imageViewLogoFinish.setImageResource(R.drawable.lose_logo);
            textViewBestMistakes.setVisibility(View.INVISIBLE);
            textViewInfoLevel.setVisibility(View.INVISIBLE);
            textViewCurrentTime.setVisibility(View.INVISIBLE);
            textViewBestTime.setVisibility(View.INVISIBLE);
            textViewCurrentMistakes.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickNewGame(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}