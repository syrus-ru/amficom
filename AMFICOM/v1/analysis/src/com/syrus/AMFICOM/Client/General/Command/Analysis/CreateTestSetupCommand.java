package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
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
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if (bs == null)
			return;

		// for local debugging, allow creating test setups with no monitoredElement 
		if (bs.monitoredElementId == null && !AnalyseMainFrameSimplified.DEBUG)
		{
			JOptionPane.showMessageDialog (
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noMonitoredElementError"),
					LangModelAnalyse.getString("error"),
					JOptionPane.OK_OPTION);
			return;
		}

		MeasurementSetup ms = (MeasurementSetup)Pool.get(AnalysisUtil.CONTEXT, "MeasurementSetup");

		String name = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("newname"),
				LangModelAnalyse.getString("testsetup"),
				JOptionPane.QUESTION_MESSAGE);
		if (name == null || name.equals(""))
			return;

		Pool.remove(AnalysisUtil.CONTEXT, "MeasurementSetup");

		MeasurementSetup measurementSetup;
		try
		{
			measurementSetup = MeasurementSetup.createInstance(
					((RISDSessionInfo)aContext.getSessionInterface()).getUserIdentifier(),
					ms.getParameterSet(),
					ms.getThresholdSet(),
					ms.getEtalon(),
					ms.getThresholdSet(),
					name,
					ms.getMeasurementDuration(),
					ms.getMonitoredElementIds());
			MeasurementStorableObjectPool.putStorableObject(measurementSetup);
		}
		catch (CreateObjectException e)
		{
			// FIXME
			System.err.println("CreateTestSetupCommand: CreateObjectException.");
			e.printStackTrace();
			return;
		}
		catch(IllegalObjectEntityException e)
		{
			// FIXME
			System.err.println("CreateTestSetupCommand: IllegalObjectEntityException.");
			e.printStackTrace();
			return;
		}
		// будем считать status == OK, если в Pool есть объект MeasurementSetup
		Pool.put(AnalysisUtil.CONTEXT, "MeasurementSetup", measurementSetup);

		aContext.getDispatcher().notify(new RefChangeEvent(traceid,
				RefChangeEvent.THRESHOLDS_CALC_EVENT));
	}
}

