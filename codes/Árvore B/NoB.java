public class NoB<T extends Comparable<T>> {
    private Object[] chaves;
    private int t;
    private NoB<T>[] filhos;
    private int numChaves;
    private boolean folha;

    @SuppressWarnings("unchecked")
    public NoB(int t, boolean folha) {
        this.t = t;
        this.chaves = new Object[2 * t - 1];
        this.filhos = new NoB[2 * t];
        this.numChaves = 0;
        this.folha = folha;
    }

    public NoB(int t) {
        this(t, true);
    }

    @SuppressWarnings("unchecked")
    public T getChave(int i) {
        return (T) chaves[i];
    }

    public void setChave(int i, T chave) {
        chaves[i] = chave;
    }

    public NoB<T> getFilho(int i) {
        return filhos[i];
    }

    public void setFilho(int i, NoB<T> filho) {
        filhos[i] = filho;
    }

    public int getNumChaves() {
        return numChaves;
    }

    public void setNumChaves(int numChaves) {
        this.numChaves = numChaves;
    }

    public boolean isFolha() {
        return folha;
    }

    public void setFolha(boolean folha) {
        this.folha = folha;
    }

    public int getT() {
        return t;
    }

    public boolean estaCheio() {
        return numChaves == 2 * t - 1;
    }

    public boolean estaNoMinimo() {
        return numChaves == t - 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < numChaves; i++) {
            if (i > 0)
                sb.append(' ');
            sb.append(chaves[i]);
        }
        return sb.append(']').toString();
    }
}
