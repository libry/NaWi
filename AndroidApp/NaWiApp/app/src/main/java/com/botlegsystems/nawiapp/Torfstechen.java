package com.botlegsystems.nawiapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Libry on 29.12.2017.
 */

public class Torfstechen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torfstechen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        final TextView contentTextView = (TextView) findViewById(R.id.text_view_naturschutzzentrum);
        contentTextView.setText(R.string.content_naturschrutzzentrum);
        */
    }
}
