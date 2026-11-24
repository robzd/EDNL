import java.util.ArrayList;
import java.util.Collections;

public class AEstrela {

    public static ArrayList<int[]> buscar(char[][] mapa, int[] inicio, int[] fim) {
        int linhas = mapa.length, colunas = mapa[0].length;
        int[][] g = new int[linhas][colunas];
        int[][] f = new int[linhas][colunas];
        int[][][] pai = new int[linhas][colunas][];
        boolean[][] naAberta = new boolean[linhas][colunas];
        boolean[][] fechada = new boolean[linhas][colunas];
        ArrayList<int[]> aberta = new ArrayList<>();

        f[inicio[0]][inicio[1]] = heuristica(inicio[0], inicio[1], fim);
        aberta.add(inicio);
        naAberta[inicio[0]][inicio[1]] = true;

        while (!aberta.isEmpty()) {
            int[] atual = aberta.get(0);
            for (int[] q : aberta)
                if (f[q[0]][q[1]] < f[atual[0]][atual[1]])
                    atual = q;

            if (atual[0] == fim[0] && atual[1] == fim[1])
                return caminho(pai, atual);

            aberta.remove(atual);
            fechada[atual[0]][atual[1]] = true;

            for (int dl = -1; dl <= 1; dl++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int l = atual[0] + dl, c = atual[1] + dc;
                    if ((dl == 0 && dc == 0) || l < 0 || l >= linhas || c < 0 || c >= colunas
                            || mapa[l][c] == '#' || fechada[l][c])
                        continue;

                    int gTentativo = g[atual[0]][atual[1]] + (dl != 0 && dc != 0 ? 14 : 10);
                    if (!naAberta[l][c]) {
                        aberta.add(new int[] { l, c });
                        naAberta[l][c] = true;
                    } else if (gTentativo >= g[l][c]) {
                        continue;
                    }

                    pai[l][c] = atual;
                    g[l][c] = gTentativo;
                    f[l][c] = gTentativo + heuristica(l, c, fim);
                }
            }
        }
        return null;
    }

    private static int heuristica(int l, int c, int[] fim) {
        int dl = Math.abs(l - fim[0]), dc = Math.abs(c - fim[1]);
        return 10 * (dl + dc) - 6 * Math.min(dl, dc);
    }

    private static ArrayList<int[]> caminho(int[][][] pai, int[] atual) {
        ArrayList<int[]> caminho = new ArrayList<>();
        for (int[] q = atual; q != null; q = pai[q[0]][q[1]])
            caminho.add(q);
        Collections.reverse(caminho);
        return caminho;
    }
}
