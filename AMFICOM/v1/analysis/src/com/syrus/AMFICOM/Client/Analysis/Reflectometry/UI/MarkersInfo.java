package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

class MarkersInfo
{
	static final int NOANALYSIS = 0;
	static final int REFLECTIVE = 1;
	static final int NONREFLECTIVE = 2;

	int a_type;
	int a_pos;
	double a_pos_m;
	double a_loss;
	double a_reflectance;
	double a_attfactor;
	double a_cumulative_loss;

	int b_pos;
	double b_pos_m;
	int b_activeEvent;

	int a_b_distance;
	double a_b_distance_m;
	double a_b_loss;
	double a_b_attenuation = 11.;
	double lsa_attenuation = 22.;
	double a_b_orl = 33.;
}