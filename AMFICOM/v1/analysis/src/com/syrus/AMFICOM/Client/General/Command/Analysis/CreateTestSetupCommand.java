package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;

import com.syrus.io.BellcoreStructure;

public class CreateTestSetupCommand extends VoidCommand
{
	ApplicationContext aContext;
	String traceid;

	public CreateTestSetupCommand(ApplicationContext aContext, String id)
	{
		this.aContext = aContext;
		this.traceid = id;
	}

	public Object clone()
	{
		return new CreateTestSetupCommand(aContext, traceid);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if (bs == null)
			return;

		if (bs.monitored_element_id.equals(""))
		{
			JOptionPane.showMessageDialog (
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noMonitoredElementError"),
					LangModelAnalyse.getString("error"),
					JOptionPane.OK_OPTION);
			return;
		}

		TestSetup _ts = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);
		if (_ts == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("unkError"),
					LangModelAnalyse.getString("error"),
					JOptionPane.OK_OPTION);
			return;
		}

		String ret = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("newname"),
				LangModelAnalyse.getString("testsetup"),
				JOptionPane.QUESTION_MESSAGE);
		if (ret == null || ret.equals(""))
			return;

		TestSetup ts = new TestSetup();
		ts.setName(ret);
		ts.settestTypeId(_ts.getTestTypeId());
		ts.setId(dataSource.GetUId(TestSetup.typ));
		ts.setTestArgumentSetId(_ts.getTestArgumentSetId());

		bs.test_setup_id = ts.getId();
		Pool.put(TestSetup.typ, ts.getId(), ts);

		aContext.getDispatcher().notify(new RefChangeEvent(traceid,
				RefChangeEvent.THRESHOLDS_CALC_EVENT));
	}
}

