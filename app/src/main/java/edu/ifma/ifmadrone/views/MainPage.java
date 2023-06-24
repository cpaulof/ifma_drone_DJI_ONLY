package edu.ifma.ifmadrone.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.ifma.ifmadrone.R;

public class MainPage extends AppCompatActivity {
    // Não usado no momento (futuro MENU, com stream, missoes, classficação de objetos, etc)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }
}