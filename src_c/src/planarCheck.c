#include "planarCheck.h"

bool MightBePlanar(int numOfVertices, int numOfEdges) {
    if (numOfVertices <= 3) return true; //Zbyt malo krawedzi zeby dalo sie byc grafem nie planarnym

    if (numOfEdges > (3 * numOfVertices) - 6) return false; //zbyt duzo krawedzi

    return true; //Nadal moze byc nie planarny, ale wymaga to glebszej analizy
}