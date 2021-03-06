// EventParams (old) to EventP (new) converter.
// Used with InitialAnalysis

#include "EPold2EPnew.h"
#include "com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent.h"
#include "../common/assert1.h"

double fmin(double a, double b, double c)
{
	if (b < a)
		a = b;
	if (c < a)
		a = c;
	return a;
}

static int etEPoEPn(int type)
{
	switch (type)
	{
	case EventParams_LINEAR:
		return com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_LINEAR;

	case EventParams_LOSS:
		return com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_LOSS;

	case EventParams_GAIN:
		return com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_GAIN;

	case EventParams_DEADZONE:
		return com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_DEADZONE;

	case EventParams_ENDOFTRACE:
		return com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_ENDOFTRACE;

	case EventParams_CONNECTOR:
		return com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_CONNECTOR;

	case EventParams_UNRECOGNIZED:
		return com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_NOTIDENTIFIED;

	default:
		fprintf(stderr, "Unknown epo type = %d\n", type);
		fflush(stderr);
		assert(0);
		return com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_NOTIDENTIFIED;
	}
}

void EPold2EPnew(EventParams* epo, EventP &epn, double delta_x)
{
	epn.begin	= epo->begin;
	epn.end		= epo->end;
	epn.delta_x	= delta_x;
	epn.gentype = etEPoEPn(epo->type);
	epn.mf.init(MF_ID_BREAKL);

	assert(epn.mf.isCorrect());
}


void EPold2SE(EventParams* epo, SimpleEvent &epn)
{
	epn.begin	= epo->begin;
	epn.end		= epo->end;
	epn.type	= etEPoEPn(epo->type);
}

void EPold2RE(EventParams* epo, ReliabilityEvent &epn)
{
	EPold2SE(epo, epn);
	double R = -1;
	switch (epo->type)
	{
	case EventParams_LOSS:
	case EventParams_GAIN:
		R = epo->R;
		break;

	case EventParams_CONNECTOR:
		R = epo->R;
		//fmin(epo->R1, epo->R2, epo->R3);
		break;

	default:
		R = -1;
	}
	// convert R sigma presentation ( R = 0 .. + inf )
	// to probability-like presentation ( rel = 0 .. 1 )
	if (R < 0)
		epn.nSigma = -1;
	else
		epn.nSigma = R;
}
