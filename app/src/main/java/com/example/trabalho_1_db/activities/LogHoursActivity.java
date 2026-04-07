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

import java.sql.SQLOutput;
import java.time.LocalTime;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.List;

public class LogHoursActivity extends AppCompatActivity {

    private ActivityLogHoursBinding binding;
    private List<EditText> listaDeCampos = new ArrayList<>();

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

        // Limpa a lista caso o método seja chamado mais de uma vez
        listaDeCampos.clear();
        binding.linearLogLayout.removeAllViews();

        for (int i = 8; i < 17; i++) { // Alterado para < 17 para representar o início da hora
            if (i == 12) continue; // Almoço

            // Regra: O campo só aparece se a hora já passou (está na hora seguinte ou depois)
            if (current_hour > i) {
                // Se for após as 17h, limitamos aos 8 campos úteis
                if (i >= 17) break;

                EditText editText = new EditText(this);
                editText.setHint("Log " + i + "h - " + (i + 1) + "h");

                // Adicionamos o listener para detectar mudanças no texto
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        atualizarContadores();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                binding.linearLogLayout.addView(editText);
                listaDeCampos.add(editText);
            }
        }
        // Inicializa os contadores assim que carregar a tela
        atualizarContadores();
    }

    private void atualizarContadores() {
        int preenchidos = 0;
        int totalExibidos = listaDeCampos.size();

        for (EditText et : listaDeCampos) {
            if (!et.getText().toString().trim().isEmpty()) {
                preenchidos++;
            }
        }

        int faltantes = totalExibidos - preenchidos;

        binding.tvPreenchidos.setText(preenchidos + " horas de trabalho já estão preenchidas.");
        binding.tvFaltantes.setText(faltantes + " horas de trabalho ainda não foram registradas.");
    }
    public void back_to_login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}