package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.BellcoreStructure;

public class CreateTestSetupCommand extends VoidCommand
{
	private ApplicationContext aContext;

	public CreateTestSetupCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new CreateTestSetupCommand(aContext);
	}

	public void execute()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
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

		MeasurementSetup ms = Heap.getContextMeasurementSetup();

		String name = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("newname"),
				LangModelAnalyse.getString("testsetup"),
				JOptionPane.QUESTION_MESSAGE);
		if (name == null || name.equals(""))
			return;

		Heap.setContextMeasurementSetup(null);

		MeasurementSetup measurementSetup;
		try
		{
			measurementSetup = MeasurementSetup.createInstance(
					((RISDSessionInfo)aContext.getSessionInterface()).getUserIdentifier(),
					ms.getParameterSet(),
					ms.getCriteriaSet(),
					ms.getThresholdSet(),
					ms.getEtalon(),
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
		// будем считать status == OK, если в Heap.ContextMeasurementSetup != null
		Heap.setContextMeasurementSetup(measurementSetup);
		Heap.notifyPrimaryMTMCUpdated();
	}
}

