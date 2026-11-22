public class Vertice {
    private Object elemento;
    private int indice;

    public Vertice(Object elemento, int indice) {
        this.elemento = elemento;
        this.indice = indice;
    }

    public Object getElemento() {
        return elemento;
    }

    public void setElemento(Object elemento) {
        this.elemento = elemento;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    @Override
    public String toString() {
        return String.valueOf(elemento);
    }
}
