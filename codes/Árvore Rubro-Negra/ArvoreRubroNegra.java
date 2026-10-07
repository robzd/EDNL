public class ArvoreRubroNegra<T extends Comparable<T>> {
    private static final boolean RUBRO = NoRN.RUBRO;
    private static final boolean NEGRO = NoRN.NEGRO;

    private NoRN<T> raiz;
    private int tamanho = 0;
    private boolean exibirPassos = true;

    public NoRN<T> buscar(T chave) {
        return buscar(chave, false);
    }

    public NoRN<T> buscar(T chave, boolean exibirCaminho) {
        NoRN<T> no = raiz;
        while (no != null) {
            if (exibirCaminho)
                System.out.println("  visitando " + no);
            int c = chave.compareTo(no.getChave());
            if (c == 0)
                return no;
            no = (c < 0) ? no.getEsquerdo() : no.getDireito();
        }
        return null;
    }

    public boolean inserir(T chave) {
        NoRN<T> v = inserirNaPosicao(chave, RUBRO);
        if (v == null)
            return false;
        corrigirInsercao(v);
        return true;
    }

    private NoRN<T> inserirNaPosicao(T chave, boolean cor) {
        NoRN<T> pai = null;
        NoRN<T> no = raiz;
        while (no != null) {
            int c = chave.compareTo(no.getChave());
            if (c == 0)
                return null;
            pai = no;
            no = (c < 0) ? no.getEsquerdo() : no.getDireito();
        }

        NoRN<T> novo = new NoRN<>(chave, cor, pai);
        if (pai == null)
            raiz = novo;
        else if (chave.compareTo(pai.getChave()) < 0)
            pai.setEsquerdo(novo);
        else
            pai.setDireito(novo);
        tamanho++;
        return novo;
    }

    private void corrigirInsercao(NoRN<T> v) {
        while (true) {
            NoRN<T> w = v.getPai();
            if (w == null) {
                if (v.isRubro())
                    log("Raiz " + v.getChave() + " pintada de negro");
                v.setCor(NEGRO);
                return;
            }
            if (!w.isRubro()) {
                log("Caso 1: pai " + w + " é negro, nada a fazer");
                return;
            }

            NoRN<T> t = w.getPai();
            NoRN<T> u = (w == t.getEsquerdo()) ? t.getDireito() : t.getEsquerdo();

            if (cor(u) == RUBRO) {
                t.setCor(RUBRO);
                w.setCor(NEGRO);
                u.setCor(NEGRO);
                log("Caso 2: tio " + u.getChave() + " é rubro -> recoloração: " + t + " " + w + " " + u);
                v = t;
                continue;
            }

            boolean wEsquerdo = w == t.getEsquerdo();
            boolean vEsquerdo = v == w.getEsquerdo();
            NoRN<T> novoTopo;
            if (wEsquerdo && vEsquerdo) {
                log("Caso 3a: rotação direita simples em " + t.getChave());
                rotacaoDireita(t);
                novoTopo = w;
            } else if (!wEsquerdo && !vEsquerdo) {
                log("Caso 3b: rotação esquerda simples em " + t.getChave());
                rotacaoEsquerda(t);
                novoTopo = w;
            } else if (!wEsquerdo) {
                log("Caso 3c: rotação esquerda dupla em " + t.getChave());
                rotacaoDireita(w);
                rotacaoEsquerda(t);
                novoTopo = v;
            } else {
                log("Caso 3d: rotação direita dupla em " + t.getChave());
                rotacaoEsquerda(w);
                rotacaoDireita(t);
                novoTopo = v;
            }
            novoTopo.setCor(NEGRO);
            t.setCor(RUBRO);
            return;
        }
    }

    public boolean remover(T chave) {
        NoRN<T> v = buscar(chave);
        if (v == null)
            return false;

        NoRN<T> x;
        NoRN<T> paiX;
        boolean corRemovida;

        if (v.getEsquerdo() == null || v.getDireito() == null) {
            x = (v.getEsquerdo() != null) ? v.getEsquerdo() : v.getDireito();
            paiX = v.getPai();
            corRemovida = v.getCor();
            logSituacao(v, v.getCor(), cor(x), (x == null) ? "nulo" : x.toString());
            substituir(v, x);
        } else {
            NoRN<T> sucessor = menor(v.getDireito());
            logSituacao(v, v.getCor(), sucessor.getCor(), sucessor.toString());
            corRemovida = sucessor.getCor();
            x = sucessor.getDireito();
            if (sucessor.getPai() == v) {
                paiX = sucessor;
            } else {
                paiX = sucessor.getPai();
                substituir(sucessor, x);
                sucessor.setDireito(v.getDireito());
                sucessor.getDireito().setPai(sucessor);
            }
            substituir(v, sucessor);
            sucessor.setEsquerdo(v.getEsquerdo());
            sucessor.getEsquerdo().setPai(sucessor);
            sucessor.setCor(v.getCor());
        }
        tamanho--;

        if (corRemovida == RUBRO) {
            log("Saiu um nó rubro: critérios mantidos");
        } else if (cor(x) == RUBRO) {
            x.setCor(NEGRO);
            log("x = " + x.getChave() + " é rubro: pintado de negro");
        } else {
            corrigirRemocao(x, paiX);
        }
        return true;
    }

    private void logSituacao(NoRN<T> v, boolean corV, boolean corSucessor, String sucessor) {
        int situacao;
        if (corV == RUBRO)
            situacao = (corSucessor == RUBRO) ? 1 : 4;
        else
            situacao = (corSucessor == RUBRO) ? 2 : 3;
        log("Situação " + situacao + ": v = " + v + ", sucessor = " + sucessor);
    }

    private void corrigirRemocao(NoRN<T> x, NoRN<T> pai) {
        while (x != raiz && cor(x) == NEGRO) {
            log("Duplo negro em " + ((x == null) ? "nulo" : x.getChave()) + " (pai " + pai.getChave() + ")");
            if (x == pai.getEsquerdo()) {
                NoRN<T> w = pai.getDireito();
                if (cor(w) == RUBRO) {
                    log("Caso 1: irmão " + w.getChave() + " rubro -> rotação esquerda em " + pai.getChave());
                    w.setCor(NEGRO);
                    pai.setCor(RUBRO);
                    rotacaoEsquerda(pai);
                    w = pai.getDireito();
                }
                if (cor(w.getEsquerdo()) == NEGRO && cor(w.getDireito()) == NEGRO) {
                    w.setCor(RUBRO);
                    if (pai.isRubro()) {
                        log("Caso 2b: irmão " + w.getChave() + " pintado de rubro e pai " + pai.getChave()
                                + " de negro");
                        pai.setCor(NEGRO);
                        return;
                    }
                    log("Caso 2a: irmão " + w.getChave() + " pintado de rubro, duplo negro sobe para "
                            + pai.getChave());
                    x = pai;
                    pai = x.getPai();
                    continue;
                }
                if (cor(w.getDireito()) == NEGRO) {
                    log("Caso 3: rotação direita em " + w.getChave() + " e troca de cores com o filho esquerdo");
                    w.getEsquerdo().setCor(NEGRO);
                    w.setCor(RUBRO);
                    rotacaoDireita(w);
                    w = pai.getDireito();
                }
                log("Caso 4: rotação esquerda em " + pai.getChave());
                w.setCor(pai.getCor());
                pai.setCor(NEGRO);
                w.getDireito().setCor(NEGRO);
                rotacaoEsquerda(pai);
                return;
            } else {
                NoRN<T> w = pai.getEsquerdo();
                if (cor(w) == RUBRO) {
                    log("Caso 1: irmão " + w.getChave() + " rubro -> rotação direita em " + pai.getChave());
                    w.setCor(NEGRO);
                    pai.setCor(RUBRO);
                    rotacaoDireita(pai);
                    w = pai.getEsquerdo();
                }
                if (cor(w.getEsquerdo()) == NEGRO && cor(w.getDireito()) == NEGRO) {
                    w.setCor(RUBRO);
                    if (pai.isRubro()) {
                        log("Caso 2b: irmão " + w.getChave() + " pintado de rubro e pai " + pai.getChave()
                                + " de negro");
                        pai.setCor(NEGRO);
                        return;
                    }
                    log("Caso 2a: irmão " + w.getChave() + " pintado de rubro, duplo negro sobe para "
                            + pai.getChave());
                    x = pai;
                    pai = x.getPai();
                    continue;
                }
                if (cor(w.getEsquerdo()) == NEGRO) {
                    log("Caso 3: rotação esquerda em " + w.getChave() + " e troca de cores com o filho direito");
                    w.getDireito().setCor(NEGRO);
                    w.setCor(RUBRO);
                    rotacaoEsquerda(w);
                    w = pai.getEsquerdo();
                }
                log("Caso 4: rotação direita em " + pai.getChave());
                w.setCor(pai.getCor());
                pai.setCor(NEGRO);
                w.getEsquerdo().setCor(NEGRO);
                rotacaoDireita(pai);
                return;
            }
        }
        if (x != null)
            x.setCor(NEGRO);
    }

    private void rotacaoEsquerda(NoRN<T> b) {
        NoRN<T> a = b.getDireito();
        b.setDireito(a.getEsquerdo());
        if (a.getEsquerdo() != null)
            a.getEsquerdo().setPai(b);
        substituir(b, a);
        a.setEsquerdo(b);
        b.setPai(a);
    }

    private void rotacaoDireita(NoRN<T> b) {
        NoRN<T> a = b.getEsquerdo();
        b.setEsquerdo(a.getDireito());
        if (a.getDireito() != null)
            a.getDireito().setPai(b);
        substituir(b, a);
        a.setDireito(b);
        b.setPai(a);
    }

    private void substituir(NoRN<T> no, NoRN<T> novo) {
        if (no.getPai() == null)
            raiz = novo;
        else if (no == no.getPai().getEsquerdo())
            no.getPai().setEsquerdo(novo);
        else
            no.getPai().setDireito(novo);
        if (novo != null)
            novo.setPai(no.getPai());
    }

    private NoRN<T> menor(NoRN<T> no) {
        while (no.getEsquerdo() != null)
            no = no.getEsquerdo();
        return no;
    }

    private boolean cor(NoRN<T> no) {
        return (no == null) ? NEGRO : no.getCor();
    }

    public void mostrar() {
        if (raiz == null) {
            System.out.println("(árvore vazia)");
            return;
        }

        String[][] matriz = new String[altura(raiz) + 1][tamanho];
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

    private void preencherMatriz(NoRN<T> no, int profundidade, int[] coluna, String[][] matriz) {
        if (no == null)
            return;
        preencherMatriz(no.getEsquerdo(), profundidade + 1, coluna, matriz);
        matriz[profundidade][coluna[0]] = no.toString();
        coluna[0]++;
        preencherMatriz(no.getDireito(), profundidade + 1, coluna, matriz);
    }

    public String validar() {
        if (raiz == null)
            return null;
        if (raiz.isRubro())
            return "a raiz é rubra (critério II)";
        String[] erro = { null };
        int[] contagem = { 0 };
        validar(raiz, null, null, erro, contagem);
        if (erro[0] == null && contagem[0] != tamanho)
            erro[0] = "tamanho registrado (" + tamanho + ") difere do número de nós (" + contagem[0] + ")";
        return erro[0];
    }

    private int validar(NoRN<T> no, T min, T max, String[] erro, int[] contagem) {
        if (no == null || erro[0] != null)
            return 0;
        contagem[0]++;
        if ((min != null && no.getChave().compareTo(min) <= 0) || (max != null && no.getChave().compareTo(max) >= 0))
            erro[0] = no + " fora de ordem";
        else if (no.isRubro() && (cor(no.getEsquerdo()) == RUBRO || cor(no.getDireito()) == RUBRO))
            erro[0] = no + " é rubro e tem filho rubro (critério III)";
        else if ((no.getEsquerdo() != null && no.getEsquerdo().getPai() != no)
                || (no.getDireito() != null && no.getDireito().getPai() != no))
            erro[0] = "ponteiro de pai incorreto abaixo de " + no;
        if (erro[0] != null)
            return 0;

        int esquerda = validar(no.getEsquerdo(), min, no.getChave(), erro, contagem);
        int direita = validar(no.getDireito(), no.getChave(), max, erro, contagem);
        if (erro[0] == null && esquerda != direita)
            erro[0] = no + " tem caminhos com números diferentes de nós negros (critério IV)";
        return esquerda + (no.isRubro() ? 0 : 1);
    }

    public int alturaNegra() {
        int h = 0;
        for (NoRN<T> no = raiz; no != null; no = no.getEsquerdo())
            if (!no.isRubro())
                h++;
        return h;
    }

    public int altura() {
        return altura(raiz);
    }

    private int altura(NoRN<T> no) {
        if (no == null)
            return -1;
        return 1 + Math.max(altura(no.getEsquerdo()), altura(no.getDireito()));
    }

    public void setExibirPassos(boolean exibirPassos) {
        this.exibirPassos = exibirPassos;
    }

    private void log(String mensagem) {
        if (exibirPassos)
            System.out.println(mensagem);
    }

    public int size() {
        return tamanho;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public NoRN<T> root() {
        return raiz;
    }
}
