#ifndef _pack_h
#define _pack_h

#include "byteStream.h"

// преобразование signed long в long-запись, у которого знак перенесен в младший бит (для облегчения упаковки)
long pk_ls2u(long v);
long pk_lu2s(long x);

// кодируем положительные long - значения меньше 255 как байт, значения >=255 - как 0xff и long
void pk_writeLongPlusInc(long delta, byteOut &bout);
long pk_readLongPlusInc(byteIn &bin);

// кодировка небольших signed long чисел: в 1..5 байт
void pk_writeLongM3(long delta, byteOut &bout);
long pk_readLongM3(byteIn &bin);

#endif
