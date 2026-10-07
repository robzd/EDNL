public class NoRN<T extends Comparable<T>> {
    public static final boolean RUBRO = true;
    public static final boolean NEGRO = false;

    private T chave;
    private boolean cor;
    private NoRN<T> pai, esquerdo, direito;

    public NoRN(T chave, boolean cor, NoRN<T> pai) {
        this.chave = chave;
        this.cor = cor;
        this.pai = pai;
    }

    public T getChave() {
        return chave;
    }

    public void setChave(T chave) {
        this.chave = chave;
    }

    public boolean getCor() {
        return cor;
    }

    public void setCor(boolean cor) {
        this.cor = cor;
    }

    public boolean isRubro() {
        return cor == RUBRO;
    }

    public NoRN<T> getPai() {
        return pai;
    }

    public void setPai(NoRN<T> pai) {
        this.pai = pai;
    }

    public NoRN<T> getEsquerdo() {
        return esquerdo;
    }

    public void setEsquerdo(NoRN<T> esquerdo) {
        this.esquerdo = esquerdo;
    }

    public NoRN<T> getDireito() {
        return direito;
    }

    public void setDireito(NoRN<T> direito) {
        this.direito = direito;
    }

    @Override
    public String toString() {
        return chave + (isRubro() ? "(R)" : "(N)");
    }
}
