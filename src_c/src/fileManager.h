#ifndef C_FILEMANAGER_H
#define C_FILEMANAGER_H

#include <stdio.h>
#include <stdlib.h>

#include "vectorInt.h"

typedef enum Algorithm{
    AlgNotSpecified,
    FRE,
    TRI
} Algorithm;

typedef enum OutputFileType {
    FileNotSpecified,
    TXT,
    BIN
} OutputFileType;

typedef struct UserSettings {
    FILE* fIn;
    int liczbaKrawedzi;
    int liczbaWierzcholkow;
    FILE* fOut;
    Algorithm chosenAlgorithm;
    OutputFileType chosenOutputFileType;
    int trybDebug;
} UserSettings;


int FlagSetup(int argc, char* argv[], UserSettings* userSettings); //0 jesli jest G, 1 jesli cos nie tak

vInt* FruchtermanReadInputFile(FILE* fIn, int* numOfVertices, int* numOfEdges);
void PrintGraph(vInt* Graph, int numOfVertices, FILE* fOut);

int** vIntTranslate(vInt* vIntGraph, int numOfVertices);

void NotifyError(const char* msg);
void Notify(const char* msg);

#endif //C_FILEMANAGER_H