package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.io.BellcoreStructure;

public class LoadEtalonCommand extends VoidCommand
{
	ApplicationContext aContext;

	public LoadEtalonCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new LoadEtalonCommand(aContext);
	}

	public void execute()
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
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

		MeasurementSetup ms = (MeasurementSetup)Pool.get(AnalysisUtil.CONTEXT, "MeasurementSetup");
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

		aContext.getDispatcher().notify(new RefChangeEvent(AnalysisUtil.ETALON, RefChangeEvent.OPEN_ETALON_EVENT));
	}
}