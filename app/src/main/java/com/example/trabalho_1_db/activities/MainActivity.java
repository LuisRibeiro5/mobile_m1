package com.example.trabalho_1_db.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.trabalho_1_db.R;
import com.example.trabalho_1_db.models.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnLogin = findViewById(R.id.login_button);
        btnLogin.setOnClickListener(this::login);
    }

    public void login(View view) {
        EditText username = findViewById(R.id.userInput);
        EditText password = findViewById(R.id.passwordInput);

        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        User user = new User("user", "user", "123456",
                "John Doe", "Sales", "Sales Manager");

        User user2 = new User("admin", "admin", "789100",
                "Kauan ta mec", "TI", "development");

        Intent intent = new Intent(this, LogHoursActivity.class);

        if (usernameText.equals(user.getUsername()) && passwordText.equals(user.getPassword())) {

            intent.putExtra("user", user);
            startActivity(intent);

        } else if (usernameText.equals(user2.getUsername()) &&
                passwordText.equals(user2.getPassword())) {

            intent.putExtra("user", user2);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Usuario ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }


}