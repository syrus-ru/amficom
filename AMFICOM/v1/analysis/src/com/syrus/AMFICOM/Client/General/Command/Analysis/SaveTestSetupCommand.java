package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.io.BellcoreStructure;

public class SaveTestSetupCommand extends VoidCommand
{
	public static final long CRITERIA = 0x00000001;
	public static final long ETALON = 0x00000002;
	public static final long THRESHOLDS = 0x00000004;

	ApplicationContext aContext;
	String traceid;

	long type;

	public SaveTestSetupCommand(ApplicationContext aContext, String id, long type)
	{
		this.aContext = aContext;
		this.traceid = id;
		this.type = type;
	}

	public Object clone()
	{
		return new SaveTestSetupCommand(aContext, traceid, type);
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

		TestSetup ts = (TestSetup)Pool.get(TestSetup.TYPE, bs.test_setup_id);
		if (ts.getTestArgumentSetId().length() == 0)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noTestArgumentsError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		ReflectogramEvent[] ep = null;
		if ((type & ETALON) != 0 || (type & THRESHOLDS) != 0)
		{
			ep = (ReflectogramEvent[])Pool.get("eventparams", traceid);
			if (ep == null)
			{
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						LangModelAnalyse.getString("noAnalysisError"),
						LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
				return;
			}
		}

		if (ts.getName().equals(""))
		{
			String s = JOptionPane.showInputDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("testsetup"),
					LangModelAnalyse.getString("newname"), JOptionPane.OK_CANCEL_OPTION);
			if (s == null || s.equals(""))
				return;
			ts.setName(s);
		}

		dataSource.attachTestArgumentSetToME(ts.getTestArgumentSetId(), bs.monitored_element_id);

		ts.setCreatedBy(dataSource.getSession().getUserId());

		if ((type & CRITERIA) != 0)
			AnalysisUtil.save_CriteriaSet(dataSource, bs);
		if ((type & ETALON) != 0)
			AnalysisUtil.save_Etalon(dataSource, bs, ep);
		if ((type & THRESHOLDS) != 0)
			AnalysisUtil.save_Thresholds(dataSource, bs, ep);

		dataSource.saveTestSetup(ts.getId());
		dataSource.attachTestSetupToME(ts.getId(), bs.monitored_element_id);
	}



}
