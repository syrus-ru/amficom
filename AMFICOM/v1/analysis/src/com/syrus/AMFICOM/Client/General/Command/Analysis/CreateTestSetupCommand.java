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
					LangModelAnalyse.String("noMonitoredElementError"),
					LangModelAnalyse.String("error"),
					JOptionPane.OK_OPTION);
			return;
		}

		TestSetup _ts = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);
		if (_ts == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.String("unkError"),
					LangModelAnalyse.String("error"),
					JOptionPane.OK_OPTION);
			return;
		}

		String ret = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.String("newname"),
				LangModelAnalyse.String("testsetup"),
				JOptionPane.QUESTION_MESSAGE);
		if (ret == null || ret.equals(""))
			return;

		TestSetup ts = new TestSetup();
		ts.name = ret;
		ts.test_type_id = _ts.test_type_id;
		ts.id = dataSource.GetUId(TestSetup.typ);
		ts.test_argument_set_id = _ts.test_argument_set_id;

		bs.test_setup_id = ts.getId();
		Pool.put(TestSetup.typ, ts.getId(), ts);

		bs.test_setup_id = ts.getId();

		aContext.getDispatcher().notify(new RefChangeEvent(traceid,
				RefChangeEvent.THRESHOLDS_CALC_EVENT));
	}
}

