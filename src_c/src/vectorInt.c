#include "vectorInt.h"

#include <stdlib.h>

void vIntInit(vInt* vec) {
    vec->size = 0;
    vec->capacity = 1;
    vec->data = malloc(vec->capacity * sizeof(int));

    if (vec->data == NULL) {
        //error
    }
}

void vIntFree(vInt* vec) {
    vec->capacity = 0;
    vec->size = 0;
    free(vec->data);
}

void vIntAdd(vInt* vec, int val) {
    if (vec->size == vec->capacity) {
        vIntResize(vec, vec->capacity * 2);
    }

    vec->data[vec->size] = val;
    vec->size++;
}

void vIntResize(vInt* vec, int newCapacity) {
    vec->capacity = newCapacity;
    vec->data = realloc(vec->data, vec->capacity * sizeof(int));
}

void vIntReplace(vInt* vec, int index, int val) {
    if (index >= vec->size) {
        //nie mozna tak
    }
    vec->data[index] = val;
}
