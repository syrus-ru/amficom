// FileName=ModelF.h TabSize=4 CP=1251

/*
	ModelF.h - "��������� �������".
  ������ ������������ C++ ��������� ��� ������ ModelF
  � native-��������� ��� Java-������ ModelFunction

  "��������� �������" ������������� ��� ��������
  �������������� ������� ������� (int ID) � ������
  ���������� ��� ���� �������.

  ������ ������������� ��������� ��� ������ � ����� ���������:
  - �������������� ������� ����� ���������� C++ � �������� Java
  - ������ ������������� ("����������") ��������� ������� - �����, ������...
  - ���������� �������� ������� � ������������ �����

  � ������ ������ ���� �� ������������:
  - ���������� ��������� ��������� � ��������������:
		��� ����� ��������� ������ � ��������� ������ � ����� ������� (??)
  - �������� ������������� ��������:
		��� ����� ��������� ������������� ����������
  ��� ������� ����������� � ������ �������, ���������� � ModelF.

  ����� "��������� �������" ������������ �������� ������ �� Java-������,
  �� �������� �� C-������ �� �������������, �.�. ModelF - ��� �� ����
  ������ ���������.

*/

#ifndef _MODELF_H
#define _MODELF_H

// ������������� ����������� ������ ������ ModelF (���� ��� ��� ��������)
// � ��������� ������ ��� �������� �� �����������
//void MF_init();

// �������������� (ID) ������������ ��������� �������

const int MF_ID_INVALID = 0;

// �������� �������
const int MF_ID_LIN		= 21;

// ������ �.�. ������ � ����������
const int MF_ID_SPL1	= 12;
const int MF_ID_CON1c	= 13;
const int MF_ID_CON1d	= 14;
const int MF_ID_CON1e	= 15;

// ������� BREAKL
const int MF_ID_BREAKL	= 22;

// ���������
const int MF_ID_GAUSS	= 16;

// ��������� ������������� ����������

const int MF_PAR_FLAG_X = 1;
const int MF_PAR_FLAG_W = 2;
const int MF_PAR_FLAG_L = 4;

// ���������� ������ (��������) ��� ���������

// ���������, ����� �� �������� ���������� � ��������, ���������� ��� �����. ���������
#define MF_CMD_CHECK_RANGE_PREFIT 1

// ���������, ����� �� �������� ���������� � ��������, ���������� ��� �������. ���������
#define MF_CMD_CHECK_RANGE_FINFIT 2

// ���������� �������� ���������� ����������� ��������� ��� �����. ���������
#define MF_CMD_FIX_RANGE_PREFIT 3

// ���������� �������� ���������� ����������� ��������� ��� �������. ���������
#define MF_CMD_FIX_RANGE_FINFIT 4

// ���������� �������������� �� �.�. � ������ �� �������� ACXL
#define MF_CMD_ACXL_CHANGE 5

// ����������� �������������� �������� ThreshDX, ThreshDY-�������
// � ������������ ����������, ����� DX-����� ����������� � ������ x-����������
// ���������� ������ ��� �������, BREAKL
#define MF_CMD_CHANGE_BY_THRESH_AND_FIND_DXDYID 7 /* BREAKL only */

// ����������� �������������� �������� ThreshDX, ThreshDY-�������
// � ������������ ���������� ������������ X-�������
// ���������� ������ ��� �������, BREAKL
#define MF_CMD_CHANGE_BY_THRESH_AND_FIND_TTDXDY 8

// ������� �������� �.�.
#define MF_CMD_LIN_SET_BY_X1Y1X2Y2 2101

// ������ ���������� �������� �������� �������,
// ��� ������������� ��� ���������� ModelF
// ������ �� ���������� ������ ��������� ������, � �.�. � ������ ���������
const int MF_PARID_GAUSS_AMPLITUDE = 0;
const int MF_PARID_GAUSS_CENTER = 1;
const int MF_PARID_GAUSS_SIGMA = 2;

/*
 * MF_CMD_ACXL_CHANGE:
 * extra[0]: A - ����� �����,
 * extra[1]: C - ����� ������,
 * extra[2]: X - ��������� ���������� ������������ ������,
 * extra[3]: L - ��������� ��������� (�������), ������������ �������� �����.
 */

// profiling info - ��� ������� � ��. ����� �������������, ��� �� �������.
extern int total_RMS2_counter_nl;
extern int total_RMS2_counter_lin;

// ����. ����� ���������� ��� ������ � ������������� ������ ����������
const int MF_MAX_FIXED_PARS = 16;

class ModelF
{
private:
	static int initialized; // ���������� ������� ������ ����������������
	static void initStatic();

	int entry;
	int nPars; // ����������� ����� ����������
	double parsStorage[MF_MAX_FIXED_PARS]; // ����� �������������� ��� �������� ����������
	double *parsPtr; // ��������� �� ����������� ���������� ����������

public:

	// ����������� �� ���������. ������� ������ ������ MF � isCorrect() == 0
	ModelF();

	// �������� npars ������������ ������ ��� ��������/������������� �������
	// �� ��������� ������ ����������. ��� ������������ npars,
	// �������� ����� ���������� ����� ���������� �� ����������� npars.

	// ����� ��������� � ������������ ���������� ID,
	// ����� ������ ������������ ���������� ��������� isCorrect()

	ModelF(int ID, int npars = 0);

	// ������ ����� ���������������� ��������� ���.
	// ���� pars �� �������, �� ModelF ��� ��������� ���������,
	// ��������, � parsStorage, ��������, ����� new/delete.
	// ���� ��������� �� ����������������.
	// ���� �� ������ �������� pars, ��������� mf � ��� �������� �����������
	// � ��������� ����� � ��������� ������, ��� ����� ModelF ���� ��������
	// �� ����� new double[]. ����� pars ���������� ���� ������ ModelF,
	// ��� ������ ��� ����� delete[].
	void init(int ID, int npars = 0, double *pars = 0);

	// ����� �������� ���� ������ ���������� �������.
	// ��� ���� ������������ ������ ��� ������� ������ ����� NPars,
	// � ����� �������� ������ ���������� � ���� ��� � ����������� �����
	// ����� getP() - ������� ��� ����� ModelF.
	void setP(double *pars);

	// �������� �������� ����������
	// XXX: ���� �����, ��� ����� ������ ������ � init() ?

	void zeroPars();

	int isCorrect(); // ������ ������ ���������

	~ModelF();

	// ���� ����� ���������� �� �����������, ���������� 0,
	// ���� �����������, - ������������� �����, ������ ����� ����������
	int hasFixedNumberOfPars();

	int getID(); // == MF_ID_INVALID: ������ ������ �����������

	int getNPars() // ��������� ����������� ����� ����������
	{
		return nPars;
	}

	double *getP() // ����� �������� ����������
	{
		return parsPtr;
	}

	double &operator[] (int ipar)
	{
		return parsPtr[ipar];
	}

	// ����������� ������ ��������� # ipar
	int getFlags(int ipar);

	// ���������� �������� ������� � �����
	double calcFunP(double *pars, double x);
	double calcFun(               double x)
	{
		return calcFunP(parsPtr, x);
	}

	// ���������� �������� ������� �� �����
	void calcFunArrayP(double *pars, double x0, double step, int N, double *output);
	void calcFunArray(               double x0, double step, int N, double *output)
	{
		calcFunArrayP(parsPtr, x0, step, N, output);
	}

	// ������ �������� ������������� ��������� ��� ������ ������
	// ���� ��� ������ ������ ���� �������� �� ���������, ���������� default_value
	double getAttrP(double *pars, const char *name, double default_value);
	double getAttr(               const char *name, double default_value)
	{
		return getAttrP(parsPtr, name, default_value);
	}

	// ���������� ������.
	// ��������� ������� ����� �������� ����� ����������,
	// ��� ��� ����� ������������ ������ execCmd.
	// ��� ��������� ������� ������������ ��� ��������.
	// ���� ��� ������� �� �������������� - ������ ���������� 0.0
	double execCmdP(double *pars, int command, void *extra = 0);
	double execCmd(               int command, void *extra = 0)
	{
		return execCmdP(parsPtr, command, extra);
	}

	// ������������ ������� ������� ���������� (RMS^2) ����� ���������
	// �������� ModelF � �������� �������� ������
	// (��� - ����� ��������� �� ������� ���������)
	// ��� ���������, ����� ������������ rough = 1 ��� rough = 2
	double RMS2P(double *pars, double *y, int i0, int x0, int length, int rough = 0);
	double RMS2(               double *y, int i0, int x0, int length, int rough = 0)
	{
		return RMS2P(parsPtr, y, i0, x0, length, rough);
	}

	// ��������� �������� ���������� (������� ���� PAR_FLAG_L) � ������� ��. �������� ����������.
	// ��� ������� � ���������� ������ ���������� ���������� RMS^2
	// ���. ��������� � pars[] ���������� � ����������� ���������� ����������.
	// ������������ ��� ��������� - ������ � ��������.
	double RMS2LinP(double *pars, double *y, int i0, int x0, int length, int rough = 0);
	double RMS2Lin(               double *y, int i0, int x0, int length, int rough = 0)
	{
		return RMS2LinP(parsPtr, y, i0, x0, length, rough);
	}
};

struct ACXL_data
{
	double dA;
	double dC;
	double dX;
	double dL;
};

/*
 * You can supply cache space for "unpacked" parameters.
 * To do so, you need to allocate local double cache[MF_CACHE_SIZE].
 * Then, at the first call to fptr with given parameters you specify cache=cache, valid=0,
 * and fptr will place here all cachable data that depends only upon params.
 * At next calls, you give cache=cache, valid!=0 and you will
 * see exciting increase of performance of... only 20-30%
 * LIMITATION: parameters marked as 'L' must not affect cache.
 */
const int MF_PCACHE_SIZE = 4;

// �������������� ID <-> entry
//int MF_ID2entry(int ID);
//int MF_entry2ID(int entry);

// ����������� ����� ����������
// return > 0: ������������� ����� ����������
// return == 0: ���������� ����� ����������
// return < 0: ���������������
//int MF_getNParsByEntry(int entry);

/*
// ���������� �������� ������� � �����
double MF_calc_fun(int entry, double *pars, double x);

// ������ �������� �������� ���������� MF �� ������
double RMS(double *pars, int entry, double *y, int i0, int x0, int length, int rough = 0);

// ��������� �������� ���������� (������� ���� PAR_FLAG_L) � ������� ��. �������� ����������.
// ���. ��������� � pars[] ���������� � ����������� ���������� ����������.
// ������������ ��� ��������� - ������ � ��������.
double RMS_lin(double *pars, int entry, double *y, int i0, int x0, int length, int rough = 0);

// ������ ���������� ��� ��������� �������
double getAttr(const char *name, int entry, double *pars, double default_value);
*/

#endif

