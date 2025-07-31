package com.example.nrainhas;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {

    private TabuleiroRainhas jogo;
    private GridLayout gradeTabuleiro;
    private TextView textoMensagem;
    private Button botaoReiniciar;
    private ImageButton botaoConfig;
    private final int TAMANHO_MIN = 4;
    private final int TAMANHO_MAX = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        if (savedInstanceState != null) {
            int tamanho = savedInstanceState.getInt("tamanhoTabuleiro");
            ArrayList<Integer> linhasRainhas = savedInstanceState.getIntegerArrayList("linhasRainhas");
            ArrayList<Integer> colunasRainhas = savedInstanceState.getIntegerArrayList("colunasRainhas");
            jogo = new TabuleiroRainhas(tamanho);
            if (linhasRainhas != null && colunasRainhas != null && linhasRainhas.size() == colunasRainhas.size()) {
                for (int i = 0; i < linhasRainhas.size(); i++) {
                    int linha = linhasRainhas.get(i);
                    int coluna = colunasRainhas.get(i);
                    jogo.alternarRainha(linha, coluna);
                }
            }
        } else {
            // Se for a primeira vez, inicializa o jogo com um valor padrão.
            jogo = new TabuleiroRainhas(4);
        }

        gradeTabuleiro = findViewById(R.id.boardGrid);
        textoMensagem = findViewById(R.id.messageText);
        botaoReiniciar = findViewById(R.id.resetButton);
        botaoConfig = findViewById(R.id.botaoConfig);


        botaoReiniciar.setOnClickListener(v -> {
            jogo.reiniciar(jogo.getTamanho());
            desenharTabuleiro();
        });

        desenharTabuleiro();

        botaoConfig.setOnClickListener(v ->{
            Intent intencao = new Intent(MainActivity.this,ConfigActivity.class);
            startActivity(intencao);

        });

    }

    public void onSaveInstanceState(@NotNull Bundle outState){
        super.onSaveInstanceState(outState);
        if(jogo!=null){
            ArrayList<Integer> linhasRainhas = new ArrayList<>();
            ArrayList<Integer> colunasRainhas = new ArrayList<>();
            int tamanho = jogo.getTamanho();
            // Percorre o tabuleiro e adiciona as coordenadas de cada rainha
            for (int linha = 0; linha < tamanho; linha++) {
                for (int coluna = 0; coluna < tamanho; coluna++) {
                    if (jogo.possuiRainha(linha, coluna)) {
                        linhasRainhas.add(linha);
                        colunasRainhas.add(coluna);
                    }
                }
            }
            outState.putIntegerArrayList("linhasRainhas", linhasRainhas);
            outState.putIntegerArrayList("colunasRainhas", colunasRainhas);
            outState.putInt("tamanhoTabuleiro", jogo.getTamanho());
        }


    }
    private void desenharTabuleiro() {
        if (jogo == null) return;
        gradeTabuleiro.removeAllViews();
        gradeTabuleiro.setColumnCount(jogo.getTamanho());
        gradeTabuleiro.setRowCount(jogo.getTamanho());

        //Point size = new Point();
        //Display display = getWindowManager().getDefaultDisplay();
        // display.getSize(size);
        // int screenWidth = size.x;
        // int screenHeight = size.y;

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int dimensaoTela = Math.min(screenHeight, screenWidth);
        int tamanhoCelula = (dimensaoTela - 100) / jogo.getTamanho();
        //quando a tela ta deitada
        if (screenHeight < screenWidth)
            tamanhoCelula = (dimensaoTela - 300) / jogo.getTamanho();

        for (int linha = 0; linha < jogo.getTamanho(); linha++) {
            for (int coluna = 0; coluna < jogo.getTamanho(); coluna++) {
                ImageButton celula = new ImageButton(this);
                celula.setLayoutParams(new GridLayout.LayoutParams());
                celula.getLayoutParams().width = tamanhoCelula;
                celula.getLayoutParams().height = tamanhoCelula;
                celula.setPadding(8, 8, 8, 8);
                celula.setScaleType(ImageButton.ScaleType.FIT_XY);

                // Define a cor de fundo estilo xadrez
                if ((linha + coluna) % 2 == 0) {
                    celula.setBackgroundColor(Color.parseColor("#FFF8E1"));
                } else {
                    celula.setBackgroundColor(Color.parseColor("#FFE082"));
                }

                if (jogo.possuiRainha(linha, coluna)) {
                    if (jogo.temConflito(linha, coluna)) {
                        //fazer animação
                        celula.setAlpha(0f);
                        celula.setImageResource(R.drawable.rainha_em_conflito_teste);

                        celula.animate().alpha(1f).setDuration(2000);
                        celula.setBackgroundColor(Color.parseColor("#FFCDD2"));
                    } else {
                        //Rainha válida
                        celula.setImageResource(R.drawable.rainha);
                    }
                } else {
                    celula.setImageResource(android.R.color.transparent);
                }

                int finalLinha = linha;
                int finalColuna = coluna;
                celula.setOnClickListener(v -> {
                    jogo.alternarRainha(finalLinha, finalColuna);
                    desenharTabuleiro();
                });

                gradeTabuleiro.addView(celula);
            }
        }
        atualizarMensagem();
    }

    private void atualizarMensagem() {
        if (jogo.estaResolvido()) {
            //textoMensagem.setText("🎉 Parabéns! Você resolveu o desafio!");
            textoMensagem.setText(R.string.mensagem_vitoria);
            textoMensagem.setTextColor(Color.parseColor("#388E3C")); // Verde

            SharedPreferences prefs = getSharedPreferences("config_nrainhas", MODE_PRIVATE);
            int tamanhoAtual = jogo.getTamanho();


                /*new AlertDialog.Builder(this).setTitle("Parabéns!").setMessage("Você venceu! Deseja avançar para o próximo nível?").setPositiveButton("Sim", (dialog, which) -> {
                            SharedPreferences prefs = getSharedPreferences("config_nrainhas", MODE_PRIVATE);
                            int tamanhoAtual = jogo.getTamanho();
                            int proximoTamanho = (tamanhoAtual >= TAMANHO_MAX) ? TAMANHO_MIN : tamanhoAtual + 1;

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("tamanho_tabuleiro", proximoTamanho);
                            editor.apply();

                            jogo.reiniciar(proximoTamanho);
                            desenharTabuleiro();
                        }).setNegativeButton("Não", (dialog, which) -> {
                            jogo.reiniciar(jogo.getTamanho());
                            desenharTabuleiro();
                        }).setCancelable(false).show();*/

            if (tamanhoAtual < TAMANHO_MAX) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setTitle(R.string.alerta_titulo);
                dialogo.setMessage(R.string.alerta_mensagem);

                dialogo.setPositiveButton(R.string.botao_sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int proximoTamanho = tamanhoAtual + 1;

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("tamanho_tabuleiro", proximoTamanho);
                        editor.apply();

                        jogo.reiniciar(proximoTamanho);
                        desenharTabuleiro();
                    }
                });

                dialogo.setNegativeButton(R.string.botao_nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                dialogo.setCancelable(false);
                dialogo.show();
            } else {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setTitle(R.string.alerta_titulo);
                dialogo.setMessage(R.string.alerta_mensagem2);

                dialogo.setPositiveButton(R.string.botao_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                dialogo.setCancelable(false);
                dialogo.show();
            }


        } else if (jogo.temConflitos()) {
            //textoMensagem.setText("Conflitos detectados!");
            textoMensagem.setText(R.string.mensagem_conflito);
            textoMensagem.setTextColor(Color.RED);
        } else {
            //textoMensagem.setText("Coloque/remova rainhas tocando nas casas.");
            textoMensagem.setText(R.string.mensagem_padrao);
            textoMensagem.setTextColor(Color.DKGRAY);
        }
    }


    @Override
    protected void onResume() {
        SharedPreferences prefs = getSharedPreferences("config_nrainhas", MODE_PRIVATE);
        boolean musicaAtiva = prefs.getBoolean("musica_ativa", true);
        int tamanho = prefs.getInt("tamanho_tabuleiro", 4);

        if (tamanho < 1) {
            tamanho = 4;
        }

        if (jogo.getTamanho() != tamanho) {
            jogo.reiniciar(tamanho);
        }
        desenharTabuleiro();

        Intent it = new Intent(MainActivity.this, AudioService.class);

        if (musicaAtiva){
            it.setAction("PLAY");
            startService(it);
        } else {
            it.setAction("PAUSE");
            startService(it);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        Intent it = new Intent(MainActivity.this, AudioService.class);
        it.setAction("PAUSE");
        startService(it);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent it = new Intent(MainActivity.this, AudioService.class);
        it.setAction("PAUSE");
        startService(it);
        super.onDestroy();
    }





}