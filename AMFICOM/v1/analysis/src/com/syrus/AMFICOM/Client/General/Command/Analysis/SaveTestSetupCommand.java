package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
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
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
		if (bs == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("unkError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		if (bs.monitoredElementId == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noMonitoredElementError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		if (bs.measurementId == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noTestSetupError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
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
//
//		MeasurementSetup ms = m.getSetup();

		MeasurementSetup ms = (MeasurementSetup)Pool.get(AnalysisUtil.CONTEXT, "MeasurementSetup");
		if (ms.getParameterSet() == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noTestArgumentsError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
		ms.setCriteriaSet(AnalysisUtil.createCriteriaSetFromParams(user_id, ms.getMonitoredElementIds()));

		if ((type & ETALON) != 0 || (type & THRESHOLDS) != 0)
		{
			ModelTraceManager mtm = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, traceid);
			if (mtm == null)
			{
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						LangModelAnalyse.getString("noAnalysisError"),
						LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
				return;
			}
			if ((type & ETALON) != 0)
				ms.setEtalon(AnalysisUtil.createEtalon(user_id, ms.getMonitoredElementIds(), mtm));
			if ((type & THRESHOLDS) != 0)
				ms.setThresholdSet(AnalysisUtil.createThresholdSet(user_id, ms.getMonitoredElementIds(), mtm));
		}

		if (ms.getDescription().equals(""))
		{
			String s = JOptionPane.showInputDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("testsetup"),
					LangModelAnalyse.getString("newname"), JOptionPane.OK_CANCEL_OPTION);
			if (!MiscUtil.validName(s))
				return;
			ms.setDescription(s);
		}

		try
		{
			ms.attachToMonitoredElement(
					new Identifier(bs.monitoredElementId),
					new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id));
		}
		catch(UpdateObjectException ex)
		{
			ex.printStackTrace();
		}

		/**
		 * @todo use flush(false) to non forced saving
		 */
		try
		{
			MeasurementStorableObjectPool.flush(true);
		}
		catch(VersionCollisionException ex)
		{
		}
		catch(ApplicationException ex)
		{
		}

	}

}
