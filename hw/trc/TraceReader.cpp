#include <stdlib.h>
#include <sys/stat.h>
//#include <sys/types.h>
#include <string.h>
#include <stdio.h>
#include <math.h>
#include "TraceReader.h"
#include "T5.h"
#include "util.h"
#include "BellcoreWriter.h"
#include "com_syrus_io_TraceDataReader.h"

JNIEXPORT jbyteArray JNICALL Java_com_syrus_io_TraceDataReader_getBellcoreData__Ljava_lang_String_2(JNIEnv *env, jobject obj, jstring jfileName) {
	const char* file_name = env->GetStringUTFChars(jfileName, 0);
	unsigned char* bellcoredata;
	unsigned int bellcoredata_size;
	if (get_bellcore_data(file_name, bellcoredata, bellcoredata_size) < 0) {
		env->ReleaseStringUTFChars(jfileName, file_name);
		return NULL;
	}
	env->ReleaseStringUTFChars(jfileName, file_name);
	jbyteArray jdata = env->NewByteArray(bellcoredata_size);
	env->SetByteArrayRegion(jdata, 0, bellcoredata_size, (jbyte*)bellcoredata);
	delete[] bellcoredata;
	return jdata;
}

JNIEXPORT jbyteArray JNICALL Java_com_syrus_io_TraceDataReader_getBellcoreData___3B(JNIEnv *env, jobject obj, jbyteArray jt5data) {
	jbyte* t5_data = env->GetByteArrayElements(jt5data, 0);
	unsigned int t5_length = env->GetArrayLength(jt5data);
	unsigned char* bellcoredata;
	unsigned int bellcoredata_size;
	if (get_bellcore_data((const char*)t5_data, t5_length, bellcoredata, bellcoredata_size) < 0) {
		env->ReleaseByteArrayElements(jt5data, t5_data, 0);
		return NULL;
	}
	env->ReleaseByteArrayElements(jt5data, t5_data, 0);
	jbyteArray jdata = env->NewByteArray(bellcoredata_size);
	env->SetByteArrayRegion(jdata, 0, bellcoredata_size, (jbyte*)bellcoredata);
	delete[] bellcoredata;
	return jdata;
}

int get_bellcore_data(const char* compdata, const unsigned short compdata_size, unsigned char*& bellcoredata, unsigned int& bellcoredata_size) {
//---------
FILE* fi = fopen("t5.trc", "wb");
fwrite(compdata, 1, compdata_size, fi);
fclose(fi);
//---------
	BellcoreStructure* bs = new BellcoreStructure();
	if (fill_bellcore_structure(compdata, compdata_size, bs) >= 0) {
		BellcoreWriter* bw = new BellcoreWriter();
		bw->write(bs);
		bellcoredata = bw->get_data();
		bellcoredata_size = bw->get_data_size();
		delete bw;
		delete bs;
		return 1;
	}
	else {
		delete bs;
		return -1;
	}
}

int get_bellcore_data(const char* file_name, unsigned char*& bellcoredata, unsigned int& bellcoredata_size) {
	BellcoreStructure* bs = new BellcoreStructure();
	if (fill_bellcore_structure(file_name, bs) >= 0) {
		BellcoreWriter* bw = new BellcoreWriter();
		bw->write(bs);
		bellcoredata = bw->get_data();
		bellcoredata_size = bw->get_data_size();
		delete bw;
		delete bs;
		return 1;
	}
	else {
		delete bs;
		return -1;
	}
}

int fill_bellcore_structure(const char* compdata, const unsigned short compdata_size, BellcoreStructure* bs) {
	NTTraceData* nttrace_data = (NTTraceData*)malloc(sizeof(NTTraceData));
	short stdecomp_ret = stdecomp(compdata, compdata_size, nttrace_data);
	if (stdecomp_ret != 0) {
		printf("stdecomp returned error: %d\n", stdecomp_ret);
		free(nttrace_data);
		return -1;
	}
	print_nttrace_data(nttrace_data);

	bellcore_from_nttrace(nttrace_data, bs);

	free(nttrace_data);

	return 0;
}
int fill_bellcore_structure(const char* file_name, BellcoreStructure* bs) {
	if (file_name == NULL)
		return -1;

	if (file_name[0] == 0)
		return -1;

	struct stat file_stat;
	int stat_ret = stat(file_name, &file_stat);
	if (stat_ret != 0)
		return -1;

	int file_is_trc_ret = file_is_trc(file_name);
	if (!file_is_trc_ret) {
		//Read "UFAS Analysis" -- return later
		printf("file %s is not trc\n", file_name);
		return -1;
	}

	NTTraceData* nttrace_data = (NTTraceData*)malloc(sizeof(NTTraceData));
	short ftdecomp_ret = ftdecomp(file_name, nttrace_data);
	if (ftdecomp_ret != 0) {
		printf("ftdecomp returned error: %d, file: <%s>\n", ftdecomp_ret, file_name);
		free(nttrace_data);
		return -1;
	}
	print_nttrace_data(nttrace_data);

	bellcore_from_nttrace(nttrace_data, bs);

	free(nttrace_data);

	return 0;
}


double dbl_10050458 = 1.4989625e-3;	//dbl_10050458
double dbl_100504F0 = 1.0e4;		//dbl_100504F0
double dynamic_range_koeff = 1.0;	//dbl_10050478
double min_refl_val = 0.0;		//dbl_10050468
double dbl_10050460 = 3.4e2;		//dbl_10050460

void bellcore_from_nttrace(const NTTraceData* nttrace_data, BellcoreStructure* bs) {

	bs->add_field_gen_params("Cable ID",
			"Fiber ID",
			0,
			nttrace_data->_00014,
			"Originating Location",
			"Terminating Location",
			"Cable code",
			"DF",
			"Operator",
			"QuestFiber");

	bs->add_field_sup_params("Supplier name",
			"Nettest Starprobe",
			"OTDR",
			nttrace_data->_00084,
			"OM serial number",
			"Software revision",
			"Other");

	short tpw = 1;
	short* pwu = new short[tpw];
	pwu[0] = nttrace_data->_00020[2];
	int* ds = new int[tpw];
	ds[0] = (int)(nttrace_data->_40072 * nttrace_data->_00012 / 3. * 100);
	int* nppw = new int[tpw];
	nppw[0] = nttrace_data->_40066;
	int ar = (int)(nttrace_data->_40072 * nttrace_data->_40066 * (nttrace_data->_00012/10000.) * 100 / 3.);
	bs->add_field_fxd_params(nttrace_data->_00002,
			"mt",
			nttrace_data->_00014 * 10,
			0,
			tpw,
			pwu,
			ds,
			nppw,
			nttrace_data->_00012 * 10,
			nttrace_data->_00044,
			ar);
	delete[] pwu;
	delete[] ds;
	delete[] nppw;

	unsigned int i;
//	double km_per_data_point = (nttrace_data->_40072 * dbl_10050458)/(nttrace_data->_00012 / dbl_100504F0);
	double dynamic_range = nttrace_data->_00048*8 + 24;
	double d = dynamic_range_koeff * dynamic_range;
	double* reflectogramma = new double[nttrace_data->_40066];
	for (i = 0; i < nttrace_data->_40066; i++) {
		if (nttrace_data->_40068[i] > 0) {
			reflectogramma[i] = d - (nttrace_data->_40068[i]*dynamic_range_koeff / dbl_10050460);
			if (reflectogramma[i] < min_refl_val)
				reflectogramma[i] = d;
		}
		else
			reflectogramma[i] = d;
	}
//---------
FILE* fi = fopen("reflectogramma", "wb");
for (i = 0; i < nttrace_data->_40066; i++)
	fprintf(fi, "%hd\n", nttrace_data->_40068[i]);//fprintf(fi, "%f\n", reflectogramma[i]);
fclose(fi);
//---------
	short tsf = 1;
	int* tps = new int[tsf];
	tps[0] = nttrace_data->_40066;
	short* sf = new short[tsf];
	sf[0] = 1000;
	unsigned short** dsf = new unsigned short*[tsf];
	dsf[0] = new unsigned short[nttrace_data->_40066];
	for (i = 0; i < nttrace_data->_40066; i++)
		dsf[0][i] = (unsigned short)(reflectogramma[i] * 1000);
	delete[] reflectogramma;
	bs->add_field_data_pts(nttrace_data->_40066,
			tsf,
			tps,
			sf,
			dsf);
	delete[] tps;
	delete[] sf;
	//!!!Don't delete dsf - it will be deleted in the destructor of BellcoreStructure

	bs->add_field_cksum(0);

	bs->add_field_map();

}

int file_is_trc(const char* file_name) {
	char* file_ext = strrchr(file_name, '.') + 1;	//ebp+var_4
	if (strcmp(file_ext, "trc") == 0)
		return 1;
	else
		return 0;
}

short stdecomp(const char* compdata, unsigned short compdata_size, NTTraceData* nttrace_data) {
	short ret = 311;
	//-------------- ?
	long temp = compdata_size & 0x8000000F;
	if (temp < 0) {
		temp--;
		temp = temp | 0xFFFFFFF0;
		temp++;
	}
	long compdata_size_1 = compdata_size + (16 - temp);
	if ((compdata_size_1 & 0x0FFFF) != compdata_size_1)
		return 321;
	//^^^^^^^^^^^^^^ ?

	memset(nttrace_data, 0, sizeof(NTTraceData));
	unsigned short offset = 0;
	short size1;
	read_short(size1, (char*)compdata, offset);
	size1 += 4;
	if (size1 > compdata_size_1)
		size1 = 0;
	offset = 22;
	short size2;
	read_short(size2, (char*)compdata, offset);
	size2 += 46;
	if (size2 > compdata_size_1)
		size2 = 0;
	if (strncmp("<T5>", compdata, 4) == 0)
		nttrace_data->_40168 = nttrace_data->_40168 & 0xFFFE;//i.e. - 0?
	else
		nttrace_data->_40168 = nttrace_data->_40168 | 1;//i.e. - 1?
	if (strncmp("<T5>", compdata, 4) == 0) {
		ret = decompress_T5(compdata, compdata_size, nttrace_data);
		if (ret == 0) {
			nttrace_data->_40066 = get_number_of_points(nttrace_data) - 1;
			nttrace_data->_40068 = &(nttrace_data->_07296[nttrace_data->_07294]);
			set_misc_data(nttrace_data);
		}
		return ret;
	}
	else{
		//Read <T4> or something else -- return later
		return ret;
	}
}

short ftdecomp(const char* file_name, NTTraceData* nttrace_data) {
	short ret = 311;
	FILE* fp = fopen(file_name, "rb");
	if (fp == NULL)
		return 349;

	long file_size = get_file_size(fp);
	//-------------- ?
	long temp = file_size & 0x8000000F;
	if (temp < 0) {
		temp--;
		temp = temp | 0xFFFFFFF0;
		temp++;
	}
	file_size = file_size + (16 - temp);
	if ((file_size & 0x0FFFF) != file_size) {
		fclose(fp);
		return 321;
	}
	//^^^^^^^^^^^^^^ ?

	char* read_file_buffer = (char*)malloc(file_size);
	if (read_file_buffer == NULL) {
		fclose(fp);
		return 350;
	}
	short bytes_read = fread(read_file_buffer, 1, file_size, fp);
	if (bytes_read == 0) {
		fclose(fp);
		free(read_file_buffer);
		return 350;
	}
	memset(nttrace_data, 0, sizeof(NTTraceData));
	char* buffer = read_file_buffer;
	unsigned short offset = 0;
	short size1;
	read_short(size1, read_file_buffer, offset);
	size1 += 4;
	if (size1 > file_size)
		size1 = 0;
	offset = 22;
	short size2;
	read_short(size2, read_file_buffer, offset);
	size2 += 46;
	if (size2 > file_size)
		size2 = 0;
/*	char* pointer1 = read_file_buffer + size1;
	char* pointer2 = read_file_buffer + size2;*/
	if (strncmp("<T5>", buffer, 4) == 0)
		nttrace_data->_40168 = nttrace_data->_40168 & 0xFFFE;//i.e. - 0?
	else
		nttrace_data->_40168 = nttrace_data->_40168 | 1;//i.e. - 1?
	if (strncmp("<T5>", buffer, 4) == 0) {
		ret = decompress_T5(read_file_buffer, bytes_read, nttrace_data);
		if (ret == 0) {
			nttrace_data->_40066 = get_number_of_points(nttrace_data) - 1;
			nttrace_data->_40068 = &(nttrace_data->_07296[nttrace_data->_07294]);
			set_misc_data(file_name, nttrace_data);
		}
		fclose(fp);
		free(read_file_buffer);
		return ret;
	}
	else{
		//Read <T4> or something else -- return later
		return ret;
	}
}

unsigned short get_number_of_points(NTTraceData* nttrace_data) {
	return (1 << (nttrace_data->_00010 + 8 - nttrace_data->_00008)) * (1 << nttrace_data->_00006) - nttrace_data->_07294 - (16 << nttrace_data->_00006);
}

float flt_10050720 = 1.25;
float flt_1005071C = 1.0e1;
float flt_10050718 = 7.0;
float flt_10050714 = 8.0;
float flt_10050710 = 1.0;
float flt_1005070C = 5.0e-1;
float flt_10050708 = (float)2.99792;
float flt_10050704 = 2.0e3;
float flt_10050700 = 1.0e4;

void set_misc_data(NTTraceData* nttrace_data) {
	double d = 1 << nttrace_data->_00008;
	nttrace_data->_40072 = (float)(d * pow(2, 3 - nttrace_data->_00006));

	if (nttrace_data->_00218 == 1) {
		nttrace_data->_40072 = nttrace_data->_40072 * flt_10050720;
	}

	float f = nttrace_data->_00020[2] / (flt_1005071C * nttrace_data->_40072);
	if ((f > flt_10050718) && (f < flt_10050714))
		f = f + flt_10050710;
	else
		f = f + flt_1005070C;
	nttrace_data->_40076 = (short)f;
	int i;
	if (nttrace_data->_40076 != 0)
		i = nttrace_data->_40076;
	else
		i = 1;
	nttrace_data->_40076 = i;

	nttrace_data->_40082 = 0x3B40C0C1;
	nttrace_data->_40078 = nttrace_data->_40072 * flt_10050708 / (nttrace_data->_00012 * flt_10050704 / flt_10050700);
	nttrace_data->_40114 = get_40114(nttrace_data, 0x0C28C0000);
	nttrace_data->_00224 = nttrace_data->_00224 | 0x0008;
	nttrace_data->_40118 = nttrace_data + 254;	//i.e. - nttrace_data->_00245
}

void set_misc_data(const char* file_name, NTTraceData* nttrace_data) {
	struct stat file_stat;
	if (nttrace_data->_00002 == 0) {
		stat(file_name, &file_stat);
		nttrace_data->_00002 = file_stat.st_mtime;
		if (nttrace_data->_00002 == -1)
			nttrace_data->_00002 = 0;
	}

	double d = 1 << nttrace_data->_00008;
	nttrace_data->_40072 = (float)(d * pow(2, 3 - nttrace_data->_00006));

	if (nttrace_data->_00218 == 1) {
		nttrace_data->_40072 = nttrace_data->_40072 * flt_10050720;
	}

	float f = nttrace_data->_00020[2] / (flt_1005071C * nttrace_data->_40072);
	if ((f > flt_10050718) && (f < flt_10050714))
		f = f + flt_10050710;
	else
		f = f + flt_1005070C;
	nttrace_data->_40076 = (short)f;
	int i;
	if (nttrace_data->_40076 != 0)
		i = nttrace_data->_40076;
	else
		i = 1;
	nttrace_data->_40076 = i;

	nttrace_data->_40082 = 0x3B40C0C1;
	nttrace_data->_40078 = nttrace_data->_40072 * flt_10050708 / (nttrace_data->_00012 * flt_10050704 / flt_10050700);
	nttrace_data->_40114 = get_40114(nttrace_data, 0x0C28C0000);
	nttrace_data->_00224 = nttrace_data->_00224 | 0x0008;
	nttrace_data->_40118 = nttrace_data + 254;	//i.e. - nttrace_data->_00245
}

double dbl_10050828 = 1.0e-9;
double dbl_10050558 = 5.0;

short get_40114(NTTraceData* nttrace_data, float f) {
	f = f / flt_1005071C;
	float f4;
	if (nttrace_data->_00018 != 0) {
		float f1 = (float)(nttrace_data->_00020[2] * dbl_10050828);
		double d1 = log10(nttrace_data->_00018 * f1);
		double d2 = f - d1;
		float f3 = (float)pow(3, d2);
		d2 = f3 + flt_10050710;
		f4 = (float)(dbl_10050558 * log10(d2));
	}
	else
		f4 = 0;
	return (short)(f4 / nttrace_data->_40082);
}

long get_file_size(FILE* fp) {
	long ftell_ret1 = ftell(fp);
	fseek(fp, 0, SEEK_END);
	long ftell_ret2 = ftell(fp);
	fseek(fp, ftell_ret1, SEEK_SET);
	return ftell_ret2;
}

void print_nttrace_data(NTTraceData* nttrace_data) {
	int i, j;
	FILE* fp = fopen("nttrace_data", "wb");
	fprintf(fp ,"_00000: %hd\n", nttrace_data->_00000);
	fprintf(fp ,"_00002: %d\n", nttrace_data->_00002);
	fprintf(fp ,"_00006: %hd\n", nttrace_data->_00006);
	fprintf(fp ,"_00008: %hd\n", nttrace_data->_00008);
	fprintf(fp ,"_00010: %hd\n", nttrace_data->_00010);
	fprintf(fp ,"_00012: %hd\n", nttrace_data->_00012);
	fprintf(fp ,"_00014: %hd\n", nttrace_data->_00014);
	fprintf(fp ,"_00016: %hd\n", nttrace_data->_00016);
	fprintf(fp ,"_00018: %hd\n", nttrace_data->_00018);
	for (i = 0; i < 12; i++)
		fprintf(fp ,"_00020[%d]: %hd\n", i, nttrace_data->_00020[i]);
	fprintf(fp ,"_00044: %d\n", nttrace_data->_00044);
	fprintf(fp ,"_00048: %hd\n", nttrace_data->_00048);
	fprintf(fp ,"_00050: %hd\n", nttrace_data->_00050);
	fprintf(fp ,"_00052: %hd\n", nttrace_data->_00052);
	fprintf(fp ,"_00054: %hd\n", nttrace_data->_00054);
	for (i = 0; i < 12; i++)
		fprintf(fp ,"_00056[%d]: %hd\n", i, nttrace_data->_00056[i]);
	fprintf(fp ,"_00080: %d\n", nttrace_data->_00080);
	for (i = 0; i < 10; i++)
		fprintf(fp ,"_00084[%d]: %d %c\n", i, nttrace_data->_00084[i], nttrace_data->_00084[i]);
	fprintf(fp ,"_00094: %hd\n", nttrace_data->_00094);
	fprintf(fp ,"_00096: %hd\n", nttrace_data->_00096);
	fprintf(fp ,"_00098: %hd\n", nttrace_data->_00098);
	fprintf(fp ,"_00100: %hd\n", nttrace_data->_00100);
	fprintf(fp ,"_00102: %hd\n", nttrace_data->_00102);
	fprintf(fp ,"_00104: %d\n", nttrace_data->_00104);
	fprintf(fp ,"_00108: %d\n", nttrace_data->_00108);
	fprintf(fp ,"_00112: %d %c\n", nttrace_data->_00112, nttrace_data->_00112);
	fprintf(fp ,"_00113: %d %c\n", nttrace_data->_00113, nttrace_data->_00113);
	for (i = 0; i < 3; i++)
		fprintf(fp ,"_00114[%d]: %hd\n", i, nttrace_data->_00114[i]);
	for (i = 0; i < 3; i++)
		fprintf(fp ,"_00120[%d]: %hd\n", i, nttrace_data->_00120[i]);
	fprintf(fp ,"_00126: %hd\n", nttrace_data->_00126);
	fprintf(fp ,"_00128: %d\n", nttrace_data->_00128);
	fprintf(fp ,"_00132: %d\n", nttrace_data->_00132);
	for (i = 0; i < 2; i++)
		fprintf(fp ,"_00136[%d]: %d\n", i, nttrace_data->_00136[i]);
	for (i = 0; i < 2; i++)
		fprintf(fp ,"_00144[%d]: %d\n", i, nttrace_data->_00144[i]);
	for (i = 0; i < 2; i++)
		for (j = 0; j < 3; j++)
			fprintf(fp ,"_00152[%d][%d]: %d\n", i, j, nttrace_data->_00152[i][j]);
	for (i = 0; i < 2; i++)
		for (j = 0; j < 3; j++)
			fprintf(fp ,"_00176[%d][%d]: %d\n", i, j, nttrace_data->_00176[i][j]);
	fprintf(fp ,"_00200: %hd\n", nttrace_data->_00200);
	fprintf(fp ,"_00202: %hd\n", nttrace_data->_00202);
	fprintf(fp ,"_00204: %hd\n", nttrace_data->_00204);
	fprintf(fp ,"_00206: %hd\n", nttrace_data->_00206);
	fprintf(fp ,"_00208: %hd\n", nttrace_data->_00208);
	fprintf(fp ,"_00210: %d\n", nttrace_data->_00210);
	fprintf(fp ,"_00214: %d\n", nttrace_data->_00214);
	fprintf(fp ,"_00218: %hd\n", nttrace_data->_00218);
	for (i = 0; i < 4; i++)
		fprintf(fp ,"_00220[%d]: %d %c\n", i, nttrace_data->_00220[i], nttrace_data->_00220[i]);
	fprintf(fp ,"_00224: %hd\n", nttrace_data->_00224);
	for (i = 0; i < 8; i++)
		fprintf(fp ,"_00226[%d]: %d %c\n", i, nttrace_data->_00226[i], nttrace_data->_00226[i]);
	for (i = 0; i < 9; i++)
		fprintf(fp ,"_00234[%d]: %hd\n", i, nttrace_data->_00234[i]);
	fprintf(fp ,"_00252: %hd\n", nttrace_data->_00252);
	for (i = 0; i < 64; i++)
		print_nttrace_data_sub(&nttrace_data->_00254[i], fp);	
	fprintf(fp ,"_07294: %hd\n", nttrace_data->_07294);
	for (i = 0; i < 16384; i++)
		fprintf(fp ,"_07296[%d]: %hd\n", i, nttrace_data->_07296[i]);
	fprintf(fp ,"_40064: %hd\n", nttrace_data->_40064);
	fprintf(fp ,"_40066: %hd\n", nttrace_data->_40066);
	fprintf(fp ,"_40072: %f\n", nttrace_data->_40072);
	fprintf(fp ,"_40076: %hd\n", nttrace_data->_40076);
	fprintf(fp ,"_40078: %f\n", nttrace_data->_40078);
	fprintf(fp ,"_40082: %d\n", nttrace_data->_40082);
	fprintf(fp ,"_40114: %hd\n", nttrace_data->_40114);
	fprintf(fp ,"_40122: %hd\n", nttrace_data->_40122);
	fprintf(fp ,"_40168: %hd\n", nttrace_data->_40168);
	fclose(fp);
}

void print_nttrace_data_sub(NTTraceData_Sub* nttrace_data_sub, void* fpv) {
	int i;
	FILE* fp = (FILE*)fpv;
	fprintf(fp ,"\n");
	fprintf(fp ,"\t_000: %hd\n", nttrace_data_sub->_000);
	fprintf(fp ,"\t_002: %hd\n", nttrace_data_sub->_002);
	fprintf(fp ,"\t_004: %hd\n", nttrace_data_sub->_004);
	fprintf(fp ,"\t_006: %d\n", nttrace_data_sub->_006);
	fprintf(fp ,"\t_010: %d\n", nttrace_data_sub->_010);
	fprintf(fp ,"\t_014: %hd\n", nttrace_data_sub->_014);
	fprintf(fp ,"\t_016: %hd\n", nttrace_data_sub->_016);
	fprintf(fp ,"\t_018: %hd\n", nttrace_data_sub->_018);
	fprintf(fp ,"\t_020: %d\n", nttrace_data_sub->_020);
	fprintf(fp ,"\t_024: %d\n", nttrace_data_sub->_024);
	fprintf(fp ,"\t_028: %hd\n", nttrace_data_sub->_028);
	fprintf(fp ,"\t_030: %d\n", nttrace_data_sub->_030);
	fprintf(fp ,"\t_034: %d\n", nttrace_data_sub->_034);
	fprintf(fp ,"\t_038: %hd\n", nttrace_data_sub->_038);
	fprintf(fp ,"\t_040: %hd\n", nttrace_data_sub->_040);
	fprintf(fp ,"\t_042: %hd\n", nttrace_data_sub->_042);
	fprintf(fp ,"\t_044: %d\n", nttrace_data_sub->_044);
	for (i = 0; i < 62; i++)
		fprintf(fp ,"\t_048[%d]: %d %c\n", i, nttrace_data_sub->_048[i], nttrace_data_sub->_048[i]);
}

