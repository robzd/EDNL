public class ArvoreAVL extends ArvorePesquisaBinaria {

    // Inserção: ArvEsq +1 / ArvDir -1. Sobe pelos antecessores e para quando FB ==
    // 0.
    @Override
    protected void aposInsercao(No novo) {
        No filho = novo;
        No pai = novo.getPai();
        while (pai != null) {
            pai.setFb(pai.getFb() + ((filho == pai.getEsquerdo()) ? 1 : -1));
            if (pai.getFb() == 0)
                return;
            if (Math.abs(pai.getFb()) == 2) {
                balancear(pai); // depois da rotação a subárvore volta à altura anterior
                return;
            }
            filho = pai;
            pai = pai.getPai();
        }
    }

    // Remoção: ArvEsq -1 / ArvDir +1. Sobe pelos antecessores e para quando FB !=
    // 0.
    @Override
    protected void aposRemocao(No pai, boolean eraEsquerdo) {
        while (pai != null) {
            pai.setFb(pai.getFb() + (eraEsquerdo ? -1 : 1));
            No subarvore = (Math.abs(pai.getFb()) == 2) ? balancear(pai) : pai;
            if (subarvore.getFb() != 0)
                return;
            pai = subarvore.getPai();
            eraEsquerdo = pai != null && subarvore == pai.getEsquerdo();
        }
    }

    private No balancear(No no) {
        if (no.getFb() == 2) {
            if (no.getEsquerdo().getFb() >= 0) {
                System.out.println("Rotação simples à direita em " + no.getChave());
            } else {
                System.out.println("Rotação dupla à direita em " + no.getChave());
                rotacaoEsquerda(no.getEsquerdo());
            }
            return rotacaoDireita(no);
        }

        if (no.getDireito().getFb() <= 0) {
            System.out.println("Rotação simples à esquerda em " + no.getChave());
        } else {
            System.out.println("Rotação dupla à esquerda em " + no.getChave());
            rotacaoDireita(no.getDireito());
        }
        return rotacaoEsquerda(no);
    }

    // B desce, A (filho direito de B) sobe
    private No rotacaoEsquerda(No b) {
        No a = b.getDireito();
        b.setDireito(a.getEsquerdo());
        if (a.getEsquerdo() != null)
            a.getEsquerdo().setPai(b);
        trocarNos(b, a);
        a.setEsquerdo(b);
        b.setPai(a);

        b.setFb(b.getFb() + 1 - Math.min(a.getFb(), 0));
        a.setFb(a.getFb() + 1 + Math.max(b.getFb(), 0));
        return a;
    }

    // B desce, A (filho esquerdo de B) sobe
    private No rotacaoDireita(No b) {
        No a = b.getEsquerdo();
        b.setEsquerdo(a.getDireito());
        if (a.getDireito() != null)
            a.getDireito().setPai(b);
        trocarNos(b, a);
        a.setDireito(b);
        b.setPai(a);

        b.setFb(b.getFb() - 1 - Math.max(a.getFb(), 0));
        a.setFb(a.getFb() - 1 + Math.min(b.getFb(), 0));
        return a;
    }

    @Override
    protected String rotulo(No no) {
        return no.getChave() + "[" + no.getFb() + "]";
    }
}
