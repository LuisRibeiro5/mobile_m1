package com.example.trabalho_1_db.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.trabalho_1_db.R;
import com.example.trabalho_1_db.databinding.ActivityLogHoursBinding;
import com.example.trabalho_1_db.models.User;
import java.time.LocalTime;

public class LogHoursActivity extends AppCompatActivity {

    private ActivityLogHoursBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_hours);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityLogHoursBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupTextViews();
        setupLogLayout();


        binding.backToLogin.setOnClickListener(this::back_to_login);
    }

    public void setupTextViews() {
        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            binding.employedId.setText(user.getEmployedId());
            binding.fullName.setText(user.getFullName());
            binding.department.setText(user.getDepartment());
            binding.jobTitle.setText(user.getJobTitle());
        }
    }

    public void setupLogLayout() {
        LocalTime localtime = LocalTime.now();
        int current_hour = localtime.getHour();

        int viewsCount = current_hour - 8;

        if (current_hour > 12) viewsCount--;
        if (current_hour > 17) viewsCount -= current_hour - 17;

        for (int i = 0; i < viewsCount; i++) {
            EditText editText = new EditText(this);
            editText.setHint("Log " + (i + 1));


            binding.linearLogLayout.addView(editText);
        }

    }
    public void back_to_login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}