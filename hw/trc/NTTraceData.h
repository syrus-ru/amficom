#ifndef NT_TRACE_DATA_H
#define NT_TRACE_DATA_H

typedef struct {
	short _000;
	short _002;
	short _004;
	int _006;
	int _010;
	short _014;
	short _016;
	short _018;
	int _020;
	int _024;
	short _028;
	int _030;
	int _034;
	short _038;
	short _040;
	short _042;
	int _044;
	char _048[62];
} NTTraceData_Sub;	//size 110

typedef struct {
	short _00000;			//size 2, short
	int _00002;			//size 4, int		//date
	short _00006;			//size 2, short
	short _00008;			//size 2, short
	short _00010;			//size 2, short
	unsigned short _00012;		//size 2, unsigned short, var_12754
	short _00014;			//size 2, short		//wavelength
	short _00016;			//size 2, short
	short _00018;			//size 2, short
	unsigned short _00020[12];	//size 24, unsigned short[12]
	int _00044;			//size 4, int
	short _00048;			//size 2, short, var_12730
	short _00050;			//size 2, short
	short _00052;			//size 2, short
	short _00054;			//size 2, short
	short _00056[12];		//size 24, short[12]
	int _00080;			//size 4, int
	char _00084[10];		//size 10, char[10]	//Optic module (OB5, ...)
	short _00094;			//size 2, short
	short _00096;			//size 2, short
	short _00098;			//size 2, short
	short _00100;			//size 2, short
	short _00102;			//size 2, short
	int _00104;			//size 4, int
	int _00108;			//size 4, int
	char _00112;			//size 1, char
	char _00113;			//size 1, char
	short _00114[3];		//size 6, short[3]
	short _00120[3];		//size 6, short[3]
	short _00126;			//size 2, short
	int _00128;			//size 4, int
	int _00132;			//size 4, int
	int _00136[2];			//size 8, int[2]
	int _00144[2];			//size 8, int[2]
	int _00152[2][3];		//size 24, int[2][3]
	int _00176[2][3];		//size 24, int[2][3]
	short _00200;			//size 2, short
	short _00202;			//size 2, short
	short _00204;			//size 2, short
	short _00206;			//size 2, short
	short _00208;			//size 2, short
	int _00210;			//size 4, int
	int _00214;			//size 4, int
	unsigned short _00218;		//size 2, unsigned short
	char _00220[4];			//size 4, char[4]
	short _00224;			//size 2, short
	char _00226[8];			//size 8, char[8]
	short _00234[9];		//size 18, short[9], ?
	short _00252;			//size 2, short
	NTTraceData_Sub _00254[64];	//size 7040, NTTraceData_Sub[64]
	unsigned short _07294;		//size 2, unsigned short (offset in _07296)
	short _07296[16384];		//size 32768, short[16384] (points)
	short _40064;			//size 2, short
	unsigned short _40066;			//size 2, short, var_8ADE (number_of_points)
	short* _40068;			//size 4, short*, var_8ADC (short array of points)
	float _40072;			//size 4, float, var_8AD8
	short _40076;			//size 2, short
	float _40078;			//size 4, float, float - ?
	int _40082;			//size 4, int, int - ?
//	char _40086[28];	//...?
	short _40114;			//size 2, short
//	char _40116[2];		//...?
	void* _40118;			//size 4, void*, void* - ?
	unsigned short _40122;		//size 2, short
	char* _40124;			//size 4, char*, char* - ?
//	char _40128[40];	//...?
	short _40168;			//size2, short
} NTTraceData;	//size 40170

#endif
