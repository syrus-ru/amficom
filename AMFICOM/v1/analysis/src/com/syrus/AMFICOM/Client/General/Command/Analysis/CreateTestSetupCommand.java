package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;

public class CreateTestSetupCommand extends VoidCommand
{
	public static final int CANCEL = 0;
	public static final int OK = 1;
	ApplicationContext aContext;
	String traceid;
	int status = CANCEL;
	MeasurementSetup measurementSetup;

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
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if (bs == null)
			return;

		if (bs.monitoredElementId == null)
		{
			JOptionPane.showMessageDialog (
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noMonitoredElementError"),
					LangModelAnalyse.getString("error"),
					JOptionPane.OK_OPTION);
			return;
		}

		Measurement m = null;
		try
		{
			m = (Measurement)MeasurementStorableObjectPool.getStorableObject(
						 new Identifier(bs.measurementId), true);
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception retrieving measurenent with " + bs.measurementId);
			ex.printStackTrace();
			return;
		}
		MeasurementSetup ms = m.getSetup();

		String name = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("newname"),
				LangModelAnalyse.getString("testsetup"),
				JOptionPane.QUESTION_MESSAGE);
		if (name == null || name.equals(""))
			return;

		measurementSetup = MeasurementSetup.createInstance(
				((RISDSessionInfo)aContext.getSessionInterface()).getUserIdentifier(),
				ms.getParameterSet(),
				ms.getThresholdSet(),
				ms.getEtalon(),
				ms.getThresholdSet(),
				name,
				ms.getMeasurementDuration(),
				ms.getMonitoredElementIds());

		try
		{
			MeasurementStorableObjectPool.putStorableObject(measurementSetup);
			status = OK;
		}
		catch(IllegalObjectEntityException ex)
		{
		}

		aContext.getDispatcher().notify(new RefChangeEvent(traceid,
				RefChangeEvent.THRESHOLDS_CALC_EVENT));
	}
}

