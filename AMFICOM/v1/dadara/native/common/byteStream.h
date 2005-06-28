#ifndef _byteStream_h
#define _byteStream_h

class byteOut {
private:
	unsigned char *data;
	int pos;
	int allocated;
	void ensure(int inc);
public:
	byteOut();
	~byteOut();
	void writeChar(int v);
	void writeShort(int v);
	void write3B(long v);
	void writeLong(long v);
	int getSize();
	char *getData();
};

class byteIn {
private:
	unsigned char *data;
	int length;
	int pos;
	int autoDeleteBuffer;
	void ensure(int inc);
public:
	byteIn(char *data, int length, int autoDeleteBuffer);
	~byteIn();
	int left();
	char readChar();
	int readShort();
	long read3B();
	long readLong();
	void unwindChar();
	void unwindShort();
	void unwind3B();
	void unwindLong();
};

#endif
