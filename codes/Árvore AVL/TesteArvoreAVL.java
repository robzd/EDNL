import java.util.Scanner;

public class TesteArvoreAVL {

    public static void main(String[] args) {
        ArvoreAVL arvore = new ArvoreAVL();
        Scanner entrada = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ÁRVORE AVL =====");
            System.out.println("1 - Inserir");
            System.out.println("2 - Remover");
            System.out.println("3 - Buscar");
            System.out.println("4 - Mostrar");
            System.out.println("5 - Carregar exemplo da atividade (10 5 15 2 8 22)");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            String opcao = entrada.nextLine().trim();
            switch (opcao) {
                case "1":
                    for (int chave : lerChaves(entrada, "Chave(s) para inserir (separadas por espaço): ")) {
                        System.out.println("\nInserir " + chave);
                        if (arvore.insert(chave, "v" + chave) == null)
                            System.out.println("Chave " + chave + " já existe.");
                        arvore.mostrar();
                    }
                    break;
                case "2":
                    for (int chave : lerChaves(entrada, "Chave(s) para remover (separadas por espaço): ")) {
                        System.out.println("\nRemover " + chave);
                        if (!arvore.remove(chave))
                            System.out.println("Chave " + chave + " não encontrada.");
                        arvore.mostrar();
                    }
                    break;
                case "3":
                    for (int chave : lerChaves(entrada, "Chave para buscar: ")) {
                        No no = arvore.search(chave);
                        if (no == null)
                            System.out.println("Chave " + chave + " não encontrada.");
                        else
                            System.out.println("Encontrado: chave=" + no.getChave() + ", elemento=" + no.getElemento()
                                    + ", FB=" + no.getFb());
                    }
                    break;
                case "4":
                    System.out.println("Tamanho: " + arvore.size());
                    arvore.mostrar();
                    break;
                case "5":
                    arvore = new ArvoreAVL();
                    for (int chave : new int[] { 10, 5, 15, 2, 8, 22 })
                        arvore.insert(chave, "v" + chave);
                    arvore.mostrar();
                    break;
                case "0":
                    entrada.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static int[] lerChaves(Scanner entrada, String mensagem) {
        System.out.print(mensagem);
        String[] partes = entrada.nextLine().trim().split("\\s+");
        try {
            int[] chaves = new int[partes.length];
            for (int i = 0; i < partes.length; i++)
                chaves[i] = Integer.parseInt(partes[i]);
            return chaves;
        } catch (NumberFormatException e) {
            System.out.println("Digite apenas números inteiros.");
            return new int[0];
        }
    }
}
