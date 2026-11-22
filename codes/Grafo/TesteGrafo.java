import java.util.ArrayList;
import java.util.Scanner;

public class TesteGrafo {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== GRAFO =====");
            System.out.println("1 - Novo grafo vazio");
            System.out.println("2 - Exemplo de grafo simples");
            System.out.println("3 - Exemplo de multigrafo misto (arestas paralelas, laços e arestas direcionadas)");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            switch (entrada.nextLine().trim()) {
                case "1":
                    executar(entrada, new Grafo());
                    break;
                case "2":
                    executar(entrada, grafoSimples());
                    break;
                case "3":
                    executar(entrada, multigrafoMisto());
                    break;
                case "0":
                    entrada.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static Grafo grafoSimples() {
        Grafo g = new Grafo();
        Vertice a = g.inserirVertice("A");
        Vertice b = g.inserirVertice("B");
        Vertice c = g.inserirVertice("C");
        Vertice d = g.inserirVertice("D");
        Vertice e = g.inserirVertice("E");
        g.inserirAresta(a, b, 4);
        g.inserirAresta(a, c, 9);
        g.inserirAresta(b, c, 6);
        g.inserirAresta(c, d, 1);
        g.inserirAresta(d, e, 8);
        return g;
    }

    private static Grafo multigrafoMisto() {
        Grafo g = new Grafo();
        Vertice x = g.inserirVertice("X");
        Vertice y = g.inserirVertice("Y");
        Vertice z = g.inserirVertice("Z");
        Vertice w = g.inserirVertice("W");
        g.inserirAresta(x, y, 3);
        g.inserirAresta(x, y, 11);
        g.inserirAresta(y, z, 5);
        g.inserirAresta(z, z, 2);
        g.inserirArestaDirecionada(w, x, 7);
        g.inserirArestaDirecionada(x, w, 12);
        g.inserirArestaDirecionada(z, w, 6);
        g.inserirArestaDirecionada(w, w, 1);
        return g;
    }

    private static void executar(Scanner entrada, Grafo g) {
        g.mostrar();
        while (true) {
            System.out.println("\n===== GRAFO =====");
            System.out.println("1 - Inserir vértice");
            System.out.println("2 - Inserir aresta não direcionada");
            System.out.println("3 - Inserir aresta direcionada");
            System.out.println("4 - Remover vértice");
            System.out.println("5 - Remover aresta");
            System.out.println("6 - Substituir elemento de um vértice");
            System.out.println("7 - Substituir elemento de uma aresta");
            System.out.println("8 - Arestas incidentes a um vértice");
            System.out.println("9 - Verificar se dois vértices são adjacentes");
            System.out.println("10 - Vértice oposto em uma aresta");
            System.out.println("11 - Vértices finais e direção de uma aresta");
            System.out.println("12 - Listar vértices e arestas");
            System.out.println("13 - Mostrar matriz de adjacência");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            String opcao = entrada.nextLine().trim();
            try {
                switch (opcao) {
                    case "1":
                        System.out.print("Elemento(s) do(s) vértice(s) (separados por espaço): ");
                        for (String elemento : entrada.nextLine().trim().split("\\s+"))
                            if (!elemento.isEmpty())
                                System.out.println("Inserido: " + g.inserirVertice(elemento));
                        g.mostrar();
                        break;
                    case "2":
                    case "3": {
                        Vertice v = lerVertice(entrada, g, "Vértice de origem: ");
                        Vertice w = lerVertice(entrada, g, "Vértice de destino: ");
                        System.out.print("Elemento da aresta: ");
                        String elemento = entrada.nextLine().trim();
                        Aresta e = opcao.equals("3") ? g.inserirArestaDirecionada(v, w, elemento)
                                : g.inserirAresta(v, w, elemento);
                        System.out.println("Inserida: " + e);
                        g.mostrar();
                        break;
                    }
                    case "4": {
                        Vertice v = lerVertice(entrada, g, "Vértice a remover: ");
                        System.out.println("Removido o vértice " + g.removeVertice(v));
                        g.mostrar();
                        break;
                    }
                    case "5": {
                        Aresta e = lerAresta(entrada, g);
                        System.out.println("Removida a aresta de elemento " + g.removeAresta(e));
                        g.mostrar();
                        break;
                    }
                    case "6": {
                        Vertice v = lerVertice(entrada, g, "Vértice: ");
                        System.out.print("Novo elemento: ");
                        Object antigo = g.substituir(v, entrada.nextLine().trim());
                        System.out.println("Elemento " + antigo + " substituído por " + v);
                        break;
                    }
                    case "7": {
                        Aresta e = lerAresta(entrada, g);
                        System.out.print("Novo elemento: ");
                        Object antigo = g.substituir(e, entrada.nextLine().trim());
                        System.out.println("Elemento " + antigo + " substituído: " + e);
                        break;
                    }
                    case "8": {
                        Vertice v = lerVertice(entrada, g, "Vértice: ");
                        ArrayList<Aresta> incidentes = g.arestasIncidentes(v);
                        System.out.println(incidentes.size() + " aresta(s) incidente(s) a " + v + ":");
                        for (Aresta e : incidentes)
                            System.out.println("  " + e);
                        break;
                    }
                    case "9": {
                        Vertice v = lerVertice(entrada, g, "Primeiro vértice: ");
                        Vertice w = lerVertice(entrada, g, "Segundo vértice: ");
                        System.out.println(v + " e " + w + (g.eAdjacente(v, w) ? " são" : " não são") + " adjacentes.");
                        break;
                    }
                    case "10": {
                        Aresta e = lerAresta(entrada, g);
                        Vertice v = lerVertice(entrada, g, "Vértice: ");
                        System.out.println("Oposto de " + v + " em " + e + ": " + g.oposto(v, e));
                        break;
                    }
                    case "11": {
                        Aresta e = lerAresta(entrada, g);
                        Vertice[] finais = g.finalVertices(e);
                        System.out.println("Vértices finais: " + finais[0] + " e " + finais[1]
                                + (g.eDirecionado(e) ? " (direcionada)" : " (não direcionada)"));
                        break;
                    }
                    case "12":
                        System.out.println("Vértices (" + g.numVertices() + "): " + g.vertices());
                        System.out.println("Arestas (" + g.numArestas() + "):");
                        for (Aresta e : g.arestas())
                            System.out.println("  " + e);
                        break;
                    case "13":
                        g.mostrar();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("ERRO: " + ex.getMessage());
            }
        }
    }

    private static Vertice lerVertice(Scanner entrada, Grafo g, String mensagem) {
        System.out.print(mensagem);
        String nome = entrada.nextLine().trim();
        for (Vertice v : g.vertices())
            if (v.toString().equals(nome))
                return v;
        throw new IllegalArgumentException("Vértice " + nome + " não encontrado");
    }

    private static Aresta lerAresta(Scanner entrada, Grafo g) {
        ArrayList<Aresta> arestas = g.arestas();
        if (arestas.isEmpty())
            throw new IllegalArgumentException("O grafo não possui arestas");
        for (int i = 0; i < arestas.size(); i++)
            System.out.println("  " + (i + 1) + " - " + arestas.get(i));
        System.out.print("Número da aresta: ");
        try {
            int i = Integer.parseInt(entrada.nextLine().trim()) - 1;
            if (i >= 0 && i < arestas.size())
                return arestas.get(i);
        } catch (NumberFormatException ex) {
        }
        throw new IllegalArgumentException("Número de aresta inválido");
    }
}
