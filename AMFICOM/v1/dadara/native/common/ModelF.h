// FileName=ModelF.h TabSize=4 CP=1251

/*
	ModelF.h - "модельная функция".
  Модуль обеспечивает C++ интерфейс для класса ModelF
  и native-поддержку для Java-класса ModelFunction

  "Модельная функция" предназначена для хранения
  идентификатора функции профиля (int ID) и набора
  параметров для этой функции.

  Модуль предоставляет интерфейс для работы с этими функциями:
  - преобразование объекта между структурой C++ и объектом Java
  - расчет характеристик ("аттрибутов") модельной функции - фронт, ширина...
  - вычисление значения функции в произвольной точке

  В данном модуле пока не производятся:
  - выполнение процедуры фитировки к рефлектограмме:
		для этого требуется знание о положении начала и конца события (??)
  - создание нетривиальных объектов:
		для этого требуется специфическая информация
  Эти функции выполняются в других модулях, работающих с ModelF.

  Класс "Модельная функция" обеспечивает сокрытие данных на Java-уровне,
  но сокрытие на C-уровне не предусмотрено, т.к. ModelF - это по сути
  только структура.

*/

#ifndef _MODELF_H
#define _MODELF_H

// инициализация статических данных модуля ModelF (один раз при загрузке)
// в настоящей версии его вызывать не обязательно
void MF_init();

// идентификаторы (ID) используемых модельных функций

const int MF_ID_INVALID = 0;

const int MF_ID_LIN		= 21;

const int MF_ID_SPL1	= 12;
const int MF_ID_CON1c	= 13; // todo: rename
const int MF_ID_CON1d	= 14; // todo: rename
const int MF_ID_CON1e	= 15; // todo: rename

const int MF_ID_BREAKL	= 22;

const int MF_ID_GAUSS	= 16;

// получение характеристик параметров

const int MF_PAR_FLAG_X = 1;
const int MF_PAR_FLAG_W = 2;
const int MF_PAR_FLAG_L = 4;

// выполнение команд (процедур) над функциями

#define MF_CMD_CHECK_RANGE_PREFIT 1
#define MF_CMD_CHECK_RANGE_FINFIT 2
#define MF_CMD_FIX_RANGE_PREFIT 3
#define MF_CMD_FIX_RANGE_FINFIT 4
#define MF_CMD_ACXL_CHANGE 5
//#define MF_CMD_CHANGE_OTHER 6
#define MF_CMD_CHANGE_BY_THRESH 7 /* BREAKL only */
#define MF_CMD_FIX_THRESH 8 /* BREAKL only */

#define MF_CMD_LIN_SET_BY_X1Y1X2Y2 2101

// номера параметров наиболее типичных функций
// используется вне реализации ModelF
// внутри же номера прописаны жестко, в т.ч. в строке сигнатуры
const int MF_PARID_GAUSS_AMPLITUDE = 0;
const int MF_PARID_GAUSS_CENTER = 1;
const int MF_PARID_GAUSS_SIGMA = 2;

/*
 * MF_CMD_ACXL_CHANGE:
 * extra[0]: A - сдвиг вверх,
 * extra[1]: C - сдвиг вправо,
 * extra[2]: X - изменение полуширины относительно центра,
 * extra[3]: L - изменение амплитуры (размаха), относительно базового уроня.
 */

// profiling info - для отладки и пр. Может переполниться, это не страшно.
extern int total_RMS2_counter_nl;
extern int total_RMS2_counter_lin;

// макс. число параметров для функий с фиксированным числом параметров
const int MF_MAX_FIXED_PARS = 16;

class ModelF
{
private:
	static int initialized; // глобальные таблицы модуля инициализированы
	static void initStatic();

	int entry;
	int nPars; // фактическое число параметров
	double parsStorage[MF_MAX_FIXED_PARS]; // может использоваться для хранения параметров
	double *parsPtr; // указатель на фактическое размещение параметров

public:

	// конструктор по умолчанию. Создает пустой объект MF с isCorrect() == 0
	ModelF();

	// Аргумент npars используется только при создании/инициализации объекта
	// со свободным числом параметров. При некорректных npars,
	// реальное число параметров будет отличаться от переданного npars.

	// Чтобы убедиться в допустимости указанного ID,
	// после вызова конструктора необходимо проверить isCorrect()

	ModelF(int ID, int npars = 0);

	// Объект можно инициализировать несколько раз.
	// Если pars не указано, то ModelF сам размещает параметры,
	// возможно, в parsStorage, возможно, через new/delete.
	// Сами параметры не инициализируются.
	// Если же указан параметр pars, создается mf с уже готовыми параметрами
	// в указанном месте в свободной памяти, как будто ModelF сама получила
	// их через new double[]. Когда pars перестанет быть нужным ModelF,
	// она удалит его через delete[].

	void init(int ID, int npars = 0, double *pars = 0);

	// можно заменить весь массив параметров целиком.
	// При этом пользователь должен сам создать массив длины NPars,
	// а после операции замены относиться к нему как к полученному через
	// метод getP() - удалять его будет ModelF.
	void setP(double *pars);

	// обнулить значения параметров
	// XXX: быть может, это стоит делать вместе с init() ?

	void zeroPars();

	int isCorrect(); // объект создан корректно

	~ModelF();

	// если число параметров не фиксировано, возвращает 0,
	// если фиксировано, - положительное число, равное числу параметров
	int hasFixedNumberOfPars();

	int getID(); // == MF_ID_INVALID: объект создан некорректно

	int getNPars() // запросить фактическое число параметров
	{
		return nPars;
	}

	double *getP() // место хранения параметров
	{
		return parsPtr;
	}

	double &operator[] (int ipar)
	{
		return parsPtr[ipar];
	}

	// определение флагов параметра # ipar
	int getFlags(int ipar);

	// вычисление значения функции в точке
	double calcFunP(double *pars, double x);
	double calcFun(               double x)
	{
		return calcFunP(parsPtr, x);
	}

	// вычисление значения функции на сетке
	void calcFunArrayP(double *pars, double x0, double step, int N, double *output);
	void calcFunArray(               double x0, double step, int N, double *output)
	{
		calcFunArrayP(parsPtr, x0, step, N, output);
	}

	// расчет значение определенного аттрибута для данной кривой
	// если для данной кривой этот аттрибут не определен, возвращает default_value
	double getAttrP(double *pars, const char *name, double default_value);
	double getAttr(               const char *name, double default_value)
	{
		return getAttrP(parsPtr, name, default_value);
	}

	// выполнение команд.
	// Некоторые функции могут изменять число параметров,
	// для них можно использовать только execCmd.
	// Для остальных функций используются оба варианта.
	double execCmdP(double *pars, int command, void *extra = 0);
	double execCmd(               int command, void *extra = 0)
	{
		return execCmdP(parsPtr, command, extra);
	}

	// рассчитывает средний квадрат отклонения (RMS^2) между модельной
	// функцией ModelF и таблично заданной кривой
	// (это - самая критичная ко времени процедура)
	// Для ускорения, можно использовать rough = 1 или rough = 2
	double RMS2P(double *pars, double *y, int i0, int x0, int length, int rough = 0);
	double RMS2(               double *y, int i0, int x0, int length, int rough = 0)
	{
		return RMS2P(parsPtr, y, i0, x0, length, rough);
	}

	// фитировка линейных параметров (имеющих флаг PAR_FLAG_L) и возврат ср. квадрата отклонения.
	// для функций с переменным числом аргументов возвращает RMS^2
	// Лин. параметры в pars[] изменяются и заполняются расчетными значениями.
	// используется при фитировке - полной и линейной.
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

// преобразование ID <-> entry
//int MF_ID2entry(int ID);
//int MF_entry2ID(int entry);

// определение числа параметров
// return > 0: фиксированное число параметров
// return == 0: переменное число параметров
// return < 0: зарезервировано
//int MF_getNParsByEntry(int entry);

/*
// вычисление значения функции в точке
double MF_calc_fun(int entry, double *pars, double x);

// расчет среднего квадрата отклонения MF от кривой
double RMS(double *pars, int entry, double *y, int i0, int x0, int length, int rough = 0);

// фитировка линейных параметров (имеющих флаг PAR_FLAG_L) и возврат ср. квадрата отклонения.
// Лин. параметры в pars[] изменяются и заполняются расчетными значениями.
// используется при фитировке - полной и линейной.
double RMS_lin(double *pars, int entry, double *y, int i0, int x0, int length, int rough = 0);

// расчет аттрибутов для модельной функции
double getAttr(const char *name, int entry, double *pars, double default_value);
*/

#endif

