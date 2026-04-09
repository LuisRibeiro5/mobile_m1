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

        binding.btnCompartilhar.setOnClickListener(this::compartilharConteudo);
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

    @Override
    protected void onResume() {
        super.onResume();
        boolean diaMudou = verificarMudancaDeDia();
        
        //Aqui kaunzinho se o dia NÃO mudou, o app só foi minimizado no mesmo dia
        //Atualizamos a tela para o horário atual sem perder os textos digitados
        if (!diaMudou) {
            atualizarCamposSemPerderDados();
        }
    }

    //kaunzinho troquei algumas coisas para retornar TRUE ou FALSE, poque a questão 5 e 6 tem q "trabalhar juntas" pra verificar se o dia mudou ou se o app só foi minimizado.
    private boolean verificarMudancaDeDia() {
        android.content.SharedPreferences prefs = getSharedPreferences("MeuAppPrefs", android.content.Context.MODE_PRIVATE);
        String dataSalva = prefs.getString("data_ultimo_acesso", "");

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        String dataAtual = sdf.format(new java.util.Date());

        //se a data salva não for vazia e for diferente da data atual, o dia virou
        if (!dataSalva.isEmpty() && !dataSalva.equals(dataAtual)) {
            setupLogLayout(); //limpa e recria vazio
            prefs.edit().putString("data_ultimo_acesso", dataAtual).apply();
            return true; //Avisa que o dia mudou
        }

        prefs.edit().putString("data_ultimo_acesso", dataAtual).apply();
        return false; //avisa que continuamos no mesmo dia
    }

    //para salvar e restaurar sem perder os dados.
    private void atualizarCamposSemPerderDados() {
        //Salva os textos atuais na memória usando o Hint
        java.util.Map<String, String> textosSalvos = new java.util.HashMap<>();
        for (EditText et : listaDeCampos) {
            textosSalvos.put(et.getHint().toString(), et.getText().toString());
        }

        //Recria os campos para incluir os novos horários que passaram enquanto estava minimizado
        setupLogLayout();

        //Devolve os textos que guardamos na memória para os seus respectivos campos
        for (EditText et : listaDeCampos) {
            String hint = et.getHint().toString();
            if (textosSalvos.containsKey(hint)) {
                et.setText(textosSalvos.get(hint));
            }
        }
    }


    public void compartilharConteudo(View view) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        String dataAtual = sdf.format(new java.util.Date());

        //Pega o usuário que veio da tela de login
        User user = (User) getIntent().getSerializableExtra("user");
        String nomeUsuario = (user != null) ? user.getFullName() : "Usuário Desconhecido";

        StringBuilder textoParaCompartilhar = new StringBuilder();
        textoParaCompartilhar.append("Data: ").append(dataAtual).append("\n");
        textoParaCompartilhar.append("Usuário: ").append(nomeUsuario).append("\n\n");
        textoParaCompartilhar.append("Preenchimentos do dia:\n");

        //Itera pela sua lista de campos já existente e pega o que o usuário digitou
        for (EditText et : listaDeCampos) {
            String horario = et.getHint().toString(); //Pega o hint (ex: "Log 8h - 9h")
            String preenchimento = et.getText().toString().trim();
            
            if (!preenchimento.isEmpty()) {
                textoParaCompartilhar.append("- ").append(horario).append(": ").append(preenchimento).append("\n");
            } else {
                textoParaCompartilhar.append("- ").append(horario).append(": (vazio)\n");
            }
        }

        //Cria a Intent para enviar (compartilhar)
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textoParaCompartilhar.toString());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Compartilhar dados via:");
        startActivity(shareIntent);
    }
    
}
