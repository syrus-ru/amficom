package com.syrus.AMFICOM.Client.Analysis;

import java.io.IOException;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.*;
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
	public static final String DADARA = "dadara";
	public static final String ETALON = "etalon";
	public static final String DADARA_ETALON_ARRAY = "dadara_etalon_event_array";
	public static final String REFLECTOGRAMM = "reflectogramm";

	private AnalysisUtil()
	{
	}

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
			if (ts.getCriteriaSetId().length() == 0)
			{
				cs = createDefaultCriteriaSet(dataSource);
				Pool.put(CriteriaSet.TYPE, cs.getId(), cs);
				ts.setCriteriaSetId(cs.getId());
			}
			else
			{
				dataSource.LoadCriteriaSets(new String[] {ts.getCriteriaSetId()});
				cs = (CriteriaSet)Pool.get(CriteriaSet.TYPE, ts.getCriteriaSetId());
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
			ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", AnalysisUtil.ETALON);
			if (ep == null)
			{
				Pool.remove("eventparams", AnalysisUtil.ETALON);
				return;
			}

			ThresholdSet thrs;

			if (ts.getThresholdSetId().length() == 0)
			{
				thrs = createDefaultThresholdSet(dataSource, ep);
				ts.setThresholdSetId(thrs.getId());
			}
			else
			{
				dataSource.LoadThresholdSets(new String[] {ts.getThresholdSetId()});
				thrs = (ThresholdSet)Pool.get(ThresholdSet.TYPE, ts.getThresholdSetId());
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
		if (ts.getEthalonId().length() == 0)
			return;

		dataSource.LoadEtalons(new String[] {ts.getEthalonId()});
		dataSource.LoadTestArgumentSets(new String[] {ts.getTestArgumentSetId()});
		Etalon et = (Etalon)Pool.get(Etalon.TYPE, ts.getEthalonId());
		TestArgumentSet metas = (TestArgumentSet)Pool.get(TestArgumentSet.TYPE, ts.getTestArgumentSetId());

		ReflectogramEvent[] events=null;
		BellcoreStructure bsEt=null;

		for (Iterator it = et.getEthalonParameterList().iterator(); it.hasNext();)
		{
			Parameter p = (Parameter)it.next();
			if (p.getCodename().equals(DADARA_ETALON_ARRAY))
			{
				events = ReflectogramEvent.fromByteArray(p.getValue());
				Pool.put("eventparams", ETALON, events);
				Pool.put(TestArgumentSet.TYPE, ETALON, metas);
			}
			else if (p.getCodename().equals(REFLECTOGRAMM))
			{
				bsEt = new BellcoreReader().getData(p.getValue());
				Pool.put("bellcorestructure", AnalysisUtil.ETALON, bsEt);
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
		cs.setId(dataSource.GetUId(CriteriaSet.TYPE));
		cs.setAnalysisTypeId(AnalysisUtil.DADARA);
		cs.setCreatedBy(dataSource.getSession().getUserId());

		AnalysisType atype = (AnalysisType)Pool.get(AnalysisType.typ, cs.getAnalysisTypeId());
		ActionParameterType apt;

		double[] defaultMinuitParams;
		defaultMinuitParams = (double[])Pool.get("analysisparameters", "minuitanalysis");
		if (defaultMinuitParams == null)
			defaultMinuitParams = (double[])Pool.get("analysisparameters", "minuitdefaults");

		try
		{
			apt = (ActionParameterType)atype.getSortedCriterias().get("ref_uselinear");
			Parameter p1 = new Parameter();
			p1.setId(dataSource.GetUId(Parameter.typ));
			p1.setParameterTypeId("integer");
			p1.setTypeId(apt.getId());
			p1.setValue(ByteArray.toByteArray(defaultMinuitParams[7]));
			p1.setCodename("ref_uselinear");
			cs.getCriteriaList().add(p1);

			apt = (ActionParameterType)atype.getSortedCriterias().get("ref_eventsize");
			Parameter p2 = new Parameter();
			p2.setId(dataSource.GetUId(Parameter.typ));
			p2.setParameterTypeId("integer");
			p2.setTypeId(apt.getId());
			p2.setValue(ByteArray.toByteArray(20));
			p2.setCodename("ref_eventsize");
			cs.getCriteriaList().add(p2);

			apt = (ActionParameterType)atype.getSortedCriterias().get("ref_conn_fall_params");
			Parameter p3 = new Parameter();
			p3.setId(dataSource.GetUId(Parameter.typ));
			p3.setParameterTypeId("double");
			p3.setTypeId(apt.getId());
			p3.setValue(ByteArray.toByteArray(defaultMinuitParams[5]));
			p3.setCodename("ref_conn_fall_params");
			cs.getCriteriaList().add(p3);

			apt = (ActionParameterType)atype.getSortedCriterias().get("ref_min_level");
			Parameter p4 = new Parameter();
			p4.setId(dataSource.GetUId(Parameter.typ));
			p4.setParameterTypeId("double");
			p4.setTypeId(apt.getId());
			p4.setValue(ByteArray.toByteArray(defaultMinuitParams[0]));
			p4.setCodename("ref_min_level");
			cs.getCriteriaList().add(p4);

			apt = (ActionParameterType)atype.getSortedCriterias().get("ref_max_level_noise");
			Parameter p5 = new Parameter();
			p5.setId(dataSource.GetUId(Parameter.typ));
			p5.setParameterTypeId("double");
			p5.setTypeId(apt.getId());
			p5.setValue(ByteArray.toByteArray(defaultMinuitParams[4]));
			p5.setCodename("ref_max_level_noise");
			cs.getCriteriaList().add(p5);

			apt = (ActionParameterType)atype.getSortedCriterias().get("ref_min_level_to_find_end");
			Parameter p6 = new Parameter();
			p6.setId(dataSource.GetUId(Parameter.typ));
			p6.setParameterTypeId("double");
			p6.setTypeId(apt.getId());
			p6.setValue(ByteArray.toByteArray(defaultMinuitParams[3]));
			p6.setCodename("ref_min_level_to_find_end");
			cs.getCriteriaList().add(p6);

			apt = (ActionParameterType)atype.getSortedCriterias().get("ref_min_weld");
			Parameter p7 = new Parameter();
			p7.setId(dataSource.GetUId(Parameter.typ));
			p7.setParameterTypeId("double");
			p7.setTypeId(apt.getId());
			p7.setValue(ByteArray.toByteArray(defaultMinuitParams[1]));
			p7.setCodename("ref_min_weld");
			cs.getCriteriaList().add(p7);

			apt = (ActionParameterType)atype.getSortedCriterias().get("ref_min_connector");
			Parameter p8 = new Parameter();
			p8.setId(dataSource.GetUId(Parameter.typ));
			p8.setParameterTypeId("double");
			p8.setTypeId(apt.getId());
			p8.setValue(ByteArray.toByteArray(defaultMinuitParams[2]));
			p8.setCodename("ref_min_connector");
			cs.getCriteriaList().add(p8);

			apt = (ActionParameterType)atype.getSortedCriterias().get("ref_strategy");
			Parameter p9 = new Parameter();
			p9.setId(dataSource.GetUId(Parameter.typ));
			p9.setParameterTypeId("integer");
			p9.setTypeId(apt.getId());
			p9.setValue(ByteArray.toByteArray((int)defaultMinuitParams[6]));
			p9.setCodename("ref_strategy");
			cs.getCriteriaList().add(p9);

			double[] minuitInitialParams = (double[])Pool.get("analysisparameters", "minuitinitials");
			for (int i = 0; i < defaultMinuitParams.length; i++)
				minuitInitialParams[i] = defaultMinuitParams[i];

			Pool.put(CriteriaSet.TYPE, cs.getId(), cs);
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
		et.setId(dataSource.GetUId(Etalon.TYPE));
		et.setTypeId(AnalysisUtil.DADARA);

		Parameter p1 = new Parameter();
		p1.setId(dataSource.GetUId(Parameter.typ));
		p1.setParameterTypeId("traceeventarray");
		p1.setTypeId(DADARA_ETALON_ARRAY);
		p1.setValue(ReflectogramEvent.toByteArray(ep));
		p1.setCodename(DADARA_ETALON_ARRAY);
		et.getEthalonParameterList().add(p1);

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		Parameter p2 = new Parameter();
		p2.setId(dataSource.GetUId(Parameter.typ));
		p2.setParameterTypeId(REFLECTOGRAMM);
		p2.setTypeId(REFLECTOGRAMM);
		p2.setValue(new BellcoreWriter().write(bs));
		p2.setCodename(REFLECTOGRAMM);
		et.getEthalonParameterList().add(p2);

		Pool.put(Etalon.TYPE, et.getId(), et);
		return et;
	}

	public static ThresholdSet createDefaultThresholdSet(DataSourceInterface dataSource, ReflectogramEvent[] ep)
	{
		ThresholdSet ts = new ThresholdSet();
		ts.setId(dataSource.GetUId(ThresholdSet.TYPE));
		ts.setEvaluationTypeId(AnalysisUtil.DADARA);
		ts.setCreatedBy(dataSource.getSession().getUserId());

		EvaluationType etype = (EvaluationType)Pool.get(EvaluationType.TYPE, ts.getEvaluationTypeId());
		ActionParameterType apt = (ActionParameterType)etype.getSortedThresholds().get("dadara_thresholds");

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
		p1.setId(dataSource.GetUId(Parameter.typ));
		p1.setParameterTypeId("double_array");
		p1.setTypeId(apt.getId());
		p1.setValue(Threshold.toByteArray(threshs));
		p1.setCodename("dadara_thresholds");
		ts.getThresholdList().add(p1);

		try
		{
			Parameter p2 = new Parameter();
			apt = (ActionParameterType)etype.getSortedThresholds().get("dadara_min_trace_level");
			p2.setId(dataSource.GetUId(Parameter.typ));
			p2.setParameterTypeId("double");
			p2.setTypeId(apt.getId());
			Double min_level = (Double)Pool.get("min_trace_level", "primarytrace");
			if (min_level == null)
				p2.setValue(ByteArray.toByteArray(0d));
			else
				p2.setValue(ByteArray.toByteArray(min_level.doubleValue()));
			p2.setCodename("dadara_min_trace_level");
			ts.getThresholdList().add(p2);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		Pool.put(ThresholdSet.TYPE, ts.getId(), ts);
		return ts;
	}

	public static void setParamsFromThresholdsSet(ThresholdSet ts, ReflectogramEvent[] ep)
	{
		Double min_level = (Double)Pool.get("min_trace_level", "primarytrace");

		for (Iterator it = ts.getThresholdList().iterator(); it.hasNext();)
		{
			Parameter p = (Parameter)it.next();
			if (p.getCodename().equals("dadara_thresholds"))
			{
				Threshold[] threshs = Threshold.fromByteArray(p.getValue());
				for (int j = 0; j < Math.min(threshs.length, ep.length); j++)
				{
					ep[j].setThreshold(threshs[j]);
					threshs[j].setReflectogramEvent(ep[j]);
				}
			}
			if (p.getCodename().equals("dadara_min_trace_level"))
			{
				try
				{
					min_level = new Double(new ByteArray(p.getValue()).toDouble());
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
			TestSetup ts = (TestSetup)Pool.get(TestSetup.TYPE, bs.test_setup_id);
			CriteriaSet cs;

			if (ts.getCriteriaSetId().equals(""))
			{
				cs = createDefaultCriteriaSet(dataSource);
				Pool.put(CriteriaSet.TYPE, cs.getId(), cs);

				ts.setAnalysisTypeId(AnalysisUtil.DADARA);
				ts.setCriteriaSetId(cs.getId());
			}
			else
			{
				cs = (CriteriaSet)Pool.get(CriteriaSet.TYPE, ts.getCriteriaSetId());
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
			TestSetup ts = (TestSetup)Pool.get(TestSetup.TYPE, bs.test_setup_id);

			// save etalon and attach to monitored element
			Etalon et;

			if (ts.getEthalonId().length() == 0)
			{
				et = createEtalon(dataSource, ep);
				Pool.put(Etalon.TYPE, et.getId(), et);

				ts.setEthalonId(et.getId());
				ts.setEvaluationTypeId(AnalysisUtil.DADARA);
			}
			else
			{
				et = (Etalon)Pool.get(Etalon.TYPE, ts.getEthalonId());
				if (et == null)
				{
					et = createEtalon(dataSource, ep);
					Pool.put(Etalon.TYPE, et.getId(), et);

					ts.setEthalonId(et.getId());
					ts.setEvaluationTypeId(AnalysisUtil.DADARA);
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
			TestSetup tset = (TestSetup)Pool.get(TestSetup.TYPE, bs.test_setup_id);
			ThresholdSet ts;

			if (tset.getThresholdSetId().length() == 0)
			{
				ts = createDefaultThresholdSet(dataSource, ep);
				Pool.put(ThresholdSet.TYPE, ts.getId(), ts);
				setThresholdsSetFromParams(ts);
				tset.setThresholdSetId(ts.getId());
				tset.setEvaluationTypeId(AnalysisUtil.DADARA);
			}
			else
			{
				ts = (ThresholdSet)Pool.get(ThresholdSet.TYPE, tset.getThresholdSetId());
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

		for (Iterator it = ts.getThresholdList().iterator(); it.hasNext();)
		{
			Parameter p = (Parameter)it.next();
			if (p.getCodename().equals("dadara_thresholds"))
			{
				p.setValue(Threshold.toByteArray(threshs));
			}
			if (p.getCodename().equals("dadara_min_trace_level"))
			{
				try
				{
					p.setValue(ByteArray.toByteArray(min_level.doubleValue()));
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
		for (Iterator it = et.getEthalonParameterList().iterator(); it.hasNext();)
		{
			Parameter p = (Parameter)it.next();
			if (p.getCodename().equals(DADARA_ETALON_ARRAY))
			{
				p.setValue(ReflectogramEvent.toByteArray(ep));
			}
			if (p.getCodename().equals(REFLECTOGRAMM))
			{
				BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
				p.setValue(new BellcoreWriter().write(bs));
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
			for (Iterator it = cs.getCriteriaList().iterator(); it.hasNext();)
			{
				Parameter p = (Parameter)it.next();
				if (p.getCodename().equals("ref_uselinear"))
					p.setValue(ByteArray.toByteArray((int)defaultMinuitParams[7]));
				else if (p.getCodename().equals("ref_strategy"))
					p.setValue(ByteArray.toByteArray((int)defaultMinuitParams[6]));
				else if (p.getCodename().equals("ref_conn_fall_params"))
					p.setValue(ByteArray.toByteArray(defaultMinuitParams[5]));
				else if (p.getCodename().equals("ref_min_level"))
					p.setValue(ByteArray.toByteArray(defaultMinuitParams[0]));
				else if (p.getCodename().equals("ref_max_level_noise"))
					p.setValue(ByteArray.toByteArray(defaultMinuitParams[4]));
				else if (p.getCodename().equals("ref_min_level_to_find_end"))
					p.setValue(ByteArray.toByteArray(defaultMinuitParams[3]));
				else if (p.getCodename().equals("ref_min_weld"))
					p.setValue(ByteArray.toByteArray(defaultMinuitParams[1]));
				else if (p.getCodename().equals("ref_min_connector"))
					p.setValue(ByteArray.toByteArray(defaultMinuitParams[2]));
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
			for (Iterator it = cs.getCriteriaList().iterator(); it.hasNext();)
			{
				Parameter p = (Parameter)it.next();
				if (p.getCodename().equals("ref_uselinear"))
					minuitParams[7] = new ByteArray(p.getValue()).toInt();
				else if (p.getCodename().equals("ref_strategy"))
					minuitParams[6] = new ByteArray(p.getValue()).toInt();
				else if (p.getCodename().equals("ref_conn_fall_params"))
					minuitParams[5] = new ByteArray(p.getValue()).toDouble();
				else if (p.getCodename().equals("ref_min_level"))
					minuitParams[0] = new ByteArray(p.getValue()).toDouble();
				else if (p.getCodename().equals("ref_max_level_noise"))
					minuitParams[4] = new ByteArray(p.getValue()).toDouble();
				else if (p.getCodename().equals("ref_min_level_to_find_end"))
					minuitParams[3] = new ByteArray(p.getValue()).toDouble();
				else if (p.getCodename().equals("ref_min_weld"))
					minuitParams[1] = new ByteArray(p.getValue()).toDouble();
				else if (p.getCodename().equals("ref_min_connector"))
					minuitParams[2] = new ByteArray(p.getValue()).toDouble();
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