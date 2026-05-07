#ifndef C_ALGFRUCHTERMAN_H
#define C_ALGFRUCHTERMAN_H

#include "math.h"
#include "vectorInt.h"
#include "fileManager.h"
#include "planarCheck.h"

#include "stdio.h"
#include "stdbool.h"

typedef struct Vec2 {
    double x;
    double y;
} Vec2;

typedef struct Vertex {
    Vec2 position;
    Vec2 displacement;
} Vertex;

typedef struct FruchtermanSettings {
    int Width;
    int Height;
    float C;
    float T;
} FruchtermanSettings;

void CheckSettings(FruchtermanSettings* settings);

bool UseFruchterman(UserSettings* userSettings);

void PhysicsSolver(Vertex* allVert, int numOfVertices, float T, float idealDistance, vInt* Graph, int Width, int Height);

void WritePositions(Vertex* allVert, UserSettings* userSettings, int numOfVertices);

Vec2 SetRandomPosition(int Width, int Height);

double AttractiveForce(double distance, double idealDistance);
double RepulsiveForce(double distance, double idealDistance);

#endif //C_ALGFRUCHTERMAN_H