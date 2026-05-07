#ifndef C_VECTORINT_H
#define C_VECTORINT_H

typedef struct vInt {
    int* data;
    int size;
    int capacity;
} vInt;

void vIntInit(vInt* vec);
void vIntFree(vInt* vec);
void vIntAdd(vInt* vec, int value);
void vIntResize(vInt* vec, int newCapacity);
void vIntReplace(vInt* vec, int index, int value);

#endif //C_VECTORINT_H