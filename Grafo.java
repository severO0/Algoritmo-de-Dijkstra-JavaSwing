import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Grafo extends JPanel {

    private List<No> nos;
    private List<Aresta> arestas;
    private List<Aresta> caminhoMinimo;
    private Image imagemFundo;
    private JComboBox<No> comboBoxOrigem;
    private JComboBox<No> comboBoxDestino;

    public Grafo() {
        nos = new ArrayList<>();
        arestas = new ArrayList<>();
        caminhoMinimo = new ArrayList<>();
        imagemFundo = new ImageIcon("mapa-do-brasil.jpg").getImage();

        // MouseListener para capturar os cliques e adicionar pontos
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Pega as coordenadas do clique
                int x = e.getX();
                int y = e.getY();

                // Pede ao usuário o nome do ponto
                String nome = JOptionPane.showInputDialog("Digite o nome do ponto:");

                if (nome != null && !nome.trim().isEmpty()) {
                    // Cria o nó com o nome e a posição
                    No novoPonto = new No(nome, x, y);
                    nos.add(novoPonto);

                    // Pede o peso das arestas que conectam esse ponto aos outros
                    for (No pontoExistente : nos) {
                        if (pontoExistente != novoPonto) {
                            String pesoStr = JOptionPane.showInputDialog(
                                "Digite o peso da aresta entre " + nome + " e " + pontoExistente.getNome() + ":");
                            try {
                                int peso = Integer.parseInt(pesoStr);
                                Aresta novaAresta = new Aresta(novoPonto, pontoExistente, peso);
                                arestas.add(novaAresta);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Valor de peso inválido. Aresta não adicionada.");
                            }
                        }
                    }

                    atualizarComboBoxes();  // Atualiza as ComboBoxes
                    repaint();  // Re-renderiza a tela
                }
            }
        });

        // Configuração do layout
        setLayout(new BorderLayout());

        // Criação dos JComboBox
        comboBoxOrigem = new JComboBox<>();
        comboBoxDestino = new JComboBox<>();

        // Atualiza as ComboBoxes
        atualizarComboBoxes();

        // Criação do botão
        JButton button = new JButton("Buscar Menor Peso");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                No origem = (No) comboBoxOrigem.getSelectedItem();
                No destino = (No) comboBoxDestino.getSelectedItem();
                encontrarMenorPeso(origem, destino);
                mostrarResultado();  // Exibe o resultado do caminho mínimo
                repaint(); // Atualiza a visualização
            }
        });

        // Painel para os JComboBox e botão
        JPanel panel = new JPanel();
        panel.add(new JLabel("Origem:"));
        panel.add(comboBoxOrigem);
        panel.add(new JLabel("Destino:"));
        panel.add(comboBoxDestino);
        panel.add(button);

        // Adiciona o painel ao topo
        add(panel, BorderLayout.NORTH);
    }

   

    // Método para encontrar o menor caminho entre origem e destino usando Dijkstra
    private void encontrarMenorPeso(No origem, No destino) {
        caminhoMinimo.clear();  // Limpa o caminho mínimo anterior

        // Instancia o algoritmo de Dijkstra e encontra o caminho
        Dijkstra dijkstra = new Dijkstra(nos, arestas);
        caminhoMinimo = dijkstra.dijkstra(origem, destino);  // Agora retorna a lista de arestas
    }

    // Método que atualiza as ComboBoxes de origem e destino
    private void atualizarComboBoxes() {
        comboBoxOrigem.removeAllItems();
        comboBoxDestino.removeAllItems();

        for (No no : nos) {
            comboBoxOrigem.addItem(no);
            comboBoxDestino.addItem(no);
        }
    }

    // Método para exibir o resultado do caminho mínimo em uma nova tela
    private void mostrarResultado() {
        if (caminhoMinimo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum caminho encontrado.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Construa a sequência de estados e o valor total
        StringBuilder resultado = new StringBuilder("Caminho Mínimo:\n");
        int totalPeso = 0;
        No ultimoNo = null;

        // Mostrar a sequência de estados e pesos das arestas
        for (Aresta aresta : caminhoMinimo) {
            if (ultimoNo == null || aresta.getNo1().equals(ultimoNo)) {
                resultado.append(aresta.getNo1().getNome())
                        .append(" -> ")
                        .append(aresta.getPeso())
                        .append(" -> ");
                totalPeso += aresta.getPeso();
                ultimoNo = aresta.getNo2();
            } else {
                resultado.append(aresta.getNo2().getNome())
                        .append(" -> ")
                        .append(aresta.getPeso())
                        .append(" -> ");
                totalPeso += aresta.getPeso();
                ultimoNo = aresta.getNo1();
            }
        }

        // Remover a última seta "->" e adicionar o destino
        if (ultimoNo != null) {
            resultado.append(ultimoNo.getNome()).append("\n");
        }

        resultado.append("Peso Total: ").append(totalPeso);

        // Exibe o resultado em um JOptionPane
        JOptionPane.showMessageDialog(this, resultado.toString(), "Resultado do Caminho Mínimo", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Desenha a imagem de fundo
        g2d.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);

        // Defina a nova fonte com o tamanho desejado
        Font fonte = new Font("Arial", Font.BOLD, 20);
        // Altere o tamanho para o desejado
        g2d.setFont(fonte);

        // Desenha as arestas
        for (Aresta aresta : arestas) {
            No no1 = aresta.getNo1();
            No no2 = aresta.getNo2();
            
            if (caminhoMinimo.contains(aresta)) {
                g2d.setColor(Color.blue);  // Destaca o caminho mínimo
            } else {
                g2d.setColor(Color.BLACK);
            }
            g2d.drawLine(no1.getX(), no1.getY(), no2.getX(), no2.getY());
            float posX = (no2.getX() + no1.getX()) / 2;
            float posY = (no2.getY() + no1.getY()) / 2;
            g2d.drawString(String.format("%d", aresta.getPeso()), posX - 15, posY - 25);
        }

        // Desenha os nós
        for (No no : nos) {
            g2d.setColor(Color.RED);
            g2d.fillOval(no.getX() - 5, no.getY() - 5, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawString(no.getNome(), no.getX() + 10, no.getY() - 10);
        }
    }

}

