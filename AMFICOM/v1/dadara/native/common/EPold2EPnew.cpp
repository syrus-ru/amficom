// EventParams (old) to EventP (new) converter.
// Used with InitialAnalysis

#include "EPold2EPnew.h"
#include "com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent.h"
#include "../common/assert1.h"

void EPold2EPnew(EventParams* epo, EventP &epn, double delta_x)
{
	// fill ep
	epn.begin	= epo->begin;
	epn.end		= epo->end;
	epn.delta_x	= delta_x;

	// fill ep.type
	switch (epo->type)
	{
	case EventParams_LINEAR:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_LINEAR;
		epn.mf.init(MF_ID_BREAKL);
		//epn.mf.init(MF_ID_LIN);
		break;

	case EventParams_LOSS:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_LOSS;
		epn.mf.init(MF_ID_BREAKL);
		break;

	case EventParams_GAIN:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_GAIN;
		epn.mf.init(MF_ID_BREAKL);
		break;

	case EventParams_DEADZONE: // fall through
	case EventParams_REFLECTIVE:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_REFLECTIVE;
		epn.mf.init(MF_ID_BREAKL);
		break;

	case EventParams_UNRECOGNIZED:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_NOTIDENTIFIED;
		epn.mf.init(MF_ID_BREAKL);
		break;

	default:
		fprintf(stderr, "Unknown epo->type = %d\n", (int )epo->type);
		fflush(stderr);
		assert(0);
	}

	assert(epn.mf.isCorrect());
}


void EPold2SE(EventParams* epo, SimpleEvent &epn)
{
	// fill ep
	epn.begin	= epo->begin;
	epn.end		= epo->end;

	// fill ep.type
	switch (epo->type)
	{
	case EventParams_LINEAR:
		epn.type = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_LINEAR;
		break;

	case EventParams_LOSS:
		epn.type = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_LOSS;
		break;

	case EventParams_GAIN:
		epn.type = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_GAIN;
		break;

	case EventParams_DEADZONE: // fall through
	case EventParams_REFLECTIVE:
		epn.type = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_REFLECTIVE;
		break;

	case EventParams_UNRECOGNIZED:
		epn.type = com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent_NOTIDENTIFIED;
		break;

	default:
		fprintf(stderr, "Unknown epo->type = %d\n", (int )epo->type);
		fflush(stderr);
		assert(0);
	}

}
