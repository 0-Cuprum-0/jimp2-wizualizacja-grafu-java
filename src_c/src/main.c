#include <stdio.h>
#include <stdlib.h>

#include "fileManager.h"
#include "vectorInt.h"
#include "planarCheck.h"
#include "AlgFruchterman.h"
#include "AlgTriangulacja.h"

int main(int argc, char* argv[]) {
    UserSettings userSettings;
    

    userSettings.chosenAlgorithm = AlgNotSpecified;
    userSettings.chosenOutputFileType = FileNotSpecified;

    if (FlagSetup(argc, argv, &userSettings)) return 1;

    switch (userSettings.chosenAlgorithm) {
        case AlgNotSpecified:
            NotifyError("Wystapil problem z ustawianiem typu algorytmu");
            break;
        case FRE:
            if (UseFruchterman(&userSettings)) {
                if (userSettings.fOut != NULL) fclose(userSettings.fOut);
                if (userSettings.fIn != NULL) fclose(userSettings.fIn);

                return 1;
            }

            Notify("Pomyslnie zakonczono dzialanie algorytmu Fruchtermana");
            break;
        case TRI:
            triangulation_full(&userSettings);
            break;
        default:
            NotifyError("Wystapil nieoczekiwany problem");
            break;
    }

    if (userSettings.fOut != NULL) fclose(userSettings.fOut);
    if (userSettings.fIn != NULL) fclose(userSettings.fIn);

    return 0;
}