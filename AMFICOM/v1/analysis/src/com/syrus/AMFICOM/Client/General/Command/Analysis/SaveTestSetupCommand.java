package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
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

		TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);
		if (ts.getTestArgumentSetId().length() == 0)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("noTestArgumentsError"),
					LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
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
						LangModelAnalyse.String("noAnalysisError"),
						LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
				return;
			}
		}

		if (ts.getName().equals(""))
		{
			String s = JOptionPane.showInputDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("testsetup"),
					LangModelAnalyse.String("newname"), JOptionPane.OK_CANCEL_OPTION);
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
