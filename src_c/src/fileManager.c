#include "fileManager.h"

#include <stdlib.h>
#include <string.h>

static int debug = 0;

int FlagSetup(int argc, char* argv[], UserSettings* userSettings) {
    if (argc < 5) {
        NotifyError("Podano zbyt mala liczbe argumentow.");
        return 1;
    }
    for (int i = 1; i < argc;i++) {
        if (!strcmp(argv[i], "-i")) {
            userSettings->fIn = fopen(argv[++i], "r");
            if (userSettings->fIn == NULL) {
                NotifyError("Nie udało sie otworzyc pliku wejściowego.");
                return 1;
            }
        } 
        else if (!strcmp(argv[i], "-d")) {
            debug = 1;
        }
        else if (!strcmp(argv[i], "-a")) {
            const char* alg_arg = argv[++i];
            if (!strcmp(alg_arg, "FRE")) {
                userSettings->chosenAlgorithm = FRE;
            } else if (!strcmp(alg_arg, "TRI")) {
                userSettings->chosenAlgorithm = TRI;
            } else {
                NotifyError("Nie podano lub blednie zapisano algorytm.");
                return 1;
            }
        } else if (!strcmp(argv[i], "-t")) {
            if (!strcmp(argv[++i], "BIN")) {
                userSettings->chosenOutputFileType = BIN;
            } else if(!strcmp(argv[i], "TXT")){
                userSettings->chosenOutputFileType = TXT;
            } else {
                userSettings->chosenOutputFileType = FileNotSpecified;
            }
        } else if (!strcmp(argv[i], "-o")) {
            switch (userSettings->chosenOutputFileType) {
                case FileNotSpecified:
                    NotifyError("Upewnij sie, ze flata -t jest w poprawnym miejscu (przed -o) i zawiera poprawny typ");
                    return 1;
                case TXT:
                    userSettings->fOut = fopen(argv[++i], "w");
                    break;
                case BIN:
                    userSettings->fOut = fopen(argv[++i], "wb");
                    break;
            }
            if (userSettings->fOut == NULL) {
                NotifyError("Nie udalo sie otworzyc pliku wyjsciowego.");
                return 1;
            }
        }
    }

    Notify("Wykryto plik wejsciowy.");

    if (userSettings->chosenAlgorithm == TRI) Notify("Wykorzystywanie algorytmu Triangulacji.");
    else Notify("Wykorzystywanie algorytmu Fruchtermana - wykorzystanie fizyki.");

    if (userSettings->chosenOutputFileType == TXT) Notify("Plik wyjsciowy w formacie .txt");
    else Notify("Plik wyjsciowy w wformacie binarnym");

    if (userSettings->fOut == NULL) {
        userSettings->fOut = fopen("assets/out.txt", "w");
        if (userSettings->fOut == NULL) {
            NotifyError("Nie udalo sie poprawnie otworzyc domyslnej sciezki wyjscia");
            return 1;
        }
        Notify("Ustawiono domyslna sciezke wyjscia: out.txt");
    } else Notify("Wykryto sciezke wyjscia");

    return 0;
}

vInt* FruchtermanReadInputFile(FILE* fIn, int* numOfVertices, int* numOfEdges) {
    //Struktura pliku wejsciowego: <nazwa_krawędzi> <wierzchołek_A> <wierzchołek_B> <waga_krawędzi> \n
    //Nazwa i waga krawedzi nas nie obchodzi
    *numOfVertices = 0;
    *numOfEdges = 0;
    vInt* edges = NULL;

    char name[100];
    int which, toWhich;
    double weight;

    while (fscanf(fIn, "%s %d %d %lf", name, &which, &toWhich, &weight) == 4) {
        int max_id = (which > toWhich) ? which : toWhich;
        int required_size = max_id + 1;

        if (required_size > *numOfVertices) {
            int prev_size = *numOfVertices;
            *numOfVertices = required_size;

            edges = realloc(edges, *numOfVertices * sizeof(vInt));
            for (int i = prev_size; i < *numOfVertices; i++) {
                vIntInit(&edges[i]);
            }
        }

        vIntAdd(&edges[which], toWhich);
        vIntAdd(&edges[toWhich], which);

        *numOfEdges += 1;
    }

    return edges;
}

void PrintGraph(vInt* Graph, int numOfVertices, FILE* fOut) {
    for (int i = 0; i < numOfVertices; i++) {
        fprintf(fOut,"%d: ", i);

        for (int j = 0; j < Graph[i].size; j++) {
            fprintf(fOut,"%d, ", Graph[i].data[j]);
        }
        fprintf(fOut,"\n");
    }

    printf("Wypisano graf\n");
}

int** vIntTranslate(vInt* vIntGraph, int numOfVertices) {
    int** Graph = malloc(numOfVertices * sizeof(int*));

    for (int i = 0; i < numOfVertices; i++) {
        Graph[i] = vIntGraph[i].data;
    }

    return Graph;
}

void NotifyError(const char* msg) {
    if (msg == NULL) return;

    printf("Wykryto problem: %s\n", msg);
}

void Notify(const char* msg) {
    if (msg == NULL) return;
    if(debug == 0) return;
    printf("%s\n", msg);
}
