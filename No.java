public class No {
    private String nome;
    private int x;
    private int y;

    
    public No(String nome, int x, int y) {
        this.nome = nome;
        this.x = x;
        this.y = y;
    }

    public String getNome() {
        return nome;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    @Override
    public String toString() {
        return nome;  // Garante que o nome será mostrado na JComboBox
    }
}