package com.syrus.AMFICOM.Client.Analysis;

import java.io.IOException;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.ActionParameterType;
import com.syrus.AMFICOM.Client.Resource.Result.CriteriaSet;
import com.syrus.AMFICOM.Client.Resource.Result.Etalon;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.TestArgumentSet;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;
import com.syrus.AMFICOM.Client.Resource.Result.ThresholdSet;
import com.syrus.AMFICOM.Client.Resource.Test.AnalysisType;
import com.syrus.AMFICOM.Client.Resource.Test.EvaluationType;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.Threshold;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.util.ByteArray;

/**
 Class with methods used to save/load measuring parameters onto server
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Stanislav Kholshin
 * @version 1.0
 */
public class AnalysisUtil
{

	/**
	 * Method for loading CriteriaSet for certain TestSetup to Pool. If there is no
	 * CriteriaSet attached to TestSetup new CriteriaSet created by call method
	 * createDefaultCriteriaSet(dataSource);
	 *
	 * @param dataSource .
	 * @param ts .
	 * @see com.syrus.AMFICOM.Client.Resource.Result.CriteriaSet
	 */

	public static void load_CriteriaSet(DataSourceInterface dataSource, TestSetup ts)
	{

			/*
	 * <ul>
* <li>{@link com.syrus.AMFICOM.Client.Resource.Result.CriteriaSet CriteriaSet}
* <li>{@link #load_CriteriaSet(DataSourceInterface, TestSetup) load_CriteriaSet()}
* <li>{@link #load_CriteriaSet load_CriteriaSet()}
	 * </ul>
	 */

		try
		{
			CriteriaSet cs;
			if (ts.criteria_set_id.equals(""))
			{
				cs = createDefaultCriteriaSet(dataSource);
				Pool.put(CriteriaSet.typ, cs.getId(), cs);
				ts.criteria_set_id = cs.getId();
			}
			else
			{
				dataSource.LoadCriteriaSets(new String[] {ts.criteria_set_id});
				cs = (CriteriaSet)Pool.get(CriteriaSet.typ, ts.criteria_set_id);
				setParamsFromCriteriaSet(cs);
			}
		}
		catch (Exception ex)
		{
			System.out.println("Error loading CriteriaSet. ME not set");
			ex.printStackTrace();
		}
	}

	/**
	 * Method for loading ThresholdSet for certain TestSetup to Pool. If there is no
	 * ThresholdSet attached to TestSetup default ThresholdSet created by call method
	 * createDefaultThresholdSet(dataSource, ep);
	 * @param dataSource server interface
	 * @param ts TestSetup
	 * @see <a href=ThresholdSet>ThresholdSet</a>
	 */
	public static void load_Thresholds(DataSourceInterface dataSource, TestSetup ts)
	{
		try
		{
			ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", "etalon");
			if (ep == null)
			{
				Pool.remove("eventparams", "etalon");
				return;
			}

			ThresholdSet thrs;

			if (ts.threshold_set_id.equals(""))
			{
				thrs = createDefaultThresholdSet(dataSource, ep);
				ts.threshold_set_id = thrs.getId();
			}
			else
			{
				dataSource.LoadThresholdSets(new String[] {ts.threshold_set_id});
				thrs = (ThresholdSet)Pool.get(ThresholdSet.typ, ts.threshold_set_id);
				setParamsFromThresholdsSet(thrs, ep);
			}
		}
		catch (Exception ex)
		{
			System.out.println("Error loading ThresholdSet. ME not set");
			ex.printStackTrace();
		}
	}

	/**
	 * Method for loading Etalon for certain TestSetup to Pool. If there is no
	 * Etalon attached to TestSetup method returns.
	 * @param dataSource server interface
	 * @param ts TestSetup
	 * @see com.syrus.AMFICOM.Client.Resource.Result.Etalon
	 */
	public static void load_Etalon(DataSourceInterface dataSource, TestSetup ts)
	{
		if (ts.etalon_id.equals(""))
			return;

		dataSource.LoadEtalons(new String[] {ts.etalon_id});
		dataSource.LoadTestArgumentSets(new String[] {ts.test_argument_set_id});
		Etalon et = (Etalon)Pool.get(Etalon.typ, ts.etalon_id);
		TestArgumentSet metas = (TestArgumentSet)Pool.get(TestArgumentSet.typ, ts.test_argument_set_id);

		ReflectogramEvent[] events=null;
		BellcoreStructure bsEt=null;

		for (int i = 0; i < et.etalon_parameters.size(); i++)
		{
			Parameter p = (Parameter)et.etalon_parameters.get(i);
			if (p.codename.equals("dadara_etalon_event_array"))
			{
				events = ReflectogramEvent.fromByteArray(p.value);
				Pool.put("eventparams", "etalon", events);
				Pool.put(TestArgumentSet.typ, "etalon", metas);
			}
			else if (p.codename.equals("reflectogramm"))
			{
				bsEt = new BellcoreReader().getData(p.value);
				Pool.put("bellcorestructure", "etalon", bsEt);
				bsEt.title = "Эталон (" + (ts.getName().equals("") ? ts.getId() : ts.getName()) + ")";
				bsEt.test_setup_id = ts.getId();
			}
		}

		if(bsEt!=null && events!=null)
		{
			double delta_x = bsEt.getDeltaX();

			for(int i=0; i<events.length; i++)
			{
				events[i].setDeltaX(delta_x);
			}
		}
	}

	public static CriteriaSet createDefaultCriteriaSet(DataSourceInterface dataSource)
	{
		CriteriaSet cs = new CriteriaSet();
		cs.id = dataSource.GetUId(CriteriaSet.typ);
		cs.analysis_type_id = "dadara";
		cs.created_by = dataSource.getSession().getUserId();

		AnalysisType atype = (AnalysisType)Pool.get(AnalysisType.typ, cs.analysis_type_id);
		ActionParameterType apt;

		double[] defaultMinuitParams;
		defaultMinuitParams = (double[])Pool.get("analysisparameters", "minuitanalysis");
		if (defaultMinuitParams == null)
			defaultMinuitParams = (double[])Pool.get("analysisparameters", "minuitdefaults");

		try
		{
			apt = (ActionParameterType)atype.sorted_criterias.get("ref_uselinear");
			Parameter p1 = new Parameter();
			p1.id = dataSource.GetUId(Parameter.typ);
			p1.parameter_type_id = "integer";
			p1.type_id = apt.getId();
			p1.value = ByteArray.toByteArray(defaultMinuitParams[7]);
			p1.codename = "ref_uselinear";
			cs.criterias.add(p1);

			apt = (ActionParameterType)atype.sorted_criterias.get("ref_eventsize");
			Parameter p2 = new Parameter();
			p2.id = dataSource.GetUId(Parameter.typ);
			p2.parameter_type_id = "integer";
			p2.type_id = apt.getId();
			p2.value = ByteArray.toByteArray(20);
			p2.codename = "ref_eventsize";
			cs.criterias.add(p2);

			apt = (ActionParameterType)atype.sorted_criterias.get("ref_conn_fall_params");
			Parameter p3 = new Parameter();
			p3.id = dataSource.GetUId(Parameter.typ);
			p3.parameter_type_id = "double";
			p3.type_id = apt.getId();
			p3.value = ByteArray.toByteArray(defaultMinuitParams[5]);
			p3.codename = "ref_conn_fall_params";
			cs.criterias.add(p3);

			apt = (ActionParameterType)atype.sorted_criterias.get("ref_min_level");
			Parameter p4 = new Parameter();
			p4.id = dataSource.GetUId(Parameter.typ);
			p4.parameter_type_id = "double";
			p4.type_id = apt.getId();
			p4.value = ByteArray.toByteArray(defaultMinuitParams[0]);
			p4.codename = "ref_min_level";
			cs.criterias.add(p4);

			apt = (ActionParameterType)atype.sorted_criterias.get("ref_max_level_noise");
			Parameter p5 = new Parameter();
			p5.id = dataSource.GetUId(Parameter.typ);
			p5.parameter_type_id = "double";
			p5.type_id = apt.getId();
			p5.value = ByteArray.toByteArray(defaultMinuitParams[4]);
			p5.codename = "ref_max_level_noise";
			cs.criterias.add(p5);

			apt = (ActionParameterType)atype.sorted_criterias.get("ref_min_level_to_find_end");
			Parameter p6 = new Parameter();
			p6.id = dataSource.GetUId(Parameter.typ);
			p6.parameter_type_id = "double";
			p6.type_id = apt.getId();
			p6.value = ByteArray.toByteArray(defaultMinuitParams[3]);
			p6.codename = "ref_min_level_to_find_end";
			cs.criterias.add(p6);

			apt = (ActionParameterType)atype.sorted_criterias.get("ref_min_weld");
			Parameter p7 = new Parameter();
			p7.id = dataSource.GetUId(Parameter.typ);
			p7.parameter_type_id = "double";
			p7.type_id = apt.getId();
			p7.value = ByteArray.toByteArray(defaultMinuitParams[1]);
			p7.codename = "ref_min_weld";
			cs.criterias.add(p7);

			apt = (ActionParameterType)atype.sorted_criterias.get("ref_min_connector");
			Parameter p8 = new Parameter();
			p8.id = dataSource.GetUId(Parameter.typ);
			p8.parameter_type_id = "double";
			p8.type_id = apt.getId();
			p8.value = ByteArray.toByteArray(defaultMinuitParams[2]);
			p8.codename = "ref_min_connector";
			cs.criterias.add(p8);

			apt = (ActionParameterType)atype.sorted_criterias.get("ref_strategy");
			Parameter p9 = new Parameter();
			p9.id = dataSource.GetUId(Parameter.typ);
			p9.parameter_type_id = "integer";
			p9.type_id = apt.getId();
			p9.value = ByteArray.toByteArray((int)defaultMinuitParams[6]);
			p9.codename = "ref_strategy";
			cs.criterias.add(p9);

			double[] minuitInitialParams = (double[])Pool.get("analysisparameters", "minuitinitials");
			for (int i = 0; i < defaultMinuitParams.length; i++)
				minuitInitialParams[i] = defaultMinuitParams[i];

			Pool.put(CriteriaSet.typ, cs.getId(), cs);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return cs;
	}

	public static Etalon createEtalon(DataSourceInterface dataSource, ReflectogramEvent[] ep)
	{
		Etalon et = new Etalon();
		et.id = dataSource.GetUId(Etalon.typ);
		et.type_id = "dadara";

		Parameter p1 = new Parameter();
		p1.id = dataSource.GetUId(Parameter.typ);
		p1.parameter_type_id = "traceeventarray";
		p1.type_id = "dadara_etalon_event_array";
		p1.value = ReflectogramEvent.toByteArray(ep);
		p1.codename = "dadara_etalon_event_array";
		et.etalon_parameters.add(p1);

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		Parameter p2 = new Parameter();
		p2.id = dataSource.GetUId(Parameter.typ);
		p2.parameter_type_id = "reflectogramm";
		p2.type_id = "reflectogramm";
		p2.value = new BellcoreWriter().write(bs);
		p2.codename = "reflectogramm";
		et.etalon_parameters.add(p2);

		Pool.put(Etalon.typ, et.getId(), et);
		return et;
	}

	public static ThresholdSet createDefaultThresholdSet(DataSourceInterface dataSource, ReflectogramEvent[] ep)
	{
		ThresholdSet ts = new ThresholdSet();
		ts.id = dataSource.GetUId(ThresholdSet.typ);
		ts.evaluation_type_id = "dadara";
		ts.created_by = dataSource.getSession().getUserId();

		EvaluationType etype = (EvaluationType)Pool.get(EvaluationType.typ, ts.evaluation_type_id);
		ActionParameterType apt = (ActionParameterType)etype.sorted_thresholds.get("dadara_thresholds");

		Threshold[] threshs = new Threshold[ep.length];
		for (int i = 0; i < ep.length; i++)
		{
			if (ep[i].getThreshold() != null)
				threshs[i] = ep[i].getThreshold();
			else
			{
				threshs[i] = new Threshold(ep[i]);
				ep[i].setThreshold(threshs[i]);
			}
		}
		//threshs[i] = ep[i].getThreshold();

		Parameter p1 = new Parameter();
		p1.id = dataSource.GetUId(Parameter.typ);
		p1.parameter_type_id = "double_array";
		p1.type_id = apt.getId();
		p1.value = Threshold.toByteArray(threshs);
		p1.codename = "dadara_thresholds";
		ts.thresholds.add(p1);

		try
		{
			Parameter p2 = new Parameter();
			apt = (ActionParameterType)etype.sorted_thresholds.get("dadara_min_trace_level");
			p2.id = dataSource.GetUId(Parameter.typ);
			p2.parameter_type_id = "double";
			p2.type_id = apt.getId();
			Double min_level = (Double)Pool.get("min_trace_level", "primarytrace");
			if (min_level == null)
				p2.value = ByteArray.toByteArray(0d);
			else
				p2.value = ByteArray.toByteArray(min_level.doubleValue());
			p2.codename = "dadara_min_trace_level";
			ts.thresholds.add(p2);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		Pool.put(ThresholdSet.typ, ts.getId(), ts);
		return ts;
	}

	public static void setParamsFromThresholdsSet(ThresholdSet ts, ReflectogramEvent[] ep)
	{
		Double min_level = (Double)Pool.get("min_trace_level", "primarytrace");

		for (int i = 0; i < ts.thresholds.size(); i++)
		{
			Parameter p = (Parameter)ts.thresholds.get(i);
			if (p.codename.equals("dadara_thresholds"))
			{
				Threshold[] threshs = Threshold.fromByteArray(p.value);
				for (int j = 0; j < Math.min(threshs.length, ep.length); j++)
				{
					ep[j].setThreshold(threshs[j]);
					threshs[j].setReflectogramEvent(ep[j]);
				}
			}
			if (p.codename.equals("dadara_min_trace_level"))
			{
				try
				{
					min_level = new Double(new ByteArray(p.value).toDouble());
					min_level.doubleValue();
					Pool.put("min_trace_level", "primarytrace", min_level);
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	public static boolean save_CriteriaSet(DataSourceInterface dataSource, BellcoreStructure bs)
	{
		try
		{
			TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);
			CriteriaSet cs;

			if (ts.criteria_set_id.equals(""))
			{
				cs = createDefaultCriteriaSet(dataSource);
				Pool.put(CriteriaSet.typ, cs.getId(), cs);

				ts.analysis_type_id = "dadara";
				ts.criteria_set_id = cs.getId();
			}
			else
			{
				cs = (CriteriaSet)Pool.get(CriteriaSet.typ, ts.criteria_set_id);
				setCriteriaSetFromParams(cs);
			}
			dataSource.saveCriteriaSet(cs.getId());
			dataSource.attachCriteriaSetToME(cs.getId(), bs.monitored_element_id);
		}
		catch (Exception ex)
		{
			System.out.println("Error saving CriteriaSet. ME not set");
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean save_Etalon(DataSourceInterface dataSource, BellcoreStructure bs, ReflectogramEvent[] ep)
	{
		try
		{
			TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);

			// save etalon and attach to monitored element
			Etalon et;

			if (ts.etalon_id.equals(""))
			{
				et = createEtalon(dataSource, ep);
				Pool.put(Etalon.typ, et.getId(), et);

				ts.etalon_id = et.getId();
				ts.evaluation_type_id = "dadara";
			}
			else
			{
				et = (Etalon)Pool.get(Etalon.typ, ts.etalon_id);
				if (et == null)
				{
					et = createEtalon(dataSource, ep);
					Pool.put(Etalon.typ, et.getId(), et);

					ts.etalon_id = et.getId();
					ts.evaluation_type_id = "dadara";
				}
				else
					updateEtalon(et, ep);
			}
			dataSource.saveEtalon(et.getId());
			dataSource.attachEtalonToME(et.getId(), bs.monitored_element_id);
		}

		catch (Exception ex)
		{
			System.out.println("Error saving Etalon. ME not set");
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean save_Thresholds(DataSourceInterface dataSource, BellcoreStructure bs, ReflectogramEvent[] ep)
	{
		try
		{
			TestSetup tset = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);
			ThresholdSet ts;

			if (tset.threshold_set_id.equals(""))
			{
				ts = createDefaultThresholdSet(dataSource, ep);
				Pool.put(ThresholdSet.typ, ts.getId(), ts);
				setThresholdsSetFromParams(ts);
				tset.threshold_set_id = ts.getId();
				tset.evaluation_type_id = "dadara";
			}
			else
			{
				ts = (ThresholdSet)Pool.get(ThresholdSet.typ, tset.threshold_set_id);
				setThresholdsSetFromParams(ts);
			}
			dataSource.saveThresholdSet(ts.getId());
			dataSource.attachThresholdSetToME(ts.getId(), bs.monitored_element_id);
		}
		catch (Exception ex)
		{
			System.out.println("Error saving saveThresholdSet. ME not set");
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static void setThresholdsSetFromParams(ThresholdSet ts)
	{
		ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", "primarytrace");
		Double min_level = (Double)Pool.get("min_trace_level", "primarytrace");
		if (ep == null || min_level == null)
			return;

		Threshold[] threshs = new Threshold[ep.length];
		for (int i = 0; i < ep.length; i++)
			threshs[i] = ep[i].getThreshold();

		for (int i = 0; i < ts.thresholds.size(); i++)
		{
			Parameter p = (Parameter)ts.thresholds.get(i);
			if (p.codename.equals("dadara_thresholds"))
			{
				p.value = Threshold.toByteArray(threshs);
			}
			if (p.codename.equals("dadara_min_trace_level"))
			{
				try
				{
					p.value = ByteArray.toByteArray(min_level.doubleValue());
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	static void updateEtalon(Etalon et, ReflectogramEvent[] ep)
	{
		for (int i = 0; i < et.etalon_parameters.size(); i++)
		{
			Parameter p = (Parameter)et.etalon_parameters.get(i);
			if (p.codename.equals("dadara_etalon_event_array"))
			{
				p.value = ReflectogramEvent.toByteArray(ep);
			}
			if (p.codename.equals("reflectogramm"))
			{
				BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
				p.value = new BellcoreWriter().write(bs);
			}
		}
	}

	public static void setCriteriaSetFromParams(CriteriaSet cs)
	{
		double[] defaultMinuitParams;
		defaultMinuitParams = (double[])Pool.get("analysisparameters", "minuitanalysis");
		if (defaultMinuitParams == null)
			defaultMinuitParams = (double[])Pool.get("analysisparameters", "minuitdefaults");

		try
		{
			for (int i = 0; i < cs.criterias.size(); i++)
			{
				Parameter p = (Parameter)cs.criterias.get(i);
				if (p.codename.equals("ref_uselinear"))
					p.value = ByteArray.toByteArray((int)defaultMinuitParams[7]);
				else if (p.codename.equals("ref_strategy"))
					p.value = ByteArray.toByteArray((int)defaultMinuitParams[6]);
				else if (p.codename.equals("ref_conn_fall_params"))
					p.value = ByteArray.toByteArray(defaultMinuitParams[5]);
				else if (p.codename.equals("ref_min_level"))
					p.value = ByteArray.toByteArray(defaultMinuitParams[0]);
				else if (p.codename.equals("ref_max_level_noise"))
					p.value = ByteArray.toByteArray(defaultMinuitParams[4]);
				else if (p.codename.equals("ref_min_level_to_find_end"))
					p.value = ByteArray.toByteArray(defaultMinuitParams[3]);
				else if (p.codename.equals("ref_min_weld"))
					p.value = ByteArray.toByteArray(defaultMinuitParams[1]);
				else if (p.codename.equals("ref_min_connector"))
					p.value = ByteArray.toByteArray(defaultMinuitParams[2]);
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void setParamsFromCriteriaSet(CriteriaSet cs)
	{
		double[] minuitParams = (double[])Pool.get("analysisparameters", "minuitanalysis");

		try
		{
			for (int i = 0; i < cs.criterias.size(); i++)
			{
				Parameter p = (Parameter)cs.criterias.get(i);
				if (p.codename.equals("ref_uselinear"))
					minuitParams[7] = new ByteArray(p.value).toInt();
				else if (p.codename.equals("ref_strategy"))
					minuitParams[6] = new ByteArray(p.value).toInt();
				else if (p.codename.equals("ref_conn_fall_params"))
					minuitParams[5] = new ByteArray(p.value).toDouble();
				else if (p.codename.equals("ref_min_level"))
					minuitParams[0] = new ByteArray(p.value).toDouble();
				else if (p.codename.equals("ref_max_level_noise"))
					minuitParams[4] = new ByteArray(p.value).toDouble();
				else if (p.codename.equals("ref_min_level_to_find_end"))
					minuitParams[3] = new ByteArray(p.value).toDouble();
				else if (p.codename.equals("ref_min_weld"))
					minuitParams[1] = new ByteArray(p.value).toDouble();
				else if (p.codename.equals("ref_min_connector"))
					minuitParams[2] = new ByteArray(p.value).toDouble();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		double[] minuitInitialParams = (double[])Pool.get("analysisparameters", "minuitinitials");
		for (int i = 0; i < minuitParams.length; i++)
			minuitInitialParams[i] = minuitParams[i];
	}
}