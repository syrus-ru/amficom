#include "DCL.h"

int explode(Tread_data read_data, Twrite_data write_data, register U8* work_buff, void* param) {
	*(U32*)(work_buff + 0x28) = (U32)read_data;
	*(U32*)(work_buff + 0x2C) = (U32)write_data;
	*(U32*)(work_buff + 0x24) = (U32)param;
	WB_0x1C = (U32)0x800;
	WB_0x20 = WB_read_data(work_buff + 0x2234, (U16)WB_0x1C, WB_param);
	if (WB_0x20 <= 4)
		return DCL_ERROR_3;

	*(U32*)(work_buff + 0x04) = *(work_buff + 0x2234);
	*(U32*)(work_buff + 0x0C) = *(work_buff + 0x2235);
	WB_0x14 = *(work_buff + 0x2236);
	*(U32*)(work_buff + 0x18) = 0;
	WB_0x1C = 3;
	if (*(U32*)(work_buff + 0x0C) < 4 || *(U32*)(work_buff + 0x0C) > 6)
		return DCL_ERROR_1;
	*(U16*)(work_buff + 0x10) = 0x0000FFFF >> (0x10 - *(U32*)(work_buff + 0x0C));

	if (*(U32*)(work_buff + 0x04) > 1) {
		return DCL_ERROR_2;
	}
	if (*(U32*)(work_buff + 0x04) == 1) {
		sub_1002C3D0(work_buff + 0x2FB4, dcl_table + 0x00D0, 0x0100);//unk_100576B0
		sub_1002C2D0(work_buff);
	}
	sub_1002C3D0(work_buff + 0x30F4, dcl_table + 0x00B0, 0x0010);//unk_10057690
	sub_1002C290(0x0010, work_buff + 0x30F4, dcl_table + 0x00C0, work_buff + 0x2B34);//unk_100576A0
	sub_1002C3D0(work_buff + 0x3104, dcl_table + 0x0080, 0x0010);//unk_10057660
	sub_1002C3D0(work_buff + 0x3114, dcl_table + 0x0090, 0x0020);//unk_10057670
	sub_1002C3D0(work_buff + 0x30B4, dcl_table + 0x0000, 0x0040);//unk_100575E0
	sub_1002C290(0x0040, work_buff + 0x30B4, dcl_table + 0x0040, work_buff + 0x2A34);//unk_10057620

	if (sub_1002BED0(work_buff) != 0x306) {
		return DCL_NO_ERROR;
	}
	else {
		return DCL_ERROR_4;
	}
}

void sub_1002C3D0(register U8* work_buff,	//edx
		register const U8* table,	//ecx
		register U32 count) {		//eax
	register U32 count_2 = count >> 0x02;	//esi
	if (count_2 != 0) {
		do {
			*(U32*)work_buff = *(U32*)table;
			work_buff += 4;
			table += 4;
			count_2 --;
		}
		while (count_2 != 0);
	}
	count = count & 0x03;
	if (count != 0) {
		do {
			*work_buff = *table;
			work_buff ++;
			table ++;
			count --;
		}
		while (count != 0);
	}
}

void sub_1002C290(register S16 count,		//ebx
		register U8* buffer1,		//edx
		register const U8* table,	//esi
		register U8* buffer2) {		//edi
	count --;
	if (count < 0)
		return;

	register U32 step;	//ebp
	register U32 i;		//eax
	do {
		i = *(table + count);
		step = 0x01 << *(buffer1 + count);
		do {
			*(buffer2 + (U16)i) = (U8)count;
			i += step;
		}
		while (i < 0x0100);
		count --;
	}
	while (count >= 0);
}

U32 sub_1002BED0(register U8* work_buff) {
	register const U32 edi = 0x00000305;
	*(U32*)(work_buff + 0x08) = 0x00001000;
	register const U32 ebx = 0x00000100;

	U32 write_size;		//var_8
	register U8* s;
	register U8* d;

	while ((write_size = sub_1002BFD0(work_buff)) < edi) {
		if (write_size < ebx) {
			//?
			*(work_buff + *(U32*)(work_buff + 0x08) + 0x30) = (U8)write_size;
			(*(U32*)(work_buff + 0x08)) ++;
		}
		else {
			write_size -= 0x00FE;
			s = (U8*)sub_1002C180(work_buff, write_size);
			if (!s) {
				write_size = 0x0306;
				break;
			}
			else {
				d = work_buff + *(U32*)(work_buff + 0x08) + 0x30;
				s = (U8*)((U32)d - (U32)s);
				(*(U32*)(work_buff + 0x08)) += write_size;
				do {
					*(d++) = *(s++);
					write_size --;
				}
				while (write_size != 0);
			}
		}
		if (*(U32*)(work_buff + 0x08) >= 0x2000) {
			write_size = 0x1000;
			WB_write_data(work_buff + 0x1030, write_size, WB_param);
			sub_1002C3D0(work_buff + 0x30, work_buff + 0x1030, *(U32*)(work_buff + 0x08) - 0x1000);
			(*(U32*)(work_buff + 0x08)) -= 0x1000;
		}
	}
	WB_write_data(work_buff + 0x1030, (U16)(*(U32*)(work_buff + 0x08) - 0x1000), WB_param);
	return write_size;
}

U32 sub_1002C180(register U8* work_buff, U32 flag) {
	register U32 ret;	//edi

	ret = *(work_buff + (U8)WB_0x14 + 0x2A34);
	if (sub_1002C200(work_buff, *(work_buff + ret + 0x30B4)))
		return 0x00;
	if (flag == 0x02) {
		ret <<= 0x02;
		ret |= (WB_0x14 & 0x03);
		if (sub_1002C200(work_buff, 0x02))
			return 0;
	}
	else {
		ret <<= *(work_buff + 0x0C);
		ret |= (*(U32*)(work_buff + 0x10)) & WB_0x14;
		if (sub_1002C200(work_buff, *(U32*)(work_buff + 0x0C)))
			return 0;
	}
	return ret + 0x01;
}

U32 sub_1002BFD0(register U8* work_buff) {
	register U32 ret;	//edi
	register U32 flag;	//ecx
	register U32 flag1;	//ebx

	if ((U8)WB_0x14 & 0x01) {//*(work_buff + 0x14)
		if (sub_1002C200(work_buff, 0x01))
			return 0x00000306;
		ret = *(work_buff + (U8)WB_0x14 + 0x2B34);
		if (sub_1002C200(work_buff, *(work_buff + ret + 0x30F4)))
			return 0x00000306;
		flag = *(work_buff + ret + 0x3104);
		if (flag) {
			flag1 = WB_0x14 & ((0x01 << flag) - 0x01);
			if (sub_1002C200(work_buff, flag)) {
				if (ret + flag1 != 0x0000010E)
					return 0x00000306;
			}
			ret = *(U16*)(work_buff + ret*2 + 0x3114) + flag1;//?
		}
		ret += 0x0100;
	}
	else {
		if (sub_1002C200(work_buff, 0x01))
			return 0x00000306;
		if (*(U32*)(work_buff + 0x04) == 0) {
			ret = (U8)WB_0x14;
			if (sub_1002C200(work_buff, 0x08))
				return 0x00000306;
		}
		else {
			flag = WB_0x14;
			if ((U8)flag) {
				ret = *(work_buff + (U8)flag + 0x2C34);
				if (ret == 0x000000FF) {
					if (flag & 0x0000003F) {
						if (sub_1002C200(work_buff, 0x04))
							return 0x00000306;
						ret = *(work_buff + (U8)WB_0x14 + 0x2D34);
					}
					else {
						if (sub_1002C200(work_buff, 0x06))
							return 0x00000306;
						ret = *(work_buff + (WB_0x14 & 0x0000007F) + 0x2E34);
					}
				}
			}
			else {
				if (sub_1002C200(work_buff, 0x08))
					return 0x00000306;
				ret = *(work_buff + (U8)WB_0x14 + 0x2EB4);
			}
			flag = *(work_buff + ret + 0x2FB4);
			if (sub_1002C200(work_buff, flag))
				return 0x00000306;
		}
	}
	return ret;
}

U32 sub_1002C200(register U8* work_buff,	//esi
		register U32 flag) {		//ebx
	register U32 ret;	//eax

	ret = *(U32*)(work_buff + 0x18);
	if (ret >= flag) {
		*(U32*)(work_buff + 0x18) -= flag;
		WB_0x14 >>= flag;
		ret = 0x00;
	}
	else {
		WB_0x14 >>= ret;
		ret = WB_0x20;
		if (ret == WB_0x1C) {
			WB_0x1C = 0x0800;
			WB_0x20 = WB_read_data(work_buff + 0x2234, (U16)WB_0x1C, WB_param);
			if (WB_0x20 == 0)
				return 0x01;
			WB_0x1C = 0x00;
		}
		ret = WB_0x1C;
		WB_0x14 |= *(work_buff + ret + 0x2234) << 8;
		WB_0x1C += 0x01;
		WB_0x14 >>= flag - *(U32*)(work_buff + 0x18);
		*(U32*)(work_buff + 0x18) = 0x08 - (flag - *(U32*)(work_buff + 0x18));
		ret = 0x00;
	}
	return ret;
}

void sub_1002C2D0(register U8* work_buff) {	//edi
	register S16 ebx = 0xFF;		//ebx
	register S16 edx = ebx;			//edx
	register const U8* table = dcl_table + 0x03CE;//unk_100579AE	//esi
	register U32 idx1;			//cl, ebp
	register U32 idx2;				//eax

	do {
		idx1 = *(work_buff + edx + 0x2FB4);
		if (idx1 <= 0x08) {
			idx2 = *(U32*)table;//?
			idx1 = 0x01 << idx1;
			do {
				*(work_buff + idx2 + 0x2C34) = (U8)edx;
				idx2 += idx1;
			}
			while (idx2 < 0x0100);
		}
		else {
			idx2 = *(U16*)table & (U16)ebx;
			if (idx2) {
				*(work_buff + (U16)idx2 + 0x2C34) = (U8)ebx;
				idx1 = *(work_buff + edx + 0x2FB4);
				if (*table & 0x3F) {
					idx1 -= 0x04;
					*(work_buff + edx + 0x2FB4) = (U8)idx1;
					idx1 = 0x01 << idx1;
					idx2 = *(U16*)table >> 0x04;
					do {
						*(work_buff + (U16)idx2 + 0x2D34) = (U8)edx;
						idx2 += idx1;
					}
					while (idx2 < 0x0100);
				}
				else {
					idx1 -= 0x06;
					*(work_buff + edx + 0x2FB4) = (U8)idx1;
					idx1 = 0x01 << idx1;
					idx2 = *(U16*)table >> 0x06;
					do {
						*(work_buff + (U16)idx2 + 0x2E34) = (U8)edx;
						idx2 += idx1;
					}
					while (idx2 < 0x0080);
				}
			}
			else {
				idx1 -= 0x08;
				*(work_buff + edx + 0x2FB4) = (U8)idx1;
				idx1 = 0x01 << idx1;
				idx2 = *(U16*)table >> 0x08;
				do {
					*(work_buff + (U16)idx2 + 0x2EB4) = (U8)edx;
					idx2 += idx1;
				}
				while (idx2 < 0x0100);
			}
		}
		table -= 2;
		edx --;
	}
	while (table >= dcl_table + 0x01D0);//unk_100577B0
}

