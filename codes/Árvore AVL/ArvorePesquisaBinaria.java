public class ArvorePesquisaBinaria {
    protected No raiz;
    protected int tamanho = 0;

    public No search(int chave) {
        No no = raiz;
        while (no != null && chave != no.getChave())
            no = (chave < no.getChave()) ? no.getEsquerdo() : no.getDireito();
        return no;
    }

    // retorna o nó inserido, ou null se a chave já existe
    public No insert(int chave, Object objeto) {
        if (raiz == null) {
            raiz = new No(chave, objeto, null);
            tamanho++;
            return raiz;
        }

        No no = raiz;
        while (true) {
            if (chave == no.getChave())
                return null;
            No proximo = (chave < no.getChave()) ? no.getEsquerdo() : no.getDireito();
            if (proximo == null)
                break;
            no = proximo;
        }

        No novo = new No(chave, objeto, no);
        if (chave < no.getChave())
            no.setEsquerdo(novo);
        else
            no.setDireito(novo);
        tamanho++;
        aposInsercao(novo);
        return novo;
    }

    // retorna false se a chave não existe
    public boolean remove(int chave) {
        No no = search(chave);
        if (no == null)
            return false;

        // nó com dois filhos: copia o sucessor e remove o sucessor no lugar dele
        if (no.getEsquerdo() != null && no.getDireito() != null) {
            No sucessor = menorChave(no.getDireito());
            no.setChave(sucessor.getChave());
            no.setElemento(sucessor.getElemento());
            no = sucessor;
        }

        No pai = no.getPai();
        boolean eraEsquerdo = pai != null && no == pai.getEsquerdo();
        trocarNos(no, (no.getEsquerdo() != null) ? no.getEsquerdo() : no.getDireito());
        tamanho--;
        aposRemocao(pai, eraEsquerdo);
        return true;
    }

    // ganchos para as subclasses (a AVL usa para rebalancear)
    protected void aposInsercao(No novo) {
    }

    protected void aposRemocao(No pai, boolean eraEsquerdo) {
    }

    protected No menorChave(No no) {
        while (no.getEsquerdo() != null)
            no = no.getEsquerdo();
        return no;
    }

    // coloca "filho" no lugar de "no" (no pai de "no" ou na raiz)
    protected void trocarNos(No no, No filho) {
        if (no.getPai() == null)
            raiz = filho;
        else if (no == no.getPai().getEsquerdo())
            no.getPai().setEsquerdo(filho);
        else
            no.getPai().setDireito(filho);

        if (filho != null)
            filho.setPai(no.getPai());
    }

    // texto de cada nó no mostrar(); a AVL sobrescreve para incluir o FB
    protected String rotulo(No no) {
        return String.valueOf(no.getChave());
    }

    public void mostrar() {
        if (raiz == null) {
            System.out.println("(árvore vazia)");
            return;
        }

        // linha = profundidade do nó, coluna = posição do nó no percurso em ordem
        int linhas = altura(raiz) + 1;
        String[][] matriz = new String[linhas][tamanho];
        preencherMatriz(raiz, 0, new int[] { 0 }, matriz);

        int largura = 0;
        for (String[] linha : matriz)
            for (String valor : linha)
                if (valor != null)
                    largura = Math.max(largura, valor.length() + 1);

        StringBuilder sb = new StringBuilder();
        for (String[] linha : matriz) {
            for (String valor : linha)
                sb.append(String.format("%-" + largura + "s", (valor == null) ? "" : valor));
            sb.append("\n");
        }
        System.out.print(sb);
    }

    private void preencherMatriz(No no, int profundidade, int[] coluna, String[][] matriz) {
        if (no == null)
            return;
        preencherMatriz(no.getEsquerdo(), profundidade + 1, coluna, matriz);
        matriz[profundidade][coluna[0]] = rotulo(no);
        coluna[0]++;
        preencherMatriz(no.getDireito(), profundidade + 1, coluna, matriz);
    }

    private int altura(No no) {
        if (no == null)
            return -1;
        return 1 + Math.max(altura(no.getEsquerdo()), altura(no.getDireito()));
    }

    public int size() {
        return tamanho;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public No root() {
        return raiz;
    }
}
