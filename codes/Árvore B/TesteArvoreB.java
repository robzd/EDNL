import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class TesteArvoreB {

    private static final String EXEMPLO_TESTE = "remover 15 8 4 1 12 (passa pelos casos 1, 2a, 2b, 2c, 3a e 3b)";

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ÁRVORE B =====");
            System.out.println("1 - Nova árvore com chaves numéricas");
            System.out.println("2 - Nova árvore com chaves de texto (letras)");
            System.out.println("3 - Árvore de exemplo (t=2, chaves 1 a 15)");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            switch (entrada.nextLine().trim()) {
                case "1":
                    executar(entrada, new ArvoreB<Integer>(lerOrdem(entrada)), Integer::parseInt);
                    break;
                case "2":
                    executar(entrada, new ArvoreB<String>(lerOrdem(entrada)), String::toUpperCase);
                    break;
                case "3":
                    ArvoreB<Integer> exemplo = new ArvoreB<>(2);
                    exemplo.setExibirPassos(false);
                    for (int chave = 1; chave <= 15; chave++)
                        exemplo.inserir(chave);
                    exemplo.setExibirPassos(true);
                    System.out.println("\nÁrvore de exemplo carregada (t=2, chaves 1 a 15).");
                    System.out.println("Exemplo de teste: " + EXEMPLO_TESTE);
                    executar(entrada, exemplo, Integer::parseInt);
                    break;
                case "0":
                    entrada.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static <T extends Comparable<T>> void executar(Scanner entrada, ArvoreB<T> arvore,
            Function<String, T> conversor) {
        arvore.mostrar();
        while (true) {
            System.out.println("\n===== ÁRVORE B (t=" + arvore.getT() + ") =====");
            System.out.println("1 - Inserir");
            System.out.println("2 - Remover");
            System.out.println("3 - Buscar");
            System.out.println("4 - Mostrar");
            System.out.println("5 - Verificar propriedades");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            switch (entrada.nextLine().trim()) {
                case "1":
                    for (T chave : lerChaves(entrada, "Chave(s) para inserir (separadas por espaço): ", conversor)) {
                        System.out.println("\nInserir " + chave);
                        if (!arvore.inserir(chave))
                            System.out.println("Chave " + chave + " já existe.");
                        arvore.mostrar();
                    }
                    break;
                case "2":
                    for (T chave : lerChaves(entrada, "Chave(s) para remover (separadas por espaço): ", conversor)) {
                        System.out.println("\nRemover " + chave);
                        if (!arvore.remover(chave))
                            System.out.println("Chave " + chave + " não encontrada.");
                        arvore.mostrar();
                    }
                    break;
                case "3":
                    for (T chave : lerChaves(entrada, "Chave(s) para buscar: ", conversor)) {
                        System.out.println("\nBuscar " + chave);
                        NoB<T> no = arvore.buscar(chave, true);
                        if (no == null)
                            System.out.println("Chave " + chave + " não encontrada.");
                        else
                            System.out.println("Encontrada no nó " + no + (no.isFolha() ? " (folha)" : " (interno)"));
                    }
                    break;
                case "4":
                    System.out.println("Chaves: " + arvore.size() + " | Altura: " + arvore.altura());
                    arvore.mostrar();
                    break;
                case "5":
                    String erro = arvore.validar();
                    System.out.println(
                            erro == null ? "Todas as propriedades da Árvore B estão válidas." : "ERRO: " + erro);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static int lerOrdem(Scanner entrada) {
        while (true) {
            System.out.print("Ordem t (mínimo 2): ");
            try {
                int t = Integer.parseInt(entrada.nextLine().trim());
                if (t >= 2)
                    return t;
            } catch (NumberFormatException e) {
            }
            System.out.println("Digite um inteiro maior ou igual a 2.");
        }
    }

    private static <T> List<T> lerChaves(Scanner entrada, String mensagem, Function<String, T> conversor) {
        System.out.print(mensagem);
        List<T> chaves = new ArrayList<>();
        String linha = entrada.nextLine().trim();
        if (linha.isEmpty())
            return chaves;
        try {
            for (String parte : linha.split("\\s+"))
                chaves.add(conversor.apply(parte));
            return chaves;
        } catch (NumberFormatException e) {
            System.out.println("Digite apenas números inteiros.");
            return new ArrayList<>();
        }
    }
}
