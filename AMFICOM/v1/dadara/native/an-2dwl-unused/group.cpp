#include <math.h>
#include <stdio.h>
#include <assert.h>

#include "Analyse.h"
#include "group.h"

// ����������� �������;
// �� ���� ������� ������ ��������� ����� �������, �� ������ ������� �� �������������

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

class GenAnalysisParams // ����� ��������� �������, ���������� �����
{
private:
	double pulseWidth; // ������ �.�., ������� - �������� �� ���. ��������
	double afterSoundEnd; // ����� �����, ������� �� ������
	int maxCatch; // ����. ������ ����������

public:
	GenAnalysisParams(
		double xRes_m,
		double pulseWidth_m)
	{
		// ������������ ��������
		pulseWidth = pulseWidth_m / xRes_m;
		// ����. ������������ �����
		afterSoundEnd = 300.0 / xRes_m; // XXX: 300 � - ������������ �����, ��� � ������� � ����������� �����
		maxCatch = imax(
			(int )(pulseWidth * 7.0) + 3, // XXX: ���������
			(int )(afterSoundEnd + pulseWidth) + 1
		);
	}

	// ������ ������� A -> B
	int fwdAffect(int xAB, int sA, int sB, int big)
	{
		assert(xAB >= 0);
		int ss = (sA + sB + 2);
		return
			big
			? xAB <= maxCatch + ss
			: xAB <= pulseWidth + ss / 2.0 || xAB <= pulseWidth * 1.2; // XXX: ���������
	}

	// �������� ������� B <- A (������ ������)
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
const int eventCompress = 2; // XXX; 2..3; 3 => begin ���������� �������� ����� �� ������
const int spliceCompress = 3; // XXX
const int allowNearConnectors = 1;


GenAnalysisParams my_ga(8.0, 250.0); // FIXIT

const int COMPLICATE_WORK_FOR_OTHERS = 1; // XXX: ������ ���� 0

// ���������
class GroupRecognizer
{
public:
	// ������� ������������ ������� ������������� ������� �� ������

	// ������� ����������� ������� �������
	//GroupRecognizer(InEvent &ref, GenAnalysisParams &gap);

	// ����� � ����� (��� ����������!) ������������� ��� ��������� �������
	// (��������������, ��� �������������� �� ����� ���������� ��� �������,
	// � ������ ����� �������� � ���� �������� ��, ��� ��� ����� �����������, �
	// ��, ��� ��������� �������� � ������� ������� � ����������� ref-�������).
	// return code != 0: hint: ������ ������ �� ����� (����., ������� �������� ����������)
	virtual int see(InEvent &be) = 0;

	// ����� ������� ����������, ������������ - ������ ��� ���, ����� - ����������� 0..1
	// (������ ������������� �����  0<x<1 ���� �� ���������)
	// ��������� ������ ���� ������� ������ ���������� �� �� ��������,
	// ���� ������ ����� ���� �� ���� ������ see(). ���� see() ��� ������
	// ����� recognize(), �� ��������� �� ���������.
	virtual double recognize() = 0;

	// ����� recognize() *�����* �������� ����������� ����, ��� ������ ������� be
	// ����������� ���� ������ (�.�. �������� �����������)
	// ��� ������� ��� �� �������� ��������� ��������������
	virtual double has(InEvent &be) = 0;

	// ���������� - ������
	virtual ~GroupRecognizer() {};
};

// ����������� �����
// UnknownRecognizer - ����� ���� ������, ����� ������� ������ � �������
class UnknownRecognizer : public GroupRecognizer
{
protected:
	GenAnalysisParams &gap;
	InEvent ref;
	int begin; // ����� ����� ���������� ������� ��������� �������, ����������� � ������ ������
	int end;
protected:

	int scalesTo(InEvent &be) // ������� ������� ��������� ����� be
	{
		return iabs(be.x0 - ref.x0) < ref.scale;
	}
	int rangesTo(InEvent &be) // ������ ��������� ����� be (XXX)
	{
		return be.x0 >= begin && be.x0 <= end; // XXX? be.x0 - be.scale >= begin && ... ?
	}

	// ������� �� A �� �� B, � ������ ��������
	static int affect(InEvent &A, InEvent &B, GenAnalysisParams &gap)
	{
		return B.x0 >= A.x0
			?
				fabs(A.value) > fabs(B.value) * 0.3 // XXX: ��������
				&& gap.fwdAffect(B.x0 - A.x0, A.scale, B.scale, A.value > BIG_EVENT)
			:
				fabs(A.value) > fabs(B.value) * 20 // XXX: ��������, <30 .. <150 ?
				&& gap.backAffect(B.x0 - A.x0, A.scale, B.scale, A.value > BIG_EVENT);
	}
	int affectedBy(InEvent &be) // ������� be ������ �� ������������� ����� ������
	{
		return affect(be, ref, gap);
	}
	int affects(InEvent &be) // ������� ������� ����� ������ ������ �� be
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
	
	virtual OutEvent &createOutEvent() // ���� recognize() ������ ������ 0, ��������� �� ���������
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

// SpliceRecognizer - ������ ���� ���������� ������� ����������� ������� � ����������� ��������

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
			&& fabs(be.value) > 0.3 * absAmpl	// �������� �������, �������� ����������� // XXX: ��������
			&& mutualAffect(be) // ������� ������������ (����������� isBig)
			&& !
			(	// ���� ������� ������ � ����������� ������ - �� ��� �� ������� � ����� // XXX: ��������
				fabs(be.value) > 3 * absAmpl
				&& be.x0 > x0
			)
			*/
				// TODO: ������ ������� �������, ���� ������ ��� ��� ����� ������ �������(??) �������� ������? ���������� min/max ����������?
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
	//double has(InEvent &be) -- ��������� �� UnknownRecognizer'�
};

// GenConnectorRecognizer - ���������� �������� �������� ����������

#define CR_FALSE 1	// ��� ��� �������� �� ���������
#define CR_HAS_TAIL 2 // ���� ������
#define CR_HAS_ANTITAIL 4 // �������� �������������� ����� � ����� ���������� ������ (� ������ �� �����������)
#define CR_TAIL_COMPLETED 8 // � ������ �� �����������
#define CR_LIMITED 16 // � ������ ������ ��������� ������� ������, �.�. ����� ��������� ������ ������

class GenConnectorRecognizer : public UnknownRecognizer
{
protected:
	int flags;
	double tailRatio;
	int x_to; // ��������� �����. �����

public:
	GenConnectorRecognizer(InEvent &ref, GenAnalysisParams &gap)
		: UnknownRecognizer(ref, gap)
	{
		flags = 0;
		tailRatio = 0;
		if (ref.value < 0) // ampl � � ������ ������������
			flags = CR_FALSE;
		x_to = ref.x0; // ����� ����� �������������
		end = ref.x0 + ref.scale / 2; // ����� ����� ��������� ��������� ����� ������� // XXX
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

		if (!affectedBy(be) && !affects(be)) // ������� ������ - ����������
			return 0;

		if (allowNearConnectors) // ��������� �� ������� ����������
		{
			if (be.x0 > ref.x0 && flags & CR_HAS_TAIL && be.value / ref.value >= 0.2 && end < be.x0 - be.scale) // XXX: �������� // Note: ������������ ������� ������� ����� �������
			{
				flags |= CR_LIMITED;
				return 0;
			}
		}

		if (flags & CR_LIMITED && !rangesTo(be)) 
			return 0;

		if (fabs(be.value) / ref.value > 1.5) // XXX: �������� // ������ ������� ��������� ����
		{
			flags = CR_FALSE;
			return 1;
		}

		if (fabs(be.value) / ref.value < 0.2)  // XXX: �������� // ����� ��������� �������
		{
			if (be.x0 > ref.x0)
			{
				// �������� ���� - ���� ��������� �������
				updateRange(be);
			}
			return 0;
		}

		if (be.x0 < ref.x0 && fabs(be.value) / ref.value < 0.5) // XXX
			return 0; // ���-�� �������� ������� ����� ����������� - ��� ������ �������

		// ��������: ������� ��������, ��������� ����������:
		// ���� ������������� ������� - �� ���� ��� ����������, �� ���� ������ - �� ���������

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
			!(flags & CR_TAIL_COMPLETED) // ���� ���� ���� ������ ����� 0
			&& be.value / ref.value < 0
			)
		{
			imaxeq (x_to, be.x0);
			updateRange(be);
			tailRatio += fabs(be.value) / ref.value;
			if (tailRatio > 0.3)	// XXX: ��������
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
		oe.tail = x_to;	// ���������� ������ ��� flags & CR_COUPLED
		return oe;
	}

	virtual int testPair(InEvent &be)
	{
		int rc = gap.likeReflPair(be.x0 - ref.x0, ref.scale, be.scale, ref.value > BIG_EVENT);
		return rc;
	}
	//double has(InEvent &be) -- ��������� �� UnknownRecognizer'�
};

// AConnectorRecognizer - ������ ����������

class AConnectorRecognizer : public GenConnectorRecognizer
{
public:
	AConnectorRecognizer(InEvent &ref, GenAnalysisParams &gap)
		: GenConnectorRecognizer(ref, gap)
	{
	}

	~AConnectorRecognizer() {}
};

// DeadZoneRecognizer - �������� ������� ����

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

		//if (!mutualAffect(be)) // ������� ������ - ����������
		//	return 0;
		if (!affectedBy(be) && !affects(be)) // ������� ������ - ����������
			return 0;

		// ����� �� ������� �� ���� - ��������� ��� � �.�.
		updateRange(be);
		return 0;
	}
};

// ��������� ������� �� ���������� ������� �� �����
// ���������� �� ����� ������ ���� �� ����������� ����������
// (����� ����������� ������ ������� - �������� ���������)
// �� ������ ��������� �� ����������, ���� �������������
void group_events(InEvent *ie, int len, ArrList &outEvents)
{
	int i;
	// �������� �������
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
	// �����������
	// pass0: ������� �� ����� - ������ ��� �������
	// pass1: ��������� �������, ����������� ������� � ������� value := 0
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
					// �������� ����� �������������� ��������
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

					// ��������
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

					// ��������� �������
					OutEvent &oe = ur[id]->createOutEvent();
					if (!sure)
						oe.oet = OET_UNKNOWN;
					outEvents.add(&oe);
				}
				else
				{
					// ������ �� ������
				}
			}
		}
	}

	// ����������� ������������ ������� (� ���� �� �� ��� �������������)
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
