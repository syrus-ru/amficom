// EventParams (old) to EventP (new) converter.
// Used with InitialAnalysis

#include "EPold2EPnew.h"
#include "com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent.h"
#include "../Common/assert.h"

void EPold2EPnew(EventParams* epo, EventP &epn, double delta_x)
{
	// fill ep
	epn.begin	= epo->begin;
	epn.end		= epo->end;
	epn.delta_x	= delta_x;

	/*--- was used with analytic model functions
	// fill ep.mf, ep.type, ep.mf.pars
	switch (epo->type)
	{
	case EventParams_LINEAR:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_LINEAR;

		epn.mf.init(MF_ID_LIN);
		assert(epn.mf.isCorrect());

		epn.mf[0] = epo->a_linear - epo->b_linear * epo->begin;
		epn.mf[1] = epo->b_linear;
		break;

	case EventParams_SPLICE:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_WELD;

		epn.mf.init (MF_ID_SPL1);
		assert(epn.mf.isCorrect());

		epn.mf[0] = epo->a_weld;
		epn.mf[1] = epo->b_weld;
		epn.mf[2] = epo->boost_weld;
		epn.mf[3] = epo->center_weld;
		epn.mf[4] = epo->width_weld;
		break;

	case EventParams_CONNECTOR:
		{
			epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_CONNECTOR;

			int newtype = MF_ID_CON1c;

			epn.mf.init(newtype);
			assert(epn.mf.isCorrect());

			switch(newtype) // XXX: несколько вариантов, но работать будет только один - остальные в runtime не нужны
			{

			case MF_ID_CON1c:
				epn.mf[0] = epo->a1_connector; // -- CON1c,d parameters
				epn.mf[1] = epo->aLet_connector;
				epn.mf[2] = epo->a1_connector + epo->aLet_connector
					- epo->a2_connector * exp(-epo->sigma1_connector / epo->width_connector); // XXX: ?

				//epn.mf.pars[3] = epn.begin;
				//epn.mf.pars[4] = epn.end;
				epn.mf[3] = epo->center_connector - epo->width_connector / 2.0;
				epn.mf[4] = epo->center_connector + epo->width_connector / 2.0;
				fprintf(stderr, "EPoldEPnew: begin %d end %d; c %g c-w/2 %g c+w/2 %g\n", // FIXIT
					epo->begin, epo->end,
					epo->center_connector,
					epo->center_connector - epo->width_connector / 2, epo->center_connector + epo->width_connector / 2);
				fflush(stderr);
				epn.mf[5] = 1.0; //epo->sigma1_connector;
				epn.mf[6] = 1.0; //epo->sigma2_connector;
				epn.mf[7] = 1.0; //epo->sigmaFit_connector; // CON1c
				epn.mf[8] = 1.0; // epo->k_connector;  // CON1c
				break;

			case MF_ID_CON1d:
				epn.mf[0] = epo->a1_connector; // -- CON1c,d parameters
				epn.mf[1] = epo->aLet_connector;
				epn.mf[2] = epo->a1_connector + epo->aLet_connector
					- epo->a2_connector * exp(-epo->sigma1_connector / epo->width_connector); // XXX: ?

				//epn.mf.pars[3] = epn.begin;
				//epn.mf.pars[4] = epn.end;
				epn.mf[3] = epo->center_connector - epo->width_connector / 2.0;
				epn.mf[4] = epo->center_connector + epo->width_connector / 2.0;
				fprintf(stderr, "EPoldEPnew: begin %d end %d; c %g c-w/2 %g c+w/2 %g\n", // FIXIT
					epo->begin, epo->end,
					epo->center_connector,
					epo->center_connector - epo->width_connector / 2, epo->center_connector + epo->width_connector / 2);
				fflush(stderr);
				epn.mf[5] = 1.0; //epo->sigma1_connector;
				epn.mf[6] = 1.0; //epo->sigma2_connector;
				epn.mf[7] = 0; //tgFit;  // CON1d
				break;

			case MF_ID_CON1e:
				epn.mf[0] = epo->a1_connector;
				epn.mf[1] = epo->aLet_connector;
				epn.mf[2] = 0; // XXX
				epn.mf[3] = epo->center_connector - epo->width_connector / 2.0;
				epn.mf[4] = epo->center_connector + epo->width_connector / 2.0;
				fprintf(stderr, "EPoldEPnew: begin %d end %d; c %g c-w/2 %g c+w/2 %g\n", // FIXIT
					epo->begin, epo->end,
					epo->center_connector,
					epo->center_connector - epo->width_connector / 2, epo->center_connector + epo->width_connector / 2);
				fflush(stderr);
				epn.mf[5] = 1.0; //epo->sigma1_connector;
				epn.mf[6] = -1.0; // XXX: b2
				epn.mf[7] = 0.0; // XXX: ad
				break;

			default:
				assert(0);
			}
			break;
		}

	case EventParams_SINGULARITY:
		fprintf(stderr, "SINGULARITY: epo->type = %d\n", (int )epo->type);
		fflush(stderr);
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_SINGULARITY;

		epn.mf.init(MF_ID_LIN);
		assert(epn.mf.isCorrect());

		epn.mf[0] = epo->a_linear - epo->b_linear * epo->begin;
		epn.mf[1] = epo->b_linear;
		break;

	default:
		fprintf(stderr, "Unknown epo->type = %d\n", (int )epo->type);
		fflush(stderr);
		assert(0);
	}
	*/


	// fill ep.type
	switch (epo->type)
	{
	case EventParams_LINEAR:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_LINEAR;
		epn.mf.init(MF_ID_BREAKL);
		//epn.mf.init(MF_ID_LIN);
		break;

	case EventParams_SPLICE:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_WELD;
		epn.mf.init(MF_ID_BREAKL);
		break;

	case EventParams_CONNECTOR:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_CONNECTOR;
		epn.mf.init(MF_ID_BREAKL);
		break;

	case EventParams_SINGULARITY:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_SINGULARITY;
		epn.mf.init(MF_ID_BREAKL);
		break;

	default:
		fprintf(stderr, "Unknown epo->type = %d\n", (int )epo->type);
		fflush(stderr);
		assert(0);
	}

	assert(epn.mf.isCorrect());
}

