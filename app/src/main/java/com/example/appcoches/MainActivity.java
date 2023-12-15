package com.example.appcoches;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginButtonClick(View view) {
        // Método llamado cuando se hace clic en el botón "Iniciar Sesión"
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Opcional, dependiendo de si quieres que el usuario pueda volver atrás desde LoginActivity
    }

    public void onViewProductsButtonClick(View view) {
        // Método llamado cuando se hace clic en el botón "Ver Productos"
        Intent intent = new Intent(this, ProductActivity.class);
        startActivity(intent);
    }
}

