package com.example.sudoku.about;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.sudoku.R;

public class AboutGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_game);
    }

    public void onClickTelegram(View view) {
        Intent browserIntent = new
                Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/jepa_lavash"));
        startActivity(browserIntent);
    }

    public void onClickVkontakte(View view) {
        Intent browserIntent = new
                Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/jepa_lavash"));
        startActivity(browserIntent);
    }

    public void onClickInstagram(View view) {
        Intent browserIntent = new
                Intent(Intent.ACTION_VIEW, Uri.parse("https://rkn.gov.ru/news/rsoc/news74180.htm"));
        startActivity(browserIntent);
    }
}