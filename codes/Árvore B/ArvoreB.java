import java.util.ArrayList;
import java.util.List;

public class ArvoreB<T extends Comparable<T>> {
    private NoB<T> raiz;
    private final int t;
    private int tamanho = 0;
    private boolean exibirPassos = true;

    public ArvoreB(int t) {
        if (t < 2)
            throw new IllegalArgumentException("A ordem t deve ser no mínimo 2.");
        this.t = t;
        raiz = new NoB<>(t);
    }

    public NoB<T> buscar(T chave) {
        return buscar(chave, false);
    }

    public NoB<T> buscar(T chave, boolean exibirCaminho) {
        NoB<T> no = raiz;
        while (true) {
            int i = posicao(no, chave);
            if (exibirCaminho)
                System.out.println("  visitando " + no);
            if (i < no.getNumChaves() && chave.compareTo(no.getChave(i)) == 0)
                return no;
            if (no.isFolha())
                return null;
            no = no.getFilho(i);
        }
    }

    private int posicao(NoB<T> no, T chave) {
        int i = 0;
        while (i < no.getNumChaves() && chave.compareTo(no.getChave(i)) > 0)
            i++;
        return i;
    }

    public boolean inserir(T chave) {
        if (buscar(chave) != null)
            return false;

        if (raiz.estaCheio()) {
            NoB<T> novaRaiz = new NoB<>(t, false);
            novaRaiz.setFilho(0, raiz);
            raiz = novaRaiz;
            log("Raiz cheia: altura aumenta");
            cisao(novaRaiz, 0);
        }

        NoB<T> no = raiz;
        while (!no.isFolha()) {
            int i = posicao(no, chave);
            if (no.getFilho(i).estaCheio()) {
                cisao(no, i);
                if (chave.compareTo(no.getChave(i)) > 0)
                    i++;
            }
            no = no.getFilho(i);
        }

        int i = no.getNumChaves() - 1;
        while (i >= 0 && chave.compareTo(no.getChave(i)) < 0) {
            no.setChave(i + 1, no.getChave(i));
            i--;
        }
        no.setChave(i + 1, chave);
        no.setNumChaves(no.getNumChaves() + 1);
        tamanho++;
        return true;
    }

    private void cisao(NoB<T> pai, int i) {
        NoB<T> y = pai.getFilho(i);
        NoB<T> z = new NoB<>(t, y.isFolha());
        T meio = y.getChave(t - 1);
        log("Cisão de " + y + ": " + meio + " sobe");

        for (int j = 0; j < t - 1; j++) {
            z.setChave(j, y.getChave(j + t));
            y.setChave(j + t, null);
        }
        if (!y.isFolha()) {
            for (int j = 0; j < t; j++) {
                z.setFilho(j, y.getFilho(j + t));
                y.setFilho(j + t, null);
            }
        }
        z.setNumChaves(t - 1);
        y.setChave(t - 1, null);
        y.setNumChaves(t - 1);

        for (int j = pai.getNumChaves(); j > i; j--)
            pai.setFilho(j + 1, pai.getFilho(j));
        pai.setFilho(i + 1, z);
        for (int j = pai.getNumChaves() - 1; j >= i; j--)
            pai.setChave(j + 1, pai.getChave(j));
        pai.setChave(i, meio);
        pai.setNumChaves(pai.getNumChaves() + 1);
    }

    public boolean remover(T chave) {
        if (buscar(chave) == null)
            return false;

        NoB<T> no = raiz;
        while (true) {
            int i = posicao(no, chave);
            boolean estaNoNo = i < no.getNumChaves() && chave.compareTo(no.getChave(i)) == 0;

            if (estaNoNo && no.isFolha()) {
                log("Caso 1: remove " + chave + " da folha " + no);
                removerChaveDaFolha(no, i);
                break;
            }

            if (estaNoNo) {
                NoB<T> y = no.getFilho(i);
                NoB<T> z = no.getFilho(i + 1);
                if (y.getNumChaves() >= t) {
                    T antecessor = maiorChave(y);
                    log("Caso 2a: " + chave + " é substituída pela antecessora " + antecessor);
                    no.setChave(i, antecessor);
                    chave = antecessor;
                    no = y;
                } else if (z.getNumChaves() >= t) {
                    T sucessor = menorChave(z);
                    log("Caso 2b: " + chave + " é substituída pela sucessora " + sucessor);
                    no.setChave(i, sucessor);
                    chave = sucessor;
                    no = z;
                } else {
                    logSemQuebra("Caso 2c: ");
                    fundir(no, i);
                    no = y;
                }
                continue;
            }

            NoB<T> filho = no.getFilho(i);
            if (filho.estaNoMinimo()) {
                NoB<T> esquerdo = (i > 0) ? no.getFilho(i - 1) : null;
                NoB<T> direito = (i < no.getNumChaves()) ? no.getFilho(i + 1) : null;
                if (esquerdo != null && esquerdo.getNumChaves() >= t) {
                    emprestarDoEsquerdo(no, i);
                } else if (direito != null && direito.getNumChaves() >= t) {
                    emprestarDoDireito(no, i);
                } else if (direito != null) {
                    logSemQuebra("Caso 3b: ");
                    fundir(no, i);
                } else {
                    logSemQuebra("Caso 3b: ");
                    fundir(no, i - 1);
                    filho = esquerdo;
                }
            }
            no = filho;
        }

        if (raiz.getNumChaves() == 0 && !raiz.isFolha()) {
            raiz = raiz.getFilho(0);
            log("Raiz ficou vazia: altura diminui");
        }
        tamanho--;
        return true;
    }

    private void removerChaveDaFolha(NoB<T> folha, int i) {
        for (int j = i; j < folha.getNumChaves() - 1; j++)
            folha.setChave(j, folha.getChave(j + 1));
        folha.setChave(folha.getNumChaves() - 1, null);
        folha.setNumChaves(folha.getNumChaves() - 1);
    }

    private void fundir(NoB<T> pai, int i) {
        NoB<T> y = pai.getFilho(i);
        NoB<T> z = pai.getFilho(i + 1);
        log("fusão de " + y + " + " + pai.getChave(i) + " + " + z);

        int n = y.getNumChaves();
        y.setChave(n, pai.getChave(i));
        for (int j = 0; j < z.getNumChaves(); j++)
            y.setChave(n + 1 + j, z.getChave(j));
        if (!y.isFolha())
            for (int j = 0; j <= z.getNumChaves(); j++)
                y.setFilho(n + 1 + j, z.getFilho(j));
        y.setNumChaves(n + 1 + z.getNumChaves());

        for (int j = i; j < pai.getNumChaves() - 1; j++) {
            pai.setChave(j, pai.getChave(j + 1));
            pai.setFilho(j + 1, pai.getFilho(j + 2));
        }
        pai.setChave(pai.getNumChaves() - 1, null);
        pai.setFilho(pai.getNumChaves(), null);
        pai.setNumChaves(pai.getNumChaves() - 1);
    }

    private void emprestarDoEsquerdo(NoB<T> pai, int i) {
        NoB<T> filho = pai.getFilho(i);
        NoB<T> irmao = pai.getFilho(i - 1);
        int m = irmao.getNumChaves();
        log("Caso 3a: " + filho + " pega emprestado de " + irmao
                + " (" + irmao.getChave(m - 1) + " sobe, " + pai.getChave(i - 1) + " desce)");

        for (int j = filho.getNumChaves() - 1; j >= 0; j--)
            filho.setChave(j + 1, filho.getChave(j));
        if (!filho.isFolha())
            for (int j = filho.getNumChaves(); j >= 0; j--)
                filho.setFilho(j + 1, filho.getFilho(j));

        filho.setChave(0, pai.getChave(i - 1));
        if (!filho.isFolha()) {
            filho.setFilho(0, irmao.getFilho(m));
            irmao.setFilho(m, null);
        }
        pai.setChave(i - 1, irmao.getChave(m - 1));
        irmao.setChave(m - 1, null);

        irmao.setNumChaves(m - 1);
        filho.setNumChaves(filho.getNumChaves() + 1);
    }

    private void emprestarDoDireito(NoB<T> pai, int i) {
        NoB<T> filho = pai.getFilho(i);
        NoB<T> irmao = pai.getFilho(i + 1);
        int n = filho.getNumChaves();
        log("Caso 3a: " + filho + " pega emprestado de " + irmao
                + " (" + irmao.getChave(0) + " sobe, " + pai.getChave(i) + " desce)");

        filho.setChave(n, pai.getChave(i));
        if (!filho.isFolha())
            filho.setFilho(n + 1, irmao.getFilho(0));
        pai.setChave(i, irmao.getChave(0));

        int m = irmao.getNumChaves();
        for (int j = 0; j < m - 1; j++)
            irmao.setChave(j, irmao.getChave(j + 1));
        if (!irmao.isFolha())
            for (int j = 0; j < m; j++)
                irmao.setFilho(j, irmao.getFilho(j + 1));
        irmao.setChave(m - 1, null);
        irmao.setFilho(m, null);

        irmao.setNumChaves(m - 1);
        filho.setNumChaves(n + 1);
    }

    private T maiorChave(NoB<T> no) {
        while (!no.isFolha())
            no = no.getFilho(no.getNumChaves());
        return no.getChave(no.getNumChaves() - 1);
    }

    private T menorChave(NoB<T> no) {
        while (!no.isFolha())
            no = no.getFilho(0);
        return no.getChave(0);
    }

    public void mostrar() {
        if (isEmpty()) {
            System.out.println("(árvore vazia)");
            return;
        }
        List<StringBuilder> linhas = new ArrayList<>();
        for (int i = 0; i <= altura(); i++)
            linhas.add(new StringBuilder());
        desenhar(raiz, 0, new int[] { 0 }, linhas);
        for (StringBuilder linha : linhas)
            System.out.println(linha);
    }

    private int desenhar(NoB<T> no, int nivel, int[] proximaColunaFolha, List<StringBuilder> linhas) {
        String rotulo = no.toString();
        int inicio;
        if (no.isFolha()) {
            inicio = proximaColunaFolha[0];
            proximaColunaFolha[0] += rotulo.length() + 2;
        } else {
            int primeiro = desenhar(no.getFilho(0), nivel + 1, proximaColunaFolha, linhas);
            int ultimo = primeiro;
            for (int i = 1; i <= no.getNumChaves(); i++)
                ultimo = desenhar(no.getFilho(i), nivel + 1, proximaColunaFolha, linhas);
            inicio = (primeiro + ultimo) / 2 - rotulo.length() / 2;
        }

        StringBuilder linha = linhas.get(nivel);
        inicio = Math.max(inicio, linha.length() == 0 ? 0 : linha.length() + 2);
        while (linha.length() < inicio)
            linha.append(' ');
        linha.append(rotulo);
        return inicio + rotulo.length() / 2;
    }

    public String validar() {
        if (isEmpty())
            return null;
        int[] nivelFolha = { -1 };
        int[] contagem = { 0 };
        String erro = validar(raiz, 0, null, null, nivelFolha, contagem);
        if (erro == null && contagem[0] != tamanho)
            erro = "tamanho registrado (" + tamanho + ") difere do número de chaves (" + contagem[0] + ")";
        return erro;
    }

    private String validar(NoB<T> no, int nivel, T min, T max, int[] nivelFolha, int[] contagem) {
        int n = no.getNumChaves();
        if (no != raiz && n < t - 1)
            return no + " tem menos de t-1 chaves";
        if (n > 2 * t - 1)
            return no + " tem mais de 2t-1 chaves";
        if (no == raiz && n < 1)
            return "raiz sem chaves";
        for (int i = 0; i < n; i++) {
            T c = no.getChave(i);
            if (i > 0 && no.getChave(i - 1).compareTo(c) >= 0)
                return no + " não está ordenado";
            if ((min != null && c.compareTo(min) <= 0) || (max != null && c.compareTo(max) >= 0))
                return no + " está fora do intervalo do pai";
        }
        contagem[0] += n;
        if (no.isFolha()) {
            if (nivelFolha[0] == -1)
                nivelFolha[0] = nivel;
            else if (nivelFolha[0] != nivel)
                return "folhas em níveis diferentes";
            return null;
        }
        for (int i = 0; i <= n; i++) {
            NoB<T> filho = no.getFilho(i);
            if (filho == null)
                return no + " tem filho nulo";
            String erro = validar(filho, nivel + 1, (i == 0) ? min : no.getChave(i - 1),
                    (i == n) ? max : no.getChave(i), nivelFolha, contagem);
            if (erro != null)
                return erro;
        }
        return null;
    }

    public int altura() {
        int h = 0;
        for (NoB<T> no = raiz; !no.isFolha(); no = no.getFilho(0))
            h++;
        return h;
    }

    public void setExibirPassos(boolean exibirPassos) {
        this.exibirPassos = exibirPassos;
    }

    private void log(String mensagem) {
        if (exibirPassos)
            System.out.println(mensagem);
    }

    private void logSemQuebra(String mensagem) {
        if (exibirPassos)
            System.out.print(mensagem);
    }

    public int size() {
        return tamanho;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public int getT() {
        return t;
    }

    public NoB<T> root() {
        return raiz;
    }
}
