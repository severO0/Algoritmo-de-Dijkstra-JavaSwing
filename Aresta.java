public class Aresta {
    private No no1;
    private No no2;
    private int peso;  // Alterado para int

    public Aresta(No no1, No no2, int peso) {
        this.no1 = no1;
        this.no2 = no2;
        this.peso = peso;
    }

    public No getNo1() {
        return no1;
    }

    public No getNo2() {
        return no2;
    }

    public int getPeso() {
        return peso;
    }
}

