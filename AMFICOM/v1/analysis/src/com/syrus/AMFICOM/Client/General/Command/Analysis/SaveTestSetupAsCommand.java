package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.CriteriaSet;
import com.syrus.AMFICOM.Client.Resource.Result.Etalon;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;
import com.syrus.AMFICOM.Client.Resource.Result.ThresholdSet;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.io.BellcoreStructure;

public class SaveTestSetupAsCommand extends VoidCommand
{
	ApplicationContext aContext;
	String traceid;

	long type;

	public SaveTestSetupAsCommand(ApplicationContext aContext, String id, long type)
	{
		this.aContext = aContext;
		this.traceid = id;
		this.type = type;
	}

	public Object clone()
	{
		return new SaveTestSetupAsCommand(aContext, traceid, type);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("noSessionError"),
					LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if (bs == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("unkError"),
					LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
			return;
		}

		if (bs.monitored_element_id.equals(""))
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("noMonitoredElementError"),
					LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
			return;
		}

		if (bs.test_setup_id.equals(""))
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("noTestSetupError"),
					LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
			return;
		}

		if (((type & SaveTestSetupCommand.ETALON) != 0 ||
				 (type & SaveTestSetupCommand.THRESHOLDS) != 0) &&
				 Pool.get("eventparams", traceid) == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("noAnalysisError"),
					LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
			return;
		}

		String s = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.String("testsetup"),
				LangModelAnalyse.String("newname"),
				JOptionPane.OK_CANCEL_OPTION);
		if (s == null || s.equals(""))
			return;

		TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);
		TestSetup newts = new TestSetup();
		newts.id = aContext.getDataSourceInterface().GetUId(TestSetup.typ);
		Pool.put(TestSetup.typ, newts.getId(), newts);
		newts.name = s;
		bs.test_setup_id = newts.getId();

		CriteriaSet newcs = AnalysisUtil.createDefaultCriteriaSet(dataSource);
		AnalysisUtil.setCriteriaSetFromParams(newcs);
		newts.criteria_set_id = newcs.getId();

		Etalon newet = AnalysisUtil.createEtalon(dataSource, (ReflectogramEvent[])Pool.get("eventparams", traceid));
		newet.name = newet.getId();
		newts.etalon_id = newet.getId();

		if ((type & SaveTestSetupCommand.THRESHOLDS) != 0)
		{
			ThresholdSet tset = (ThresholdSet)Pool.get(ThresholdSet.typ, ts.threshold_set_id);
			ThresholdSet newtset = AnalysisUtil.createDefaultThresholdSet(
					dataSource, (ReflectogramEvent[])Pool.get("eventparams", traceid));
			newts.threshold_set_id = newtset.getId();
		}

		newts.analysis_type_id = ts.analysis_type_id;
		newts.description = ts.description;
		newts.evaluation_type_id = ts.evaluation_type_id;
		newts.test_argument_set_id = ts.test_argument_set_id;
		newts.test_type_id = ts.test_type_id;

		newts.monitored_element_ids = new String[ts.monitored_element_ids.length];
		for (int i = 0; i < ts.monitored_element_ids.length; i++)
			newts.monitored_element_ids[i] = new String(ts.monitored_element_ids[i]);

		new SaveTestSetupCommand(aContext, traceid, type).execute();

		aContext.getDispatcher().notify(new RefChangeEvent("primarytrace",
				RefChangeEvent.THRESHOLDS_CALC_EVENT));
	}
}
