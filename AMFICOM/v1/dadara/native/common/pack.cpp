#include "pack.h"

// кодируем положительные long - значения меньше 255 как байт, значения >=255 - как 0xff и long
void pk_writeLongPlusInc(long delta, byteOut &bout)
{
	if (delta >= 0 && delta < 255)
		bout.writeChar(delta);
	else {
		bout.writeChar(255);
		bout.writeLong(delta);
	}

}
long pk_readLongPlusInc(byteIn &bin)
{
	unsigned int delta = bin.readChar() & 0xff;
	if (delta < 255)
		return delta;
	else
		return bin.readLong();
}

// кодируем signed long в виде long, у которого знак перенесен в младший бит (для облегчения упаковки)
long pk_ls2u(long v)
{
	if (v < 0)
		return ~(v << 1);
	else
		return v << 1;
}
long pk_lu2s(long x) {
	unsigned long v = (unsigned long)x;
	if (v & 0x1L)
		return (long)~(v >> 1);
	else
		return (long)(v >> 1);
}
void pk_writeLongM3(long delta, byteOut &bout)
{
	delta = pk_ls2u(delta);
	if      ((delta & 0xFFffFF80L) == 0)
		bout.writeChar(delta);
	else if ((delta & 0xFFffC000L) == 0)
		bout.writeShort(    0x8000 | (int)delta);
	else if ((delta & 0xFFe00000L) == 0)
		bout.write3B(     0xc00000L | delta);
	else if ((delta & 0xF0000000L) == 0)
		bout.writeLong( 0xE0000000L | delta);
	else
	{
		bout.writeChar(0xff);
		bout.writeLong(delta);
	}
}

long pk_readLongM3(byteIn &bin)
{
	int key = bin.readChar() & 0xff;
	bin.unwindChar();
	long delta = 0;
	if ((key & 0x80) == 0)
		delta = bin.readChar()       & 0x7f ;
	else if ((key & 0xC0) == 0x80)
		delta = bin.readShort()    & 0x3Fff ;
	else if ((key & 0xE0) == 0xC0)
		delta = bin.read3B()     & 0x1fFFffL;
	else if ((key & 0xF0) == 0xE0)
		delta = bin.readLong() & 0x0FffFFffL;
	else
	{
		bin.readChar(); // wind back :)
		delta = bin.readLong();
	}
	return pk_lu2s(delta);
}
