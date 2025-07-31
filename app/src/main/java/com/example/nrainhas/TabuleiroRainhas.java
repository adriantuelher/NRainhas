package com.example.nrainhas;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class TabuleiroRainhas {
    private int tamanho;
    private final ArrayList<int[]> posicoesRainhas;
    private final Set<String> conflitos;

    public TabuleiroRainhas(int tamanhoInicial) {
        this.tamanho = tamanhoInicial;
        //guarda todas as rainhas do tabuleiro
        this.posicoesRainhas = new ArrayList<>();
        this.conflitos = new HashSet<>();
    }
    private int encontrarIndiceRainha(int linha, int coluna) {
        //percorre a quantidade de rainhas que temos no tabuleiro
        for (int i = 0; i < posicoesRainhas.size(); i++) {
            int[] rainha = posicoesRainhas.get(i);
            //retorna a posição da rainha se ela existir
            if (rainha[0] == linha && rainha[1] == coluna) {
                return i;
            }
        }
        return -1;
    }

    public void alternarRainha(int linha, int coluna) {
        int indiceRainha = encontrarIndiceRainha(linha, coluna);
        // a rainha existe no vetor de rainhas
        if (indiceRainha >= 0) {
            // Se encontrou a rainha, remove
            posicoesRainhas.remove(indiceRainha);
        } else {
            // Se não encontrou, adiciona
            if(posicoesRainhas.size() <tamanho)
                posicoesRainhas.add(new int[]{linha, coluna});
        }
        verificarConflitos();
    }

    private void verificarConflitos() {
        conflitos.clear();
        for (int i = 0; i < posicoesRainhas.size(); i++) {
            for (int j = i + 1; j < posicoesRainhas.size(); j++) {
                int[] rainha1 = posicoesRainhas.get(i);
                int[] rainha2 = posicoesRainhas.get(j); // rainha 2

                boolean mesmaDiagonalPrincipal = (rainha1[0] - rainha1[1]) == (rainha2[0] - rainha2[1]);
                boolean mesmaDiagonalSecundaria = (rainha1[0] + rainha1[1]) == (rainha2[0] + rainha2[1]);

                if (rainha1[0] == rainha2[0] || rainha1[1] == rainha2[1] || mesmaDiagonalPrincipal || mesmaDiagonalSecundaria
                ) {
                    conflitos.add(rainha1[0] + "-" + rainha1[1]);
                    // 0-0
                    conflitos.add(rainha2[0] + "-" + rainha2[1]);
                    // 2-2
                }
            }
        }
    }


    public void reiniciar(int novoTamanho) {
        this.tamanho = novoTamanho;
        this.posicoesRainhas.clear();
        this.conflitos.clear();
    }
    public int getTamanho() {
        return tamanho;
    }

    public boolean possuiRainha(int linha, int coluna) {
        return encontrarIndiceRainha(linha, coluna) >= 0;
    }

    public boolean temConflito(int linha, int coluna) {
        return conflitos.contains(linha + "-" + coluna);
    }

    public boolean estaResolvido() {
        return conflitos.isEmpty() && posicoesRainhas.size() == tamanho;
    }

    public boolean temConflitos() {
        return !conflitos.isEmpty();
    }
}