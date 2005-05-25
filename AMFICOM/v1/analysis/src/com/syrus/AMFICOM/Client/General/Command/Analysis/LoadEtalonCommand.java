package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.BellcoreStructure;

public class LoadEtalonCommand extends AbstractCommand
{
	public LoadEtalonCommand()
	{ // empty
	}

	public Object clone()
	{
		return new LoadEtalonCommand();
	}

	public void execute()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs.measurementId == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noTestSetupError"),
					LangModelAnalyse.getString("error"),
					JOptionPane.OK_OPTION);
			return;
		}

//		Measurement m = null;
//		try
//		{
//			m = (Measurement)MeasurementStorableObjectPool.getStorableObject(
//						 new Identifier(bs.measurementId), true);
//		}
//		catch(ApplicationException ex)
//		{
//			System.err.println("Exception retrieving measurenent with " + bs.measurementId);
//			ex.printStackTrace();
//			return;
//		}

		MeasurementSetup ms = Heap.getContextMeasurementSetup();
		if (ms != null)
		if (ms.getEtalon() == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noEtalonError"),
					LangModelAnalyse.getString("error"),
					JOptionPane.OK_OPTION);
			return;
		}

		// Heap.etalonMTMCUpdated();
	}
}
