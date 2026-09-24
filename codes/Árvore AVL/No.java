public class No {
    private int chave;
    private Object elemento;
    private int fb; // fator de balanceamento: altura(esq) - altura(dir)
    private No pai, esquerdo, direito;

    public No(int chave, Object elemento, No pai) {
        this.chave = chave;
        this.elemento = elemento;
        this.pai = pai;
    }

    public int getChave() {
        return chave;
    }

    public void setChave(int c) {
        this.chave = c;
    }

    public Object getElemento() {
        return elemento;
    }

    public void setElemento(Object o) {
        this.elemento = o;
    }

    public int getFb() {
        return fb;
    }

    public void setFb(int fb) {
        this.fb = fb;
    }

    public No getPai() {
        return pai;
    }

    public void setPai(No p) {
        this.pai = p;
    }

    public No getEsquerdo() {
        return esquerdo;
    }

    public void setEsquerdo(No e) {
        this.esquerdo = e;
    }

    public No getDireito() {
        return direito;
    }

    public void setDireito(No d) {
        this.direito = d;
    }
}
