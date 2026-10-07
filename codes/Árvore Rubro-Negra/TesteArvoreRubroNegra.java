import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class TesteArvoreRubroNegra {

    private static final int[] CRESCENTE = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
    private static final int[] MISTA = { 30, 20, 10, 50, 60, 40, 45, 5, 8, 47, 46 };
    private static final int[] REMOCAO = { 150, 65, 60, 125, 115, 10, 15, 85, 90, 40, 30, 120 };
    private static final String EXEMPLO_TESTE = "remover 150 120 125 65 15 (passa pelas 4 situações e pelos casos 1 a 4)";

    private static final Function<String, Integer> NUMERO = Integer::parseInt;
    private static final Function<String, String> TEXTO = String::toUpperCase;

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ÁRVORE RUBRO-NEGRA =====");
            System.out.println("1 - Nova árvore com chaves numéricas");
            System.out.println("2 - Nova árvore com chaves de texto (letras)");
            System.out.println("3 - Exemplo de inserção crescente (10 a 100, passo a passo)");
            System.out.println("4 - Exemplo de inserção mista (rotações simples e duplas, passo a passo)");
            System.out.println("5 - Árvore de exemplo para remoção");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            switch (entrada.nextLine().trim()) {
                case "1":
                    executar(entrada, new ArvoreRubroNegra<Integer>(), NUMERO);
                    break;
                case "2":
                    executar(entrada, new ArvoreRubroNegra<String>(), TEXTO);
                    break;
                case "3":
                    executar(entrada, carregar(CRESCENTE, true), NUMERO);
                    break;
                case "4":
                    executar(entrada, carregar(MISTA, true), NUMERO);
                    break;
                case "5":
                    ArvoreRubroNegra<Integer> arvore = carregar(REMOCAO, false);
                    System.out.println("\nÁrvore de exemplo carregada.\nExemplo de teste: " + EXEMPLO_TESTE);
                    executar(entrada, arvore, NUMERO);
                    break;
                case "0":
                    entrada.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static ArvoreRubroNegra<Integer> carregar(int[] chaves, boolean exibirPassos) {
        ArvoreRubroNegra<Integer> arvore = new ArvoreRubroNegra<>();
        arvore.setExibirPassos(exibirPassos);
        for (int chave : chaves) {
            if (exibirPassos)
                System.out.println("\nInserir " + chave);
            arvore.inserir(chave);
            if (exibirPassos)
                arvore.mostrar();
        }
        arvore.setExibirPassos(true);
        return arvore;
    }

    private static <T extends Comparable<T>> void executar(Scanner entrada, ArvoreRubroNegra<T> arvore,
            Function<String, T> conversor) {
        arvore.mostrar();
        while (true) {
            System.out.println("\n===== ÁRVORE RUBRO-NEGRA =====");
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
                        NoRN<T> no = arvore.buscar(chave, true);
                        if (no == null)
                            System.out.println("Chave " + chave + " não encontrada.");
                        else
                            System.out.println("Encontrada: " + no + (no.isRubro() ? " (rubro)" : " (negro)"));
                    }
                    break;
                case "4":
                    System.out.println("Nós: " + arvore.size() + " | Altura: " + arvore.altura()
                            + " | Altura negra: " + arvore.alturaNegra());
                    arvore.mostrar();
                    break;
                case "5":
                    String erro = arvore.validar();
                    System.out.println(
                            erro == null ? "Todas as propriedades da Árvore Rubro-Negra estão válidas." : "ERRO: " + erro);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
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
