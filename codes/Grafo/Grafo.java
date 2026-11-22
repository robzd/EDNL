import java.util.ArrayList;

public class Grafo {
    private ArrayList<Vertice> vertices = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Aresta>>> matriz = new ArrayList<>();

    public Vertice[] finalVertices(Aresta e) {
        validar(e);
        return new Vertice[] { e.getOrigem(), e.getDestino() };
    }

    public Vertice oposto(Vertice v, Aresta e) {
        validar(v);
        validar(e);
        if (e.getOrigem() == v)
            return e.getDestino();
        if (e.getDestino() == v)
            return e.getOrigem();
        throw new IllegalArgumentException("A aresta " + e + " não é incidente ao vértice " + v);
    }

    public boolean eAdjacente(Vertice v, Vertice w) {
        validar(v);
        validar(w);
        return !celula(v, w).isEmpty() || !celula(w, v).isEmpty();
    }

    public Object substituir(Vertice v, Object x) {
        validar(v);
        Object antigo = v.getElemento();
        v.setElemento(x);
        return antigo;
    }

    public Object substituir(Aresta e, Object x) {
        validar(e);
        Object antigo = e.getElemento();
        e.setElemento(x);
        return antigo;
    }

    public Vertice inserirVertice(Object o) {
        Vertice v = new Vertice(o, vertices.size());
        vertices.add(v);
        for (ArrayList<ArrayList<Aresta>> linha : matriz)
            linha.add(new ArrayList<>());
        ArrayList<ArrayList<Aresta>> novaLinha = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++)
            novaLinha.add(new ArrayList<>());
        matriz.add(novaLinha);
        return v;
    }

    public Aresta inserirAresta(Vertice v, Vertice w, Object o) {
        validar(v);
        validar(w);
        Aresta e = new Aresta(v, w, o, false);
        celula(v, w).add(e);
        if (v != w)
            celula(w, v).add(e);
        return e;
    }

    public Aresta inserirArestaDirecionada(Vertice v, Vertice w, Object o) {
        validar(v);
        validar(w);
        Aresta e = new Aresta(v, w, o, true);
        celula(v, w).add(e);
        return e;
    }

    public Object removeVertice(Vertice v) {
        validar(v);
        int i = v.getIndice();
        matriz.remove(i);
        for (ArrayList<ArrayList<Aresta>> linha : matriz)
            linha.remove(i);
        vertices.remove(i);
        for (int k = i; k < vertices.size(); k++)
            vertices.get(k).setIndice(k);
        v.setIndice(-1);
        return v.getElemento();
    }

    public Object removeAresta(Aresta e) {
        validar(e);
        celula(e.getOrigem(), e.getDestino()).remove(e);
        if (!e.isDirecionada() && e.getOrigem() != e.getDestino())
            celula(e.getDestino(), e.getOrigem()).remove(e);
        return e.getElemento();
    }

    public boolean eDirecionado(Aresta e) {
        validar(e);
        return e.isDirecionada();
    }

    public ArrayList<Aresta> arestasIncidentes(Vertice v) {
        validar(v);
        int i = v.getIndice();
        ArrayList<Aresta> incidentes = new ArrayList<>();
        for (ArrayList<Aresta> c : matriz.get(i))
            incidentes.addAll(c);
        for (int j = 0; j < vertices.size(); j++)
            if (j != i)
                for (Aresta e : matriz.get(j).get(i))
                    if (e.isDirecionada())
                        incidentes.add(e);
        return incidentes;
    }

    public ArrayList<Vertice> vertices() {
        return new ArrayList<>(vertices);
    }

    public ArrayList<Aresta> arestas() {
        ArrayList<Aresta> todas = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++)
            for (int j = 0; j < vertices.size(); j++)
                for (Aresta e : matriz.get(i).get(j))
                    if (e.isDirecionada() || i <= j)
                        todas.add(e);
        return todas;
    }

    public int numVertices() {
        return vertices.size();
    }

    public int numArestas() {
        return arestas().size();
    }

    public void mostrar() {
        if (vertices.isEmpty()) {
            System.out.println("(grafo vazio)");
            return;
        }

        int n = vertices.size();
        String[][] tabela = new String[n + 1][n + 1];
        tabela[0][0] = "";
        for (int i = 0; i < n; i++) {
            tabela[0][i + 1] = vertices.get(i).toString();
            tabela[i + 1][0] = vertices.get(i).toString();
            for (int j = 0; j < n; j++)
                tabela[i + 1][j + 1] = formatarCelula(matriz.get(i).get(j));
        }

        int largura = 0;
        for (String[] linha : tabela)
            for (String valor : linha)
                largura = Math.max(largura, valor.length() + 2);

        StringBuilder sb = new StringBuilder();
        for (String[] linha : tabela) {
            for (String valor : linha)
                sb.append(String.format("%-" + largura + "s", valor));
            sb.append("\n");
        }
        System.out.print(sb);
    }

    private String formatarCelula(ArrayList<Aresta> c) {
        if (c.isEmpty())
            return "-";
        StringBuilder sb = new StringBuilder("{");
        for (int k = 0; k < c.size(); k++) {
            if (k > 0)
                sb.append(",");
            sb.append(c.get(k).getElemento());
        }
        return sb.append("}").toString();
    }

    private ArrayList<Aresta> celula(Vertice v, Vertice w) {
        return matriz.get(v.getIndice()).get(w.getIndice());
    }

    private void validar(Vertice v) {
        if (v == null || v.getIndice() < 0 || v.getIndice() >= vertices.size() || vertices.get(v.getIndice()) != v)
            throw new IllegalArgumentException("Vértice " + v + " não pertence ao grafo");
    }

    private void validar(Aresta e) {
        if (e == null)
            throw new IllegalArgumentException("Aresta nula");
        validar(e.getOrigem());
        validar(e.getDestino());
        if (!celula(e.getOrigem(), e.getDestino()).contains(e))
            throw new IllegalArgumentException("Aresta " + e + " não pertence ao grafo");
    }
}
