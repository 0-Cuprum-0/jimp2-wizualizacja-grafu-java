cmake_minimum_required(VERSION 4.0)
project(C C)

set(CMAKE_C_STANDARD 17)

add_executable(C src/main.c
        src/fileManager.c
        src/fileManager.h
        src/AlgFruchterman.c
        src/AlgFruchterman.h
        src/vectorInt.c
        src/vectorInt.h
        src/planarCheck.c
        src/planarCheck.h
        src/AlgTriangulacja.c
        src/AlgTriangulacja.h)
