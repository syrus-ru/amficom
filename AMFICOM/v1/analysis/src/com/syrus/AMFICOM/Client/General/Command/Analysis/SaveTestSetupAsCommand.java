package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
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
					LangModelAnalyse.getString("noSessionError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if (bs == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("unkError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		if (bs.monitored_element_id.equals(""))
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noMonitoredElementError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		if (bs.test_setup_id.equals(""))
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noTestSetupError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		if (((type & SaveTestSetupCommand.ETALON) != 0 ||
				 (type & SaveTestSetupCommand.THRESHOLDS) != 0) &&
				 Pool.get("eventparams", traceid) == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noAnalysisError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		String s = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("testsetup"),
				LangModelAnalyse.getString("newname"),
				JOptionPane.OK_CANCEL_OPTION);
		if (s == null || s.equals(""))
			return;

		TestSetup ts = (TestSetup)Pool.get(TestSetup.TYPE, bs.test_setup_id);
		TestSetup newts = new TestSetup();
		newts.setId(aContext.getDataSourceInterface().GetUId(TestSetup.TYPE));
		Pool.put(TestSetup.TYPE, newts.getId(), newts);
		newts.setName(s);
		bs.test_setup_id = newts.getId();

		CriteriaSet newcs = AnalysisUtil.createDefaultCriteriaSet(dataSource);
		AnalysisUtil.setCriteriaSetFromParams(newcs);
		newts.setCriteriaSetId(newcs.getId());

		Etalon newet = AnalysisUtil.createEtalon(dataSource, (ReflectogramEvent[])Pool.get("eventparams", traceid));
		newet.setName(newet.getId());
		newts.setEthalonId(newet.getId());

		if ((type & SaveTestSetupCommand.THRESHOLDS) != 0)
		{
			ThresholdSet tset = (ThresholdSet)Pool.get(ThresholdSet.TYPE, ts.getThresholdSetId());
			ThresholdSet newtset = AnalysisUtil.createDefaultThresholdSet(
					dataSource, (ReflectogramEvent[])Pool.get("eventparams", traceid));
			newts.setThresholdSetId(newtset.getId());
		}

		newts.setAnalysisTypeId(ts.getAnalysisTypeId());
		newts.setDescription(ts.getDescription());
		newts.setEvaluationTypeId(ts.getEvaluationTypeId());
		newts.setTestArgumentSetId(ts.getTestArgumentSetId());
		newts.settestTypeId(ts.getTestTypeId());

		String[] me_ids = new String[ts.getMonitoredElementIds().length];
		for (int i = 0; i < me_ids.length; i++)
			me_ids[i] = new String(ts.getMonitoredElementIds()[i]);
		newts.setMonitoredElementIds(me_ids);

		new SaveTestSetupCommand(aContext, traceid, type).execute();

		aContext.getDispatcher().notify(new RefChangeEvent("primarytrace",
				RefChangeEvent.THRESHOLDS_CALC_EVENT));
	}
}
