// Model Functions implementation

#include <math.h>
#include <assert.h>
#include <string.h> // strcmp; strlen
#include "ModelF.h"
#include "mat-solve.h"
#include "../BreakL/BreakL-mf.h"

#include "prf.h"

//#include <stdio.h> // FIXIT: debug only

const double PI = 3.14159265358979323846;

const int MF_MAX_ID = 100;

// отображение ID -> entry
// должно быть проинициализировано функцией MF_init()
static int ID_to_entry[MF_MAX_ID];

// тип "функция реализация профиля модельной функции"
// параметры cache и valid используются для ускорения расчета, см. описание MF_PCACHE_SIZE
// Вызывающая сторона может оставить значения по умолчанию
// Вызываемая сторона может игнорировать эти параметры.
// npars - должен игнорироваться в случае фиксированного числа параметров
typedef double (*MF_Tfptr) (double *pars, int npars, double x, double *cache = 0, int valid = 0);

// расчет значений функции в диапазоне - необязательный метод
// здесь кэш не нужен
typedef void (*MF_Tfarrptr) (double *pars, int npars, double x0, double step, int N, double *buffer);

// тип "функция реализация аттрибутов"
typedef double (*MF_Tattrp) (double *pars, int npars);

// тип "функция выполнения команд"
typedef double (*MF_Tcmdp) (double *pars, ModelF &mf, int command, void *args);

// тип "аттрибут"
struct MF_Attr
{
	const char *name;
	MF_Tattrp fun;
};

// Описание конкретных модельных функций

const int MF_MAX_ATTRS = 10; // макс. число аттрибутов у модельной функции

struct MF_MD
{
	int ID;						// shapeID - ID функции
	int npars;					// полное число параметров (аргументов)
	char *psig;					// сигнатура; если psig!=0, то strlen(psig)==pars
	MF_Tfptr fptr;				// shape function
	MF_Tfarrptr farrptr;		// shape quick array computation function
	MF_Tcmdp cmd;				// command function list
	MF_Attr attr[MF_MAX_ATTRS];	// attributes function list
};

extern struct MF_MD funcs[]; // будет ниже

inline const char *i_entry2sig(int n) { return funcs[n].psig; }
inline int i_ID2entry(int ID) { return (unsigned )ID < MF_MAX_ID ? ID_to_entry[ID] : -1; }
inline int i_entry2ID(int n) { return funcs[n].ID; }
inline int i_entry2npars(int n) { return funcs[n].npars; }

int ModelF::initialized; // глобальные таблицы модуля инициализированы

// инициализация статической таблицы ID_to_entry
// должна была вызвана до любой работы с ModelF
// не используется
void MF_init() {}

void MF_do_init()
{
	int i;

	for (i = 0; i < MF_MAX_ID; i++)
		ID_to_entry[i] = -1;

	for (i = 0; funcs[i].ID != MF_ID_INVALID; i++)
	{
		// должно быть [0..MF_MAX_FIXED_PARS) фиксированных параметров
		assert((unsigned)funcs[i].npars <= MF_MAX_FIXED_PARS);

		// ID должен умещаться во вспомогательной таблице
		assert((unsigned)funcs[i].ID < MF_MAX_ID);

		// у функций с переменным числом параметров сигнатуры быть
		// (в этой версии) не может
		if (funcs[i].npars == 0)
			assert(funcs[i].psig == 0);

		// если сигнатура есть, ее длина должна быть равна числу параметров
		if (funcs[i].psig)
			assert((int)strlen(funcs[i].psig) == (int)funcs[i].npars);

		// заполняем таблицу обратного отображения
		ID_to_entry[funcs[i].ID] = i;
	}
}

void ModelF::initStatic()
{
	if (!initialized)
	{
		MF_do_init();
		initialized++;
	}
}

ModelF::ModelF()
{
	initStatic();
	parsPtr = parsStorage;
	nPars = 0;
	entry = -1;
}

ModelF::ModelF(int ID, int np_)
{
	initStatic();
	parsPtr = parsStorage;
	init(ID, np_);
}

void ModelF::init(int ID_, int np_, double *pars_)
{
	if (parsPtr != parsStorage)
		delete[] parsPtr;

	nPars = 0;
	parsPtr = parsStorage;
	entry = i_ID2entry(ID_);

	if (entry < 0)
		return; // неверный ID

	nPars = i_entry2npars(entry);

	// если число параметров переменное - используем np_
	if (nPars == 0)
	{
		nPars = np_;
		if (nPars < 0)
			nPars = 0;
	}

	if (pars_)
	{
		parsPtr = pars_;
	}
	else if (nPars > MF_MAX_FIXED_PARS)
	{
		parsPtr = new double[nPars];
		assert(parsPtr);
	}
}

void ModelF::setP(double *pars)
{
	if (parsPtr != parsStorage)
		delete[] parsPtr;
	parsPtr = pars;
}

void ModelF::zeroPars()
{
	assert(entry >= 0);
	int i;
	for (i = 0; i < nPars; i++)
		parsPtr[i] = 0;
}

int ModelF::hasFixedNumberOfPars()
{
	assert(entry >= 0);
	return i_entry2npars(entry);
}

int ModelF::isCorrect()
{
	return entry >= 0;
}

ModelF::~ModelF()
{
	if (parsPtr != parsStorage)
		delete[] parsPtr;
}

int ModelF::getID()
{
	if (entry < 0)
		return MF_ID_INVALID;
	else
		return i_entry2ID(entry);
}

int ModelF::getFlags(int ipar)
{
	assert(entry >= 0);
	if (funcs[entry].psig == 0)
		return 0; // сигнатура не указана
	switch (funcs[entry].psig[ipar])
	{
	case 'X':
		return MF_PAR_FLAG_X;
	case 'W':
		return MF_PAR_FLAG_W;
	case 'L':
		return MF_PAR_FLAG_L;
	default:
		return 0;
	}
}

double ModelF::calcFunP(double *pars, double x)
{
	assert(entry >= 0);
	return funcs[entry].fptr(pars, nPars, x);
}

void ModelF::calcFunArrayP(double *pars, double x0, double step, int N, double *output)
{
	assert(entry >= 0);
	MF_Tfarrptr farrptr = funcs[entry].farrptr;
	if (farrptr)
	{
		farrptr(pars, nPars, x0, step, N, output);
	}
	else
	{
		MF_Tfptr fptr = funcs[entry].fptr;
		int i;
		for (i = 0; i < N; i++)
		{
			// XXX: сюда надо добавить поддержку кэша fptr, это пара строк, но нет времени отлаживать
			output[i] = fptr(pars, nPars, x0 + i * step);
		}
	}
}

double ModelF::getAttrP(double *pars, const char *name, double default_value)
{
	assert(entry >= 0);
	MF_Attr *ap = funcs[entry].attr;
	while (ap->name)
	{
		if (!strcmp(ap->name, name))
			if (ap->fun)
				return ap->fun(pars, nPars); // calculate attribute
			else
				return default_value; // invalid table record => return default value
		else
			ap++;
	}
	//fprintf(stderr, "getAttrP: returning default for '%s'\n", name); // FIXIT
	return default_value; // no record found -- return default value
}

double ModelF::execCmdP(double *pars_, int command, void *extra)
{
	assert(entry >= 0);
	if (funcs[entry].cmd)
		return funcs[entry].cmd(pars_, *this, command, extra);
	else
		return 0;
}

double ModelF::RMS2P(double *pars, double *y, int i0, int x0, int length, int rough)
{
	assert(entry >= 0);
	int j;
	MF_Tfptr fp = funcs[entry].fptr;

	int step = rough ? rough > 1 ? 7 : 3 : 1; // XXX: find if other values are ok,. if not, why?

	double cache[MF_PCACHE_SIZE] = {0};

	if (length < step * 3 + 1)
	{
		rough = 0;
		step = 1;
	}

	total_RMS2_counter_nl += length / step;

	if (rough == 0)
	{
		double sum = 0;
		for (j = 0; j < length; j ++)
		{
			double vy = y[i0 + j];
			double vf = fp(pars, nPars, (double )(x0 + j), cache, j);
			sum += (vy - vf) * (vy - vf);
		}
		if (length > 0)
			sum /= length;
		return sum;
	}
	else
	{
		double sum = 0;
		int count = 0;
		for (j = 0; j < length + step - 1; j += step)
		{
			if (j >= length)
				j = length - 1; // infinite loop will not occur
			double vy = y[i0 + j];
			double vf = fp(pars, nPars, (double )(x0 + j), cache, j);
			sum += (vy - vf) * (vy - vf);
			count ++;
		}
		if (count)
			sum /= count;
		return sum;
	}
}

double ModelF::RMS2LinP(double *pars, double *y, int i0, int x0, int length, int rough)
{
	assert(entry >= 0);
	MF_Tfptr fp = funcs[entry].fptr;
	int c_lin = 0;
	int c_nl = 0;
	int i, j;
	int lp[MF_MAX_FIXED_PARS];

	// защитный код "на будущее".
	// должен срабатывать только в случаях переменного числа параметров.
	if (nPars > MF_MAX_FIXED_PARS)
		return RMS2P(pars, y, i0, x0, length, rough); // XXX

	// разделяем параметры на линейные и нелинейные
	// для функций с переменным числом параметров ничего не происходит
	for (i = 0; i < nPars; i++)
	{
		if (getFlags(i) & MF_PAR_FLAG_L)
			lp[c_lin++] = i;
		else
			c_nl++;
	}

	// линейные параметры не найдены - ничего не делаем
	if (c_lin == 0)
		return RMS2P(pars, y, i0, x0, length, rough);

	double *F2 = new double[c_lin * c_lin];
	double *F1 = new double[c_lin];
	double *ft = new double[c_lin];
	double dy2;
	int cnt = 0;

	dy2 = 0;
	for (i = 0; i < c_lin; i++)
		F1[i] = 0;
	for (i = 0; i < c_lin * c_lin; i++)
		F2[i] = 0;

	int step = rough ? rough > 1 ? 7 : 3 : 1; // XXX: find if other divisors are ok,. if not, why?
	if (length < step * 3 + 1)
	{
		rough = 0;
		step = 1;
	}
	total_RMS2_counter_lin += length / step;

	double cache[MF_PCACHE_SIZE] = {0};

	double sum = 0;
	int count = 0;
	for (j = 0; j < length + step - 1; j += step)
	{
		if (j >= length)
			j = length - 1; // infinite loop will not occur

		// calc fz
		for (i = 0; i < c_lin; i++)
			pars[lp[i]] = 0;
		double vfz = fp(pars, nPars, (double )(x0 + j), cache, j);

		// calc y
		double dy = y[i0 + j] - vfz;
		dy2 += dy * dy;

		// calc f_i, F1
		for (i = 0; i < c_lin; i++)
		{
			pars[lp[i]] = 1;
			ft[i] = fp(pars, nPars, (double )(x0 + j), cache, j) - vfz;
			pars[lp[i]] = 0;
			F1[i] += ft[i] * dy;
		}

		// calc F2
		for (i = 0; i < c_lin; i++)
		{
			int n;
			for (n = 0; n < c_lin; n++)
				F2[i*c_lin + n] += ft[i] * ft[n];
		}
		count++;
	}

	double ret;
	if (count)
	{
		// XXX: правильно ли мы обрабатываем случай вырожденной матрицы?
		mat_solvex(c_lin, F2, F1, ft);

		// now, ft_i is the resulting x_i

		// fill pars with solution

		for (i = 0; i < c_lin; i++)
		{
			pars[lp[i]] = ft[i];
		}
		//for (i = 0; i < entry2npars(entry); i++)
		//	fprintf (stderr, "lin all pars: par %d val %g\n", i, pars[i]);

		double ret_expect = 0;

		// note: tho following code may be simplified due to x*F2*x == F1*x
		for (i = 0; i < c_lin; i++)
			for (j = 0; j < c_lin; j++)
				ret_expect += ft[i] * F2[i*c_lin + j] * ft[j];
		for (i = 0; i < c_lin; i++)
			ret_expect -= 2 * F1[i] * ft[i];
		ret_expect += dy2;

		ret_expect /= count;

		//fprintf (stderr, "lin RMS expect: %g\n", ret_expect);
		ret = ret_expect; //RMS(pars, fp, y, i0, x0, length, rough);
		//fprintf (stderr, "lin RMS %g\n", ret);
	}
	else
		ret = 0;

	delete[] F2;
	delete[] F1;
	delete[] ft;

	return ret;
}

// экспортируются для отладки - счетчики числа обращений к f() из RMS2()
int total_RMS2_counter_nl = 0;
int total_RMS2_counter_lin = 0;

inline double dabs(double v)
{
	return v > 0
		? v
		: -v;
}

inline double dpow2(double v)
{
	return v * v;
}

// модельные функции

static double f_LIN0(double *pars, int, double x, double *, int)
{
	return pars[0] + x * pars[1];
}

static double f_SPL1(double *pars, int, double x, double *, int)
{
	double a = pars[0];
	double b = pars[1];
	double boost = pars[2];
	double center = pars[3];
	double width = pars[4];
	{
		double ret;
		double arg = x - center;
		double halfWidth = width/2.;

		if(arg < -halfWidth)
			ret = -1.;
		else if(arg > halfWidth)
			ret = 1.;
		else
			ret = sin(PI*arg / width);

		ret = ret * boost/2. + a + b*arg;
		return ret;
	}
}

#define BIG_EXP 30

static double expa(double x, double s1, double s2, double part)
{
    double arg1 = x/s1;
    double arg2 = x/s2;
    double val1 = exp(-arg1) * part;
	double val2 = exp(-arg2) * (1.-part);
    return val1 + val2;
}

static double CON1_dHm (double *pars)
{
	return 1. - exp(-(pars[4] - pars[3]) / pars[5]);
}

static double f_CON1c(double *pars, int, double x, double *cache, int valid)
{
	//double a1_connector = pars[0];
	//double a2_connector = pars[1];
	//double aLet_connector = pars[2];
	double level0 = pars[0]; // == a1
	double dlevel1 = pars[1];// == aLet
	double dlevel2 = pars[2];// == (tmp ~= a1+aLet) - a2

	double x0 = pars[3];
	double x1 = pars[4];
	double sigma1_connector = pars[5];
	double sigma2_connector = pars[6];
	double sigmaFit_connector = pars[7];
	double k_connector = pars[8];

	double width_connector  = x1 - x0;

	if (cache && !valid)
		//cache[0] = (1. - exp(-width_connector / sigma1_connector));
		cache[0] = CON1_dHm(pars);

	// XXX: ?: <=x / <x< / x>=

	if (x < x1)
	{
		if (x <= x0)
			return level0;

		// x0 < x < x1

		if (dlevel1 == 0)
			return level0;
		if ((x - x0) > BIG_EXP * sigma1_connector) // XXX
			return dlevel1 + level0;
		else
			return dlevel1 * (1. - exp(-(x - x0) / sigma1_connector)) + level0;
	}
	else // x >= x1
	{
		double tmp;
		if (cache) // if cache is present, it is valid by now
			tmp = level0 + dlevel1 * cache[0];
		else
			tmp = level0 + dlevel1 * CON1_dHm(pars);

		if (dlevel2 == 0)
			return tmp;
		else
			return tmp -
			   dlevel2 *
			   (1. - expa(x - x1,
				  sigma2_connector,
				  sigmaFit_connector,
				  k_connector));
	}
}

static double f_CON1d(double *pars, int, double x, double *cache, int valid)
{
	// лин. параметры
	double level0 = pars[0]; // == a1
	double dlevel1 = pars[1];// == aLet
	double dlevel2 = pars[2];// == (tmp ~= a1+aLet) - a2
	double tgFit = pars[7];

	// нел. пар-ры
	double x0 = pars[3];
	double x1 = pars[4];
	double sigma1_connector = pars[5];
	double sigma2_connector = pars[6];

	double width_connector  = x1 - x0;

	if (cache && !valid)
		cache[0] = CON1_dHm(pars);

	// XXX: ?: <=x / <x< / x>=

	if (x < x1)
	{
		if (x <= x0)
			return level0;

		// x0 < x < x1

		if (dlevel1 == 0)
			return level0;
		if ((x - x0) > BIG_EXP * sigma1_connector) // XXX
			return dlevel1 + level0;
		else
			return dlevel1 * (1. - exp(-(x - x0) / sigma1_connector)) + level0;
	}
	else // x >= x1
	{
		double tmp;
		if (cache) // if cache is present, it is valid by now
			tmp = level0 + dlevel1 * cache[0];
		else
			tmp = level0 + dlevel1 * CON1_dHm(pars);

		if (dlevel2)
			tmp -=
			   dlevel2 *
			   (1. - exp(-(x - x1) / sigma2_connector));

		tmp -= tgFit * (x - x1);

		return tmp;
	}
}

static double f_CON1e(double *pars, int, double x, double *cache, int valid)
{
	// лин. параметры
	double level0 = pars[0]; // == a1
	double dlevel1 = pars[1];// == aLet
	double a2 = pars[2];

	// нел. пар-ры
	double x0 = pars[3];
	double x1 = pars[4];
	double sigma1_connector = pars[5];
	double b2 = pars[6];
	double ad = pars[7];

	double width_connector  = x1 - x0;

	if (cache && !valid)
	{
		cache[0] = CON1_dHm(pars);
		cache[1] = pow(10.0, -b2 / 5.0) - 1.0;
	}

	// XXX: ?: <=x / <x< / x>=

	if (x < x1)
	{
		if (x <= x0)
			return level0;

		// x0 < x < x1

		if (dlevel1 == 0)
			return level0;
		if ((x - x0) > BIG_EXP * sigma1_connector) // XXX
			return dlevel1 + level0;
		else
			return dlevel1 * (1. - exp(-(x - x0) / sigma1_connector)) + level0;
	}
	else // x >= x1
	{
		double tmp;
		if (cache) // if cache is present, it is valid by now
			tmp = level0 + dlevel1 * cache[0];
		else
			tmp = level0 + dlevel1 * CON1_dHm(pars);

		double c1 = cache
			? cache[1]
			: pow(10.0, -b2 / 5.0) - 1.0;

		double dx = x - x1;

		return tmp + a2 * dx + b2 + 5.0 * log10(1.0 + c1 * pow(10.0, ad * dx));
	}
}

// описание ограничений

// Структура par_min предназначена для описания простых ограничений.
// В ней - не все ограничения, т.к. есть и сложные, напр., par[3] <= par[4].
struct par_lim
{
	int ipar; // value -1 is signature to end
	double vmin;
	double vmax;
};

// специальное значение для полей vmin, vmax par_lim,
// обозначает отсутствие ограничение.
// Должно иметь точное представление в двоичной арифметике FPU
// (достаточно быть целым)
const double pl_ndf = 12345;

// Ограничения для коннектора

const double CON1_tauFit_tau2_prefit_ration = 1.2;

par_lim pl_CON1c_pre[] = // дополнительные ограничения для режима PREFIT
{
	{5, 0.5, 0.5e+4},	// front tau
	{6, 0.5, 0.499e+4 / CON1_tauFit_tau2_prefit_ration }, // tail tau2
	{7, 0.5, 0.5e+4},	// tail tauFit
	{-1, 0, 0}
};

par_lim pl_CON1c_fin[] = // ограничения общие для режимов FINFIT и PREFIT
{
	{5, 0.1, 1.0e+7},	// front tau -- XXX: 0.1 ?
	{6, 0.1, 1.0e+7},	// tail tau2
	{7, 0.1, 1.0e+7},	// tail tauFit
	{8, 0.0, 1.0},		// k
	{-1, 0, 0}
};

par_lim pl_CON1d_pre[] = // дополнительные ограничения для режима PREFIT
{
	{5, 0.5, 0.5e+4},	// front tau
	{6, 0.5, 0.499e+4 / CON1_tauFit_tau2_prefit_ration }, // tail tau2
	{-1, 0, 0}
};

par_lim pl_CON1d_fin[] = // ограничения общие для режимов FINFIT и PREFIT
{
	{5, 0.1, 1.0e+7},	// front tau -- XXX: 0.1 ?
	{6, 0.1, 1.0e+7},	// tail tau2
	{-1, 0, 0}
};

par_lim pl_CON1e_pre[] = // дополнительные ограничения для режима PREFIT
{
	{-1, 0, 0}
};

par_lim pl_CON1e_fin[] = // ограничения общие для режимов FINFIT и PREFIT
{
	{5, 0.5, 0.5e+4},	// front tau
	//{6,-1e3, 0.0},		// b2 должно быть отрицательным
	{7, pl_ndf, 0.0},	// ad должно быть отрицаетльным
	{-1, 0, 0}
};

// Наложить на параметры простые ограничения par_lim
// update = 0: только проверить соответствие ограничениям
// update != 0: скорректировать параметры так, чтобы удовлетворяли
// Возвращает
//    0: параметры находятся в указанных пределах
//   !=0: параметры выходят за пределы / параметры были скорректированы
static int impose_simple_limits(double *pars, par_lim *pll, int update)
{
	int rc = 0;
	while (pll->ipar >= 0)
	{
		if (pll->vmin != pl_ndf && pars[pll->ipar] < pll->vmin)
		{
			rc = 1;
			if (update)
				pars[pll->ipar] = pll->vmin;
		}
		if (pll->vmin != pl_ndf && pars[pll->ipar] > pll->vmax)
		{
			rc = 1;
			if (update)
				pars[pll->ipar] = pll->vmax;
		}
		pll++;
	}
	return rc;
}

// проверяем/исправляем X-упорядоченность
static int impose_x_ordering(double &pL, double &pR, int update)
{
	int rc = 0;
	if (pL > pR)
	{
		rc = 1;
		if (update)
		{
			double tmp = pR;
			pR = pL;
			pL = tmp;
		}
	}
	return rc;
}

static void ACXL_fc_CON1(double *pars, ACXL_data *ACXL)
{
	pars[0] += ACXL->dA;
	pars[1] += ACXL->dX;
	pars[2] -= ACXL->dX * CON1_dHm(pars);
	pars[3] += ACXL->dC - ACXL->dL;
	pars[4] += ACXL->dC + ACXL->dL;
}

static int command_requires_update(int command)
{
	switch (command)
	{
	case MF_CMD_FIX_RANGE_PREFIT:
	case MF_CMD_FIX_RANGE_FINFIT:
	case MF_CMD_ACXL_CHANGE:
		return 1;
	default:
		return 0;
	}
}

// Note: if update is requested,
// then the parameter set returned *must* satisfy to the limits,
// no matter if PREFIT or FINFIT is the case.
// This must be in mind when coding complicated limits like
// a7 > a6*1.2 && a6 < AMAX && a7 < AMAX.
// To avoid the collision like between a7:=a6*1.2 and a7:=AMAX
// (that would occur when a6==AMAX),
// we use different a6 and a7 AMAX values, see pl_CON1c_pre.

// XXX: for reliable software,
// the code should not fail if the problem above would come.

static double fc_CON1c(double *pars, ModelF &, int command, void *extra)
{
	int update = command_requires_update(command);
	int rc = 0;

	switch (command)
	{
	case MF_CMD_CHECK_RANGE_PREFIT:
	case MF_CMD_FIX_RANGE_PREFIT:

		if (impose_simple_limits(pars, pl_CON1c_pre, update))
			rc = 1;

		if (pars[7] < pars[6] * CON1_tauFit_tau2_prefit_ration)	// tail / tail2 poor separation
		{
			rc = 1;
			if (update)
				pars[7] = pars[6] * (CON1_tauFit_tau2_prefit_ration + 1e-3); // note: not the best fix
		}

		// fall through; no 'break'

	case MF_CMD_CHECK_RANGE_FINFIT:

		if (impose_x_ordering(pars[3], pars[4], update))
			rc = 1;

		if (impose_simple_limits(pars, pl_CON1c_fin, update))
			rc = 1;

		return rc;

	case MF_CMD_ACXL_CHANGE:
		ACXL_fc_CON1(pars, (ACXL_data *)extra);
		return 0;

	default:
		return rc;
	}
}

static double fc_CON1d(double *pars, ModelF &, int command, void *extra)
{
	int update = command_requires_update(command);
	int rc = 0;

	switch (command)
	{
	case MF_CMD_CHECK_RANGE_PREFIT:
	case MF_CMD_FIX_RANGE_PREFIT:

		if (impose_simple_limits(pars, pl_CON1d_pre, update))
			rc = 1;

		// fall through; no 'break'

	case MF_CMD_CHECK_RANGE_FINFIT:

		if (impose_x_ordering(pars[3], pars[4], update))
			rc = 1;

		if (impose_simple_limits(pars, pl_CON1d_fin, update))
			rc = 1;

		return rc;

	case MF_CMD_ACXL_CHANGE:
		ACXL_fc_CON1(pars, (ACXL_data *)extra);
		return 0;

	default:
		return rc;
	}
}

static double fc_CON1e(double *pars, ModelF &, int command, void *extra)
{
	int update = command_requires_update(command);
	int rc = 0;

	switch (command)
	{
	case MF_CMD_CHECK_RANGE_PREFIT:
	case MF_CMD_FIX_RANGE_PREFIT:

		if (impose_simple_limits(pars, pl_CON1e_pre, update))
			rc = 1;

		// fall through; no 'break'

	case MF_CMD_CHECK_RANGE_FINFIT:

		if (impose_x_ordering(pars[3], pars[4], update))
			rc = 1;

		if (impose_simple_limits(pars, pl_CON1e_fin, update))
			rc = 1;

		return rc;

	case MF_CMD_ACXL_CHANGE:
		ACXL_fc_CON1(pars, (ACXL_data *)extra);
		return 0;

	default:
		return rc;
	}
}

static double fc_LIN0(double *pars, ModelF &, int command, void *extra)
{
	if (command == MF_CMD_ACXL_CHANGE)
		pars[0] += ((ACXL_data *)extra)->dA;

	if (command == MF_CMD_LIN_SET_BY_X1Y1X2Y2)
	{
		double *args = (double *)extra;
		double x1 = args[0];
		double y1 = args[1];
		double x2 = args[2];
		double y2 = args[3];
		if (x1 == x2)
		{
			pars[0] = (y1 + y2) / 2.0;
			pars[1] = 0.0;
		}
		else
		{
			pars[1] = (y2 - y1) / (x2 - x1);
			pars[0] = y1 - x1 * pars[1];
		}
	}

	return 0;
}

static double fc_SPL1(double *pars, ModelF &, int command, void *extra)
{
	if (command == MF_CMD_ACXL_CHANGE)
		pars[0] += ((ACXL_data *)extra)->dA;
	return 0;
}

// описания аттрибутов

static double a_zero(double *, int)
{	return 0;
}
static double a_fpos_SPL1(double *p, int)
{	return p[3];
}
static double a_width_SPL1(double *p, int)
{	return p[4];
}
static double a_weldstep_SPL1(double *p, int)
{	return p[2];
}

static double a_fpos_CON1cde(double *p, int)
{	return p[3];
}
static double a_width_CON1cde(double *p, int)
{	return p[4] - p[3];
}
static double a_fheight_CON1cde(double *p, int)
{	return p[1]; // XXX: not a front height but aLet that sometimes may be much bigger
}

static double a_canLeftLink_true(double *, int)
{
	return 1.0;
}

static double f_GAUSS(double *pars, int, double x, double *, int)
{
	double A = pars[0];
	double x0 = pars[1];
	double sigma = pars[2];
	if (sigma == 0)
		return 0;
	double frac = (x - x0) / sigma;
	return A * exp(-0.5 * frac * frac);
}

static const char *an_fPos =		"fPos";
static const char *an_width =		"width";
static const char *an_weldStep =	"weldStep";
static const char *an_fHeight =		"fHeight";
static const char *an_noiseSuppressionLength = "noiseSuppressionLength";
static const char *an_canLeftLink = "canLeftLink";

// главная таблица модельных функций с фикс. числом параметров

// entry = -1: reserved,
// entry = 0: первая функция в списке

// ID = MF_ID_INVALID = 0: reserved

static MF_MD funcs[] =
{
	{
		MF_ID_LIN,
		2,
		"LL",
		f_LIN0, 0,
		fc_LIN0
	},

	{
		MF_ID_SPL1,
		5,
		"LLLXW", // XXX: '.' just reminds to rewrite (center,width)(..) to (x0,x1)(XX)
		f_SPL1, 0,
		fc_SPL1,
		{
			{ an_fPos, a_fpos_SPL1 },
			{ an_width, a_width_SPL1 },
			{ an_weldStep, a_weldstep_SPL1 }
		}
	},

	{
		MF_ID_CON1c,
		9,
		"LLLXXWWW.",
		f_CON1c, 0,
		fc_CON1c,
		{
			{ an_fPos, a_fpos_CON1cde },
			{ an_width, a_width_CON1cde },
			{ an_fHeight, a_fheight_CON1cde }
		}
	},

	{
		MF_ID_CON1d,
		8,
		"LLLXXWWL",
		f_CON1d, 0,
		fc_CON1d,
		{
			{ an_fPos, a_fpos_CON1cde },
			{ an_width, a_width_CON1cde },
			{ an_fHeight, a_fheight_CON1cde }
		}
	},

	{
		MF_ID_CON1e,
		8,
		"LLLXXW..",
		f_CON1e, 0,
		fc_CON1e,
		{
			{ an_fPos, a_fpos_CON1cde },
			{ an_width, a_width_CON1cde },
			{ an_fHeight, a_fheight_CON1cde }
		}
	},

	{
		MF_ID_BREAKL,
		0, // переменное число параметров
		0, // сигнатуры нет
		f_BREAKL, farr_BREAKL, // функция
		fc_BREAKL, // обработчик команд
		{
			{ an_noiseSuppressionLength, a_noiseSuppressionLength_BREAKL },
			{ an_canLeftLink, a_canLeftLink_true }
		}
	},

	{	// гауссиана
		MF_ID_GAUSS,
		3,
		"LXW",
		f_GAUSS, 0
	},

	{
		MF_ID_INVALID
	}
};

