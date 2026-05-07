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
            else {
                char temp1[20];
                int temp2, temp3;
                double temp4;
                while(fscanf(userSettings->fIn, "%s %d %d %lf", temp1, &temp2, &temp3, &temp4) == 4){
                    userSettings->liczbaKrawedzi++;
                    //nw jak inaczej sprawdzic liczbe wierzcholkow
                    if (temp2 > userSettings->liczbaWierzcholkow) userSettings->liczbaWierzcholkow = temp2;
                    if (temp3 > userSettings->liczbaWierzcholkow) userSettings->liczbaWierzcholkow = temp3;
                }
                fclose(userSettings->fIn);
                userSettings->fIn = fopen(argv[i], "r"); //resetowanie wskaznika na poczatek pliku
            }
        } 
        else if (!strcmp(argv[i], "-d")) {
            debug = 1;
        }
        else if (!strcmp(argv[i], "-a")) {
            if (!strcmp(argv[++i], "FRE")) {
                userSettings->chosenAlgorithm = FRE;
            } else if (!strcmp(argv[i], "TRI")) {
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

    *numOfVertices = 1;
    vInt* edges = malloc(*numOfVertices * sizeof(vInt));
    for (int i = 0; i < *numOfVertices; i++) {
        vIntInit(&edges[i]);
    }

    char line[256];
    while (fgets(line, sizeof(line), fIn) != NULL) {
        int which;
        int toWhich;

        if (sscanf(line, "%*s %d %d", &which, &toWhich) != 2) {
            continue;
        }

        int max = (which > toWhich) ? which : toWhich;

        if (max > *numOfVertices) {
            int prev = *numOfVertices;
            *numOfVertices = max;

            edges = realloc(edges, *numOfVertices * sizeof(vInt));
            for (int i = prev; i < *numOfVertices; i++) {
                vIntInit(&edges[i]);
            }
        }

        vIntAdd(&edges[which - 1], toWhich - 1);
        vIntAdd(&edges[toWhich - 1], which - 1);

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
