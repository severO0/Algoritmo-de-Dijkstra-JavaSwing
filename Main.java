import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Grafo grafo = new Grafo();
        frame.add(grafo);
        frame.setSize(800, 800); 
        frame.setVisible(true);
        grafo.repaint();
    }
}
