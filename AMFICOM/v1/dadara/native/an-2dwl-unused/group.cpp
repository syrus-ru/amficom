#include <math.h>
#include <stdio.h>
#include <assert.h>

#include "Analyse.h"
#include "group.h"

// группировка событий;
// на вход события должны поступать слева направо, на выходе события не отсортированы

inline int iabs(int x)
{
	return x >= 0 ? x : -x;
}
inline int imax(int a, int b)
{
	return a > b ? a : b;
}
inline int imin(int a, int b)
{
	return a < b ? a : b;
}
inline void imineq(int &a, int b)
{
	if (b < a)
		a = b;
}
inline void imaxeq(int &a, int b)
{
	if (b > a)
		a = b;
}

class GenAnalysisParams // общие параметры анализа, задаваемые извне
{
private:
	double pulseWidth; // ширина а.ф., отсчеты - основана на шир. импульса
	double afterSoundEnd; // конец звона, отсчеты от фронта
	int maxCatch; // макс. ширина связывания

public:
	GenAnalysisParams(
		double xRes_m,
		double pulseWidth_m)
	{
		// длительность импульса
		pulseWidth = pulseWidth_m / xRes_m;
		// макс. длительность звона
		afterSoundEnd = 300.0 / xRes_m; // XXX: 300 м - длительность звона, эхо и добавки к затягиванию спада
		maxCatch = imax(
			(int )(pulseWidth * 7.0) + 3, // XXX: параметры
			(int )(afterSoundEnd + pulseWidth) + 1
		);
	}

	// прямое влияние A -> B
	int fwdAffect(int xAB, int sA, int sB, int big)
	{
		assert(xAB >= 0);
		int ss = (sA + sB + 2);
		return
			big
			? xAB <= maxCatch + ss
			: xAB <= pulseWidth + ss / 2.0 || xAB <= pulseWidth * 1.2; // XXX: параметры
	}

	// обратное влияние B <- A (справа налево)
	int backAffect(int xAB, int sA, int sB, int big)
	{
		assert(xAB <= 0);
		int ss = (sA + sB + 2);
		return
			big
			? -xAB <= pulseWidth + ss / 2.0
			: 0;
	}

	int likeReflPair(int xLR, int sL, int sR, int big)
	{
		if (xLR < 0)
			return 0;
		int dx = xLR;
		int ss = (sL + sR + 1) / 2;
		fprintf(stderr, "dx %d pw %g mc %d ss %d\n",
			dx, pulseWidth, maxCatch, ss);
		return
			big 
			? dx > pulseWidth - ss && dx < maxCatch + ss
			: dx > pulseWidth - ss && dx < pulseWidth + ss;
	}

	double getCritConnectorValue()
	{
		return 0.2; // FIXIT - should be taken from GUI
	}

	double getCritEndOfFiber()
	{
		return 3.0; // FIXIT - should be taken from GUI
	}
};

const double BIG_EVENT = 1.5; // dB; XXX
const int eventCompress = 2; // XXX; 2..3; 3 => begin коннектора начнется точно на фронте
const int spliceCompress = 3; // XXX
const int allowNearConnectors = 1;


GenAnalysisParams my_ga(8.0, 250.0); // FIXIT

const int COMPLICATE_WORK_FOR_OTHERS = 1; // XXX: должно быть 0

// интерфейс
class GroupRecognizer
{
public:
	// порядок перечисления методов соответствуют порядку их вызова

	// вначале указывается опорное событие
	//GroupRecognizer(InEvent &ref, GenAnalysisParams &gap);

	// затем в цикле (БЕЗ ПОВТОРЕНИЙ!) предъявляются все остальные события
	// (предполагается, что распознаватель не будет запоминать эти объекты,
	// а только будет помечать у себя флажками то, что ему нужно обязательно, и
	// то, что отвергает гипотезу о наличии события в окрестности ref-события).
	// return code != 0: hint: данные больше не нужны (напр., событие заведомо отвергнуто)
	virtual int see(InEvent &be) = 0;

	// когда события закончатся, спрашивается - похоже или нет, ответ - вероятность 0..1
	// (способ интерпретации чисел  0<x<1 пока не определен)
	// повторные вызовы этой функции должны возвращать то же значение,
	// если только между ними не было вызова see(). Если see() был вызван
	// после recognize(), то результат не определен.
	virtual double recognize() = 0;

	// после recognize() *можно* спросить вероятность того, что данное событие be
	// принадлежит этой группе (т.е. условная вероятность)
	// эта функция уже не изменяет состояние разпознавателя
	virtual double has(InEvent &be) = 0;

	// поигрались - отдали
	virtual ~GroupRecognizer() {};
};

// абстрактный класс
// UnknownRecognizer - умеет лишь узнать, какие события близки к данному
class UnknownRecognizer : public GroupRecognizer
{
protected:
	GenAnalysisParams &gap;
	InEvent ref;
	int begin; // здесь будем запоминать границы найденных событий, приписанных к данной группе
	int end;
protected:

	int scalesTo(InEvent &be) // опорное событие покрывает центр be
	{
		return iabs(be.x0 - ref.x0) < ref.scale;
	}
	int rangesTo(InEvent &be) // группа покрывает центр be (XXX)
	{
		return be.x0 >= begin && be.x0 <= end; // XXX? be.x0 - be.scale >= begin && ... ?
	}

	// влияние БС A на БС B, с учетом амплитуд
	static int affect(InEvent &A, InEvent &B, GenAnalysisParams &gap)
	{
		return B.x0 >= A.x0
			?
				fabs(A.value) > fabs(B.value) * 0.3 // XXX: параметр
				&& gap.fwdAffect(B.x0 - A.x0, A.scale, B.scale, A.value > BIG_EVENT)
			:
				fabs(A.value) > fabs(B.value) * 20 // XXX: параметр, <30 .. <150 ?
				&& gap.backAffect(B.x0 - A.x0, A.scale, B.scale, A.value > BIG_EVENT);
	}
	int affectedBy(InEvent &be) // событие be влияет на распознавание нашей группы
	{
		return affect(be, ref, gap);
	}
	int affects(InEvent &be) // опорное событие нашей группы влияет на be
	{
		return affect(ref, be, gap);
	}
	void updateRange(InEvent &be)
	{
		imineq(begin, be.x0 - be.scale / 1); // XXX
		imaxeq(end, be.x0 + be.scale / 1); // XXX
	}

public:
	UnknownRecognizer(InEvent &ref_, GenAnalysisParams &gap_)
		: gap(gap_), ref(ref_)
	{
		int compress = eventCompress; // XXX
		begin = ref.x0 - ref.scale / compress;
		end = ref.x0 + ref.scale / compress;
	} // empty

	virtual ~UnknownRecognizer() {} // empty
	
	virtual int see(InEvent &be)
	{
		//if (isNear(be))
		//	updateRange(be);
		return 0;
	}
	
	virtual double recognize()
	{
		return 0.01; // XXX
	}
	
	virtual OutEvent &createOutEvent() // если recognize() вернул строго 0, результат не определен
	{
		OutEvent &oe = *new OutEvent();
		oe.oet = OET_UNKNOWN;
		oe.begin = ref.x0 - ref.scale;
		oe.end = ref.x0 + ref.scale;
		oe.front = oe.begin;
		oe.tail = oe.end;
		return oe;
	}
	
	virtual double has(InEvent &be)
	{
		return rangesTo(be);
	}
};

// SpliceRecognizer - узнает факт отсутствия больших посторонних событий в окрестности опорного

class SpliceRecognizer : public UnknownRecognizer
{
private:
	int falsificated;
	const int outACompress;
	const int outBCompress;

public:
	SpliceRecognizer(InEvent &ref, GenAnalysisParams &gap)
		: UnknownRecognizer(ref, gap),
		outACompress(spliceCompress), // XXX
		outBCompress(spliceCompress) // XXX
	{
		falsificated = ref.special;
	}

	~SpliceRecognizer() {}

	int see(InEvent &be)
	{
		if
		(
			!falsificated
			&& affectedBy(be)
			/*
			&& fabs(be.value) > 0.3 * absAmpl	// заметное событие, мешающее распознанию // XXX: параметр
			&& mutualAffect(be) // близкое расположение (учитывается isBig)
			&& !
			(	// если событие справа и значительно больше - то оно не связано с нашим // XXX: параметр
				fabs(be.value) > 3 * absAmpl
				&& be.x0 > x0
			)
			*/
				// TODO: увидев большое событие, надо отсечь все еще более правые события(??) пройтись заново? запоминать min/max координаты?
		)
		falsificated = 1;
		return falsificated;
	}

	double recognize()
	{
		return falsificated ? 0.0 : 0.9;
	}

	OutEvent &createOutEvent()
	{
		OutEvent &oe = *new OutEvent();
		oe.oet = OET_SPLICE;
		oe.begin = ref.x0 - ref.scale / outACompress;
		oe.end   = ref.x0 + ref.scale / outACompress;
		oe.front = ref.x0 - ref.scale / outBCompress;
		oe.tail  = ref.x0 + ref.scale / outBCompress;
		return oe;
	}
	//double has(InEvent &be) -- наследуем от UnknownRecognizer'а
};

// GenConnectorRecognizer - определяет основные свойства коннектора

#define CR_FALSE 1	// это уже заведомо не коннектор
#define CR_HAS_TAIL 2 // спад найден
#define CR_HAS_ANTITAIL 4 // артефакт положительного знака в конце коннектора найден (и больше не допускается)
#define CR_TAIL_COMPLETED 8 // и больше не допускается
#define CR_LIMITED 16 // к группе нельзя добавлять события справа, т.к. далее ожидается другая группа

class GenConnectorRecognizer : public UnknownRecognizer
{
protected:
	int flags;
	double tailRatio;
	int x_to; // положение соотв. спада

public:
	GenConnectorRecognizer(InEvent &ref, GenAnalysisParams &gap)
		: UnknownRecognizer(ref, gap)
	{
		flags = 0;
		tailRatio = 0;
		if (ref.value < 0) // ampl м б только положительна
			flags = CR_FALSE;
		x_to = ref.x0; // потом будет увеличиваться
		end = ref.x0 + ref.scale / 2; // здесь будем вычислять положение конца события // XXX
	}

	virtual ~GenConnectorRecognizer() {}

	virtual int see(InEvent &be)
	{
		int rc = see0(be);
		if(0) fprintf(stderr,
			"# connector's see: be(%g at %d:%d) -- ref(%g at %d:%d); tailRation %g flags %d; rc %d\n",
			be.value, be.x0, be.scale,
			ref.value, ref.x0, ref.scale,
			tailRatio, flags, rc);
		return rc;
	}
	int see0(InEvent&be)
	{
		if (flags & CR_FALSE)
			return 1;

		if (!affectedBy(be) && !affects(be)) // событие далеко - пропускаем
			return 0;

		if (allowNearConnectors) // допускать ли близкие коннекторы
		{
			if (be.x0 > ref.x0 && flags & CR_HAS_TAIL && be.value / ref.value >= 0.2 && end < be.x0 - be.scale) // XXX: параметр // Note: используется порядок прохода слева направо
			{
				flags |= CR_LIMITED;
				return 0;
			}
		}

		if (flags & CR_LIMITED && !rangesTo(be)) 
			return 0;

		if (fabs(be.value) / ref.value > 1.5) // XXX: параметр // данное событие заглушает наше
		{
			flags = CR_FALSE;
			return 1;
		}

		if (fabs(be.value) / ref.value < 0.2)  // XXX: параметр // видно маленькое событие
		{
			if (be.x0 > ref.x0)
			{
				// наверное звон - надо расширить границы
				updateRange(be);
			}
			return 0;
		}

		if (be.x0 < ref.x0 && fabs(be.value) / ref.value < 0.5) // XXX
			return 0; // что-то среднего размера перед коннектором - это другое событие

		// известно: события недалеки, амплитуда соизмерима:
		// если предъявленное событие - не спад для коннектора, то наша группа - не коннектор

		if (
			!testPair(be)
			|| be.x0 < ref.x0
			)
		{
			flags = CR_FALSE;
			return 1;
		}

		if ( // antitail
			be.value / ref.value > 0
			&& !(flags & CR_HAS_ANTITAIL)
			&& (flags & CR_HAS_TAIL)
			)
		{
			imaxeq(x_to, be.x0);
			updateRange(be);
			flags |= CR_HAS_ANTITAIL;
			flags |= CR_TAIL_COMPLETED;
			return 0;
		}
		if ( // tail
			!(flags & CR_TAIL_COMPLETED) // этот флаг пока всегда равен 0
			&& be.value / ref.value < 0
			)
		{
			imaxeq (x_to, be.x0);
			updateRange(be);
			tailRatio += fabs(be.value) / ref.value;
			if (tailRatio > 0.3)	// XXX: параметр
				flags |= CR_HAS_TAIL;
			return 0;
		}

		// 
		flags = CR_FALSE;
		return 1;
	}

	virtual double recognize()
	{
		double rc = 0;
		if (flags & CR_HAS_TAIL && !(flags & CR_FALSE))
			rc = ref.value > gap.getCritConnectorValue()
				? 0.95 //XXX
				: 0.8; //XXX
		if (rc)
		{
			fprintf(stderr,
				"# connector recognized: ref(%g at %d:%d); tailRation %g flags %d\n",
				ref.value, ref.x0, ref.scale, tailRatio, flags);
		}
		return rc;
	}

	virtual OutEvent &createOutEvent()
	{
		OutEvent &oe = *new OutEvent();
		oe.oet = ref.value > gap.getCritEndOfFiber()
			? OET_CONNECTOR_OR_EOF
			: OET_CONNECTOR_NOT_EOF;
		oe.begin = begin;
		oe.end = end;
		oe.front = ref.x0;
		oe.tail = x_to;	// определено только при flags & CR_COUPLED
		return oe;
	}

	virtual int testPair(InEvent &be)
	{
		int rc = gap.likeReflPair(be.x0 - ref.x0, ref.scale, be.scale, ref.value > BIG_EVENT);
		return rc;
	}
	//double has(InEvent &be) -- наследуем от UnknownRecognizer'а
};

// AConnectorRecognizer - узнает коннекторы

class AConnectorRecognizer : public GenConnectorRecognizer
{
public:
	AConnectorRecognizer(InEvent &ref, GenAnalysisParams &gap)
		: GenConnectorRecognizer(ref, gap)
	{
	}

	~AConnectorRecognizer() {}
};

// DeadZoneRecognizer - опознает мертвую зону

class DeadZoneRecognizer : public GenConnectorRecognizer
{
public:
	DeadZoneRecognizer(InEvent &ref, GenAnalysisParams &gap)
		: GenConnectorRecognizer(ref, gap)
	{
		if (ref.x0 != 0)
			flags = CR_FALSE;
		else
			flags = CR_HAS_TAIL;
	}

	~DeadZoneRecognizer() {}

	virtual int see(InEvent &be) // overrides GenConnectorRecognizer::see()
	{
		if (flags & CR_FALSE)
			return 1;

		//if (!mutualAffect(be)) // событие далеко - пропускаем
		//	return 0;
		if (!affectedBy(be) && !affects(be)) // событие далеко - пропускаем
			return 0;

		// какое бы событие ни было - связываем его с м.з.
		updateRange(be);
		return 0;
	}
};

// результат зависит от сортировки событий на входе
// сортировка на входе должна идти по возрастанию координаты
// (можно попробовать другой вариант - убывание амплитуды)
// на выходе результат не сортирован, надо отсортировать
void group_events(InEvent *ie, int len, ArrList &outEvents)
{
	int i;
	// печатаем события
	FILE *fout = stderr;
	fprintf(fout, "--- group_events ---\n");	
	for (i = 0; i < len; i++)
		fprintf(fout, "InEvent[%d]: x0 %d scale %d value %g\n",
		  i,
		  ie[i].x0,
		  ie[i].scale,
		  ie[i].value);

	int pass;
	const int HIDE_DEBUG = 0;
	// анализируем
	// pass0: выводим на экран - только для отладки
	// pass1: принимаем решения, вычеркиваем события с помощью value := 0
	for (pass = !!HIDE_DEBUG; pass < 2; pass++)
	{
		for (i = 0; i < len; i++)
		{
			int j;

			if (ie[i].value == 0)
				continue;

			const int NR = 3;
			SpliceRecognizer r1 (ie[i], my_ga);
			AConnectorRecognizer r2 (ie[i], my_ga);
			DeadZoneRecognizer r3 (ie[i], my_ga);

			UnknownRecognizer *ur[NR] = { &r1, &r2, &r3 };
			char *rNames[NR] = { "Splice", "AConn", "BConn" };

			int rn;
			double p[NR];
			int nhits = 0;
			{
				for (rn = 0; rn < NR; rn++)
				{
					for (j = 0; j < len; j++)
					{
						if (j != i && ie[j].value)
						{
							if (ur[rn]->see(ie[j]))
								break;
						}
					}
					p[rn] = ur[rn]->recognize();
					if (p[rn])
						nhits++;
				}
			}

			if (pass == 0)
			{
				if (nhits > 1)
					fprintf(fout, "Found[%d]: %g:%g:%g -- MULTIHIT\n", i, p[0], p[1], p[2]);

				for (rn = 0; rn < NR; rn++)
				{
					if (p[rn])
					{
						fprintf(fout, "Found[%d]: %g:%g:%g:", i, p[0], p[1], p[2]);
						for (j = 0; j < len; j++)
							if (ur[rn]->has(ie[j]))
								fprintf(fout, " %d", j);
						fprintf(fout, "\n");
					}
				}
			}
			else
			{
				// pass != 0
				if (nhits)
				{
					// выбираем самую правдоподобную гипотезу
					int id = 0;
					double val = 0;
					double sval = 0;
					for (rn = 0; rn < NR; rn++)
					{
						sval += p[rn];
						if (p[rn] > val)
						{
							val = p[rn];
							id = rn;
						}
					}
					assert(val); // XXX

					// печатаем
					int sure = p[id] > sval * 0.50; // XXX
					fprintf(fout, "%s%c[%d]: %g:%g:%g:", rNames[id], sure ? '+' : '?', i, p[0], p[1], p[2]);
					for (j = 0; j < len; j++)
					{
						if (j == i || ie[j].value && ur[id]->has(ie[j]))
						{
							fprintf(fout, " %d", j);
							ie[j].value = 0;
						}
					}
					fprintf(fout, "\n");

					// добавляем событие
					OutEvent &oe = ur[id]->createOutEvent();
					if (!sure)
						oe.oet = OET_UNKNOWN;
					outEvents.add(&oe);
				}
				else
				{
					// ничего не делаем
				}
			}
		}
	}

	// перечисляем неопознанные события (а надо бы их еще сгруппировать)
	for (i = 0; i < len; i++)
	{
		if (ie[i].value != 0)
		{
			fprintf(fout, "Unknown[%d]\n", i);
			UnknownRecognizer r0(ie[i], my_ga);
			outEvents.add(&r0.UnknownRecognizer::createOutEvent());
		}
	}
	fprintf(fout, "\n");
	fflush(fout);
}
