#ifndef DCL_H
#define DCL_H

#define DCL_NO_ERROR	0L
#define DCL_ERROR_1	1L
#define DCL_ERROR_2	2L
#define DCL_ERROR_3	3L
#define DCL_ERROR_4	4L

#define WB_read_data	((Tread_data)*(U32*)(work_buff + 0x28))
#define WB_write_data	((Twrite_data)*(U32*)(work_buff + 0x2C))
#define WB_param	(void*)*(U32*)(work_buff + 0x24)
#define WB_0x14		*(U32*)(work_buff + 0x14)
#define WB_0x1C		*(U32*)(work_buff + 0x1C)
#define WB_0x20		*(U32*)(work_buff + 0x20)

typedef unsigned char U8;
typedef unsigned short U16;
typedef signed short S16;
typedef unsigned long U32;

typedef U16 (*Tread_data)(U8* buffer, U16 size, void* param);
typedef void (*Twrite_data)(U8* buffer, U16 size, void* param);

extern const U8 dcl_table[];

//sub_1002BD50
int explode(Tread_data read_data, Twrite_data write_data, register U8* work_buff, void* param);

void sub_1002C3D0(register U8* work_buff, register const U8* table, register U32 count);

void sub_1002C2D0(register U8* work_buff);

void sub_1002C290(register S16 count, register U8* buffer1, register const U8* table, register U8* buffer2);

U32 sub_1002BED0(register U8* work_buff);

U32 sub_1002C180(register U8* work_buff, U32 flag);

U32 sub_1002BFD0(register U8* work_buff);

U32 sub_1002C200(register U8* work_buff, register U32 flag);

#endif
