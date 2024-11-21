import java.time.LocalDateTime;
import java.util.*;

public class Dijkstra {
    private List<Aresta> arestas;
    private List<No> nos;

    public Dijkstra(List<No> nos, List<Aresta> arestas) {
        this.nos = nos;
        this.arestas = arestas;
    }

    public List<Aresta> dijkstra(No origem, No destino) {
        int V = nos.size();
        int[] distances = new int[V];
        boolean[] visited = new boolean[V];
        int[] parent = new int[V];

        // Mapeando os índices dos nós
        int origemIndex = nos.indexOf(origem);
        int destinoIndex = nos.indexOf(destino);

        // Inicializa as distâncias
        for (int i = 0; i < V; i++) {
            distances[i] = Integer.MAX_VALUE;
            visited[i] = false;
            parent[i] = -1;
        }

        distances[origemIndex] = 0;
        LocalDateTime startTime = LocalDateTime.now(); 

        // Algoritmo de Dijkstra
        for (int count = 0; count < V - 1; count++) {
            int u = minimumDistance(distances, visited);
            visited[u] = true;    

            for (Aresta aresta : arestas) {
                int v = -1;
                if (aresta.getNo1().equals(nos.get(u))) {
                    v = nos.indexOf(aresta.getNo2());
                } else if (aresta.getNo2().equals(nos.get(u))) {
                    v = nos.indexOf(aresta.getNo1());
                }

                if (v != -1 && !visited[v] && distances[u] != Integer.MAX_VALUE) {
                    int newDist = distances[u] + (int) aresta.getPeso();
                    if (newDist < distances[v]) {
                        distances[v] = newDist;
                        parent[v] = u;
                    }
                }
            }
            
        }
        LocalDateTime endTime = LocalDateTime.now();
        long duration = java.time.Duration.between(startTime, endTime).toMillis();
        System.out.println("Tempo de execução Algoritmo de Dijkstra: " + duration + " milissegundos");

        // Agora, vamos recuperar o caminho mínimo em forma de arestas
        List<Aresta> caminho = new ArrayList<>();
        int crawl = destinoIndex;
        while (parent[crawl] != -1) {
            int parentIndex = parent[crawl];
            No no1 = nos.get(crawl);
            No no2 = nos.get(parentIndex);
            // Encontrando a aresta entre os dois nós no caminho
            for (Aresta aresta : arestas) {
                if ((aresta.getNo1().equals(no1) && aresta.getNo2().equals(no2)) ||
                    (aresta.getNo1().equals(no2) && aresta.getNo2().equals(no1))) {
                    caminho.add(aresta);
                    break;
                }
            }
            crawl = parent[crawl];
        }

        // Como o caminho foi percorrido de trás para frente, invertemos
        Collections.reverse(caminho);
        return caminho;
    }

    private int minimumDistance(int[] distances, boolean[] visited) {
        int minDistance = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int v = 0; v < distances.length; v++) {
            if (!visited[v] && distances[v] <= minDistance) {
                minDistance = distances[v];
                minIndex = v;
            }
        }
        return minIndex;
    }
}

