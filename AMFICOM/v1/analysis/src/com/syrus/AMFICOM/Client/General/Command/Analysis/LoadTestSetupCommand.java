package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.TestSetupLoadDialog;
import com.syrus.io.BellcoreStructure;

public class LoadTestSetupCommand extends VoidCommand
{
	ApplicationContext aContext;
	String traceid;

	public LoadTestSetupCommand(ApplicationContext aContext, String id)
	{
		this.aContext = aContext;
		this.traceid = id;
	}

	public Object clone()
	{
		return new LoadTestSetupCommand(aContext, traceid);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if (bs == null || bs.monitored_element_id.equals(""))
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("noMonitoredElementError"),
					LangModelAnalyse.String("error"), JOptionPane.OK_OPTION);
			return;
		}

		TestSetupLoadDialog dialog = new TestSetupLoadDialog (aContext);
		dialog.show();

		if(dialog.ret_code == 0)
			return;
		if (!(dialog.resource instanceof TestSetup))
			return;

		TestSetup ts = (TestSetup)dialog.resource;

		bs.test_setup_id = ts.getId();

		if (Pool.get("eventparams", "etalon") != null)
		{
			aContext.getDispatcher().notify(new RefChangeEvent("etalon", RefChangeEvent.CLOSE_EVENT));
			aContext.getDispatcher().notify(new RefChangeEvent("primarytrace", RefChangeEvent.SELECT_EVENT));
		}

		AnalysisUtil.load_CriteriaSet(dataSource, ts);

		if (!ts.etalon_id.equals(""))
			AnalysisUtil.load_Etalon(dataSource, ts);

		//		if (!ts.threshold_set_id.equals(""))
		AnalysisUtil.load_Thresholds(dataSource, ts);

		aContext.getDispatcher().notify(new RefUpdateEvent("etalon",
				RefUpdateEvent.THRESHOLDS_UPDATED_EVENT));
		aContext.getDispatcher().notify(new RefChangeEvent("primarytrace",
				RefChangeEvent.THRESHOLDS_CALC_EVENT));
/*		aContext.getDispatcher().notify(new RefChangeEvent("primarytrace",
				RefChangeEvent.CLOSE_EVENT));
		aContext.getDispatcher().notify(new RefChangeEvent("primarytrace",
				RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
		aContext.getDispatcher().notify(new RefUpdateEvent("primarytrace",
				RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));*/
	}
}
