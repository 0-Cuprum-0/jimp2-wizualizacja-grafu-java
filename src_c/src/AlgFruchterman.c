#include "AlgFruchterman.h"

#include <stdlib.h>

Vec2 SetRandomPosition(int Width, int Height) {
    Vec2 Position;
    Position.x = rand() % (Width / 2);
    Position.y = rand() % (Height / 2);

    return Position;
}

void CheckSettings(FruchtermanSettings* settings) {
    FILE* fSet = fopen("assets/fruchterman.txt", "r");
    char line[256];

    settings->Width = 100;
    settings->Height = 100;
    settings->C = 0.25f;
    settings->T = 100.0f;

    if (fSet == NULL) {
        return;
    }

    if (fgets(line, sizeof(line), fSet) == NULL) {
        fclose(fSet);
        return;
    }

    int w;
    int h;
    float c;
    float t;

    if (sscanf(line, "%d %d %f %f", &w, &h, &c, &t) != 4) {
        fclose(fSet);
        return;
    }

    settings->Width = w;
    settings->Height = h;
    settings->C = c;
    settings->T = t;

    fclose(fSet);
}

double AttractiveForce(double distance, double idealDistance) {
    return (distance * distance) / idealDistance;
}

double RepulsiveForce(double distance, double idealDistance) {
    return -(idealDistance * idealDistance) / distance;
}

float maxF(float a, float b) {
    return a > b ? a : b;
}

float minF(float a, float b) {
    return a < b ? a : b;
}

void PhysicsSolver(Vertex* allVert, int numOfVertices, float T, float idealDistance, vInt* Graph, int Width, int Height) {
     while (T > 0) {

        for (int i = 0; i < numOfVertices; i++) {
            //repulsive
            allVert[i].displacement = (Vec2){0.0f, 0.0f};
            for (int j = 0; j < numOfVertices; j++) {
                if (i == j) continue;

                Vec2 delta = (Vec2){allVert[j].position.x - allVert[i].position.x, allVert[j].position.y - allVert[i].position.y};
                float dist = maxF(sqrt(delta.x * delta.x + delta.y * delta.y), 0.01f);

                allVert[i].displacement.x += (delta.x / dist) * RepulsiveForce(dist, idealDistance);
                allVert[i].displacement.y += (delta.y / dist) * RepulsiveForce(dist, idealDistance);
            }
        }

        for (int i = 0; i < numOfVertices; i++) {
            //attractive
            for (int j = 0; j < Graph[i].size; j++) {
                int n = Graph[i].data[j];

                Vec2 delta = (Vec2){allVert[n].position.x - allVert[i].position.x, allVert[n].position.y - allVert[i].position.y};
                float dist = maxF(sqrt(delta.x * delta.x + delta.y * delta.y), 0.01f);

                allVert[i].displacement.x += (delta.x / dist) * AttractiveForce(dist, idealDistance);
                allVert[i].displacement.y += (delta.y / dist) * AttractiveForce(dist, idealDistance);
            }
        }

        for (int i = 0; i < numOfVertices; i++) {
            float distance = maxF(sqrt(allVert[i].displacement.x * allVert[i].displacement.x + allVert[i].displacement.y * allVert[i].displacement.y), 0.01f);

            allVert[i].position.x += (allVert[i].displacement.x / distance) * minF(distance, T);
            allVert[i].position.y += (allVert[i].displacement.y / distance) * minF(distance, T);

            if (allVert[i].position.x > Width) allVert[i].position.x = Width;
            if (allVert[i].position.x < 5) allVert[i].position.x = 5;
            if (allVert[i].position.y > Height) allVert[i].position.y = Height;
            if (allVert[i].position.y < 5) allVert[i].position.y = 5;
        }

        T = T * 0.95f - 0.5f;
    }
}

bool UseFruchterman(UserSettings* userSettings) {
    int numOfEdges = 0;
    int numOfVertices = 0;
    vInt* Graph = FruchtermanReadInputFile(userSettings->fIn, &numOfVertices, &numOfEdges);

    if (!MightBePlanar(numOfVertices, numOfEdges)) {
        NotifyError("Wykryto ze graf nie jest planarny");

        for (int i = 0; i < numOfVertices; i++) {
            free(Graph[i].data);
        }

        free(Graph);
        fclose(userSettings->fOut);
        fclose(userSettings->fIn);

        return 1;
    }

    FruchtermanSettings algSettings;

    CheckSettings(&algSettings);

    const int area = algSettings.Width * algSettings.Height;
    const float idealDistance = algSettings.C * sqrt((float)area / numOfVertices);

    Vertex* allVert = malloc(numOfVertices * sizeof(Vertex));
    for (int i = 0; i < numOfVertices; i++) {
        allVert[i].displacement = (Vec2){0.0f, 0.0f};
        allVert[i].position = SetRandomPosition(algSettings.Width, algSettings.Height);
    }

    PhysicsSolver(allVert, numOfVertices, algSettings.T, idealDistance, Graph, algSettings.Width, algSettings.Height);

    WritePositions(allVert, userSettings, numOfVertices);

    for (int i = 0; i < numOfVertices; i++) {
        free(Graph[i].data);
    }
    free(Graph);
    free(allVert);

    return 0;
}

void WritePositions(Vertex* allVert, UserSettings* userSettings, int numOfVertices) {
    switch (userSettings->chosenOutputFileType) {
        case TXT:
            for (int i = 0; i < numOfVertices; i++) {
                fprintf(userSettings->fOut, "%d %lf %lf\n", i, allVert[i].position.x, allVert[i].position.y);
            }
            break;
        case BIN:
            for (int i = 0; i < numOfVertices; i++) {
                fwrite(&allVert[i].position, sizeof(allVert[i].position), 1, userSettings->fOut);
            }
            break;
    }
}
