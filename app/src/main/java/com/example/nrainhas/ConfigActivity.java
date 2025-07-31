package com.example.nrainhas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ConfigActivity extends AppCompatActivity {

    Button botaoVoltar;

    Button botaoSalvar;
    int tamanhoSelecionado;
    private Spinner seletorTamanho;

    private  boolean musicaAtiva;
    private SwitchCompat switchMusica;

    int tamanhoOriginal;
    boolean musicaOriginal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config);

        seletorTamanho = findViewById(R.id.seletorTamanho);
        botaoVoltar = findViewById(R.id.botaoVoltar);
        botaoSalvar = findViewById(R.id.botaoSalvar);
        switchMusica = findViewById(R.id.switchMusica);

        SharedPreferences prefs = getSharedPreferences("config_nrainhas", MODE_PRIVATE);

        tamanhoOriginal = prefs.getInt("tamanho_tabuleiro", 4);
        musicaOriginal = prefs.getBoolean("musica_ativa", true);

        tamanhoSelecionado = prefs.getInt("tamanho_tabuleiro", 4);
        musicaAtiva = prefs.getBoolean("musica_ativa", true);


        switchMusica.setChecked(musicaAtiva);
        switchMusica.setOnCheckedChangeListener((buttonView, isChecked) ->
                musicaAtiva = isChecked);


        if(tamanhoSelecionado >= 4){
            seletorTamanho.setSelection(tamanhoSelecionado-4);
        }

        seletorTamanho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                tamanhoSelecionado = 4+pos;



            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



        botaoVoltar.setOnClickListener(v -> {
            if (tamanhoSelecionado != tamanhoOriginal || musicaAtiva != musicaOriginal) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setTitle(R.string.alerta_titulo2);
                dialogo.setMessage(R.string.deseja_sair);

                dialogo.setPositiveButton(R.string.botao_sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        salvarConfiguracoes();
                        finish();
                    }
                });

                dialogo.setNegativeButton(R.string.botao_nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                dialogo.setCancelable(false);
                dialogo.show();
            } else {
                finish();
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarConfiguracoes();
                finish();
            }
        });


    }

    private void salvarConfiguracoes() {
        SharedPreferences prefs = getSharedPreferences("config_nrainhas", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("tamanho_tabuleiro", tamanhoSelecionado);
        editor.putBoolean("musica_ativa", musicaAtiva);
        editor.apply();
        //Toast.makeText(ConfigActivity.this, "Configurações salvas", Toast.LENGTH_SHORT).show();
        Toast.makeText(ConfigActivity.this, R.string.configuracoes_salvas, Toast.LENGTH_SHORT).show();
    }
}