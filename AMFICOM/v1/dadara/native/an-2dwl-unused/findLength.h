#ifndef _findLength_h
#define _findLength_h

// определяет длину рефлектограммы до начала шума (т.е. до конца р/граммы)
// если ничего не может сделать, должно вернуть data_length
// data - входная р/г
// data_length - ее длина
// return: значение >=0, <=data; по возможности не ноль.
int find_reflectogram_length(double *data, int data_lenght);

#endif

