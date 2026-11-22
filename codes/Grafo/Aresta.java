public class Aresta {
    private Object elemento;
    private Vertice origem;
    private Vertice destino;
    private boolean direcionada;

    public Aresta(Vertice origem, Vertice destino, Object elemento, boolean direcionada) {
        this.origem = origem;
        this.destino = destino;
        this.elemento = elemento;
        this.direcionada = direcionada;
    }

    public Object getElemento() {
        return elemento;
    }

    public void setElemento(Object elemento) {
        this.elemento = elemento;
    }

    public Vertice getOrigem() {
        return origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public boolean isDirecionada() {
        return direcionada;
    }

    @Override
    public String toString() {
        return origem + (direcionada ? " -> " : " -- ") + destino + " (" + elemento + ")";
    }
}
