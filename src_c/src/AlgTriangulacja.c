#include "AlgTriangulacja.h"



#define MAX_V 1000
#define ITERATIONS MAX_V * 10


//zmienne prywatne
static int adj[MAX_V][MAX_V];
static int deg[MAX_V];
static double X[MAX_V];
static double Y[MAX_V];
static int is_boundary[MAX_V];
static int V = 0;

//funkcje prywatne

// Czyszczenie pamięci (konieczne, gdy funkcja jest wywoływana wielokrotnie)
static void reset_state() {
    memset(adj, 0, sizeof(adj));
    memset(deg, 0, sizeof(deg));
    memset(X, 0, sizeof(X));
    memset(Y, 0, sizeof(Y));
    memset(is_boundary, 0, sizeof(is_boundary));
    V = 0;
}


//ladowanie musi byc oddzielnie
static int load_graph(FILE* input_file) {
    if (!input_file) {
        Notify("Blad: Nie mozna otworzyc pliku wejsciowego\n");
        return -1;
    }

    char line[256];
    char edge_name[100];
    int u, v;
    double weight;
    int max_id = -1;

    while (fscanf(input_file, "%s %d %d %lf", edge_name, &u, &v, &weight) == 4) {
        if (adj[u][v] == 0) {
            adj[u][v] = 1;
            adj[v][u] = 1;
            deg[u]++;
            deg[v]++;
        }
        if (u > max_id) max_id = u;
        if (v > max_id) max_id = v;
    }
    V = max_id + 1;
    char output[40];
    sprintf(output, "Wczytano graf: %d wierzcholkow.\n", V);
    Notify(output);
    return 0;
}

static void triangulate() {
    int added_edges = 0;
    for (int u = 0; u < V; u++) {
        for (int v = 0; v < V; v++) {
            if (!adj[u][v]) continue;
            for (int w = 0; w < V; w++) {
                if (v == w || u == w || !adj[v][w]) continue;
                for (int x = 0; x < V; x++) {
                    if (x == u || x == v || x == w || !adj[w][x]) continue;
                    
                    if (adj[x][u] && adj[u][w] == 0 && adj[v][x] == 0) {
                        adj[u][w] = 1;
                        adj[w][u] = 1;
                        deg[u]++;
                        deg[w]++;
                        added_edges++;
                    }
                }
            }
        }
    }
    char output[60];
    sprintf(output, "Ztriangulowano. Dodano %d tymczasowych krawedzi.\n", added_edges);
    Notify(output);
}

static void calculate_tutte() {
    if (V < 3) return;
    
    // Obwódka na sztywno
    is_boundary[0] = 1; X[0] = 0.0;   Y[0] = 0.0;
    is_boundary[1] = 1; X[1] = 100.0; Y[1] = 0.0;
    is_boundary[2] = 1; X[2] = 50.0;  Y[2] = 86.6025; 
    
    for (int i = 0; i < V; i++) {
        if (!is_boundary[i]) {
            X[i] = 50.0;
            Y[i] = 28.8; 
        }
    }

    for (int iter = 0; iter < ITERATIONS; iter++) {
        double max_movement = 0.0;

        for (int i = 0; i < V; i++) {
            if (is_boundary[i]) continue;

            double sum_x = 0.0, sum_y = 0.0;
            for (int j = 0; j < V; j++) {
                if (adj[i][j]) {
                    sum_x += X[j];
                    sum_y += Y[j];
                }
            }

            if (deg[i] > 0) {
                double new_x = sum_x / deg[i];
                double new_y = sum_y / deg[i];
                
                double dist = sqrt((new_x - X[i])*(new_x - X[i]) + (new_y - Y[i])*(new_y - Y[i]));
                if (dist > max_movement) max_movement = dist;

                X[i] = new_x;
                Y[i] = new_y;
            }
        }
        if (max_movement < 0.001) {
            char output[60];
            sprintf(output,"Uklad ustabilizowal sie po %d iteracjach.\n", iter);
            Notify(output);
            break;
        }
    }
}

static int save_coordinates(UserSettings* settings) {
    FILE *file = settings->fOut;
    if(settings->chosenOutputFileType == TXT)
    {
        for (int i = 0; i < V; i++) {
            fprintf(file, "%d %.4f %.4f\n", i, X[i], Y[i]);
        }
    }
    if(settings->chosenOutputFileType == BIN)
    {
        for (int i = 0; i < V; i++) {
            fwrite(&i, sizeof(int), 1, file);
            fwrite(&X[i], sizeof(double), 1, file);
            fwrite(&Y[i], sizeof(double), 1, file);
        }
    }
    Notify("Zapisano wspolrzedne do pliku\n");
    return 0;
}

//funkcja publiczna
int triangulation_full(UserSettings* settings) {
    reset_state(); // Czyszczenie pamięci przed pracą
    
    if (load_graph(settings->fIn) != 0) {
        return -1;
    }
    
    triangulate();
    calculate_tutte();
    
    if (save_coordinates(settings) != 0) {
        return -1;
    }
    
    return 0; // Sukces
}