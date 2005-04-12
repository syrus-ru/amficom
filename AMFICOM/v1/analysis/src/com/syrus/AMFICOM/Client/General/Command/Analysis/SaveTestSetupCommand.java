package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.BellcoreStructure;

public class SaveTestSetupCommand extends VoidCommand
{
	public static final long CRITERIA = 0x00000001;
	public static final long ETALON = 0x00000002;
	//public static final long THRESHOLDS = 0x00000004;

	private ApplicationContext aContext;

	private long type;

	public SaveTestSetupCommand(ApplicationContext aContext, long type)
	{
		this.aContext = aContext;
		this.type = type;
	}

	public Object clone()
	{
		return new SaveTestSetupCommand(aContext, type);
	}

	public void execute()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
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

		MeasurementSetup ms = Heap.getContextMeasurementSetup();
		if (ms.getParameterSet() == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noTestArgumentsError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		Identifier user_id = new Identifier(((RISDSessionInfo)aContext
				.getSessionInterface()).getAccessIdentifier().user_id);
		try
		{
			ms.setCriteriaSet(AnalysisUtil.createCriteriaSetFromParams(user_id, ms.getMonitoredElementIds()));
		} catch (ApplicationException e)
		{
			System.err.println("SaveTestSetupCommand: ApplicationException (criterias)");
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("createObjectProblem"),
				LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		if ((type & ETALON) != 0)
		{
			ModelTraceManager mtm = Heap.getMTMEtalon();
			if (mtm == null)
			{
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						LangModelAnalyse.getString("noAnalysisError"),
						LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
				return;
			}
			try
			{
				ms.setEtalon(AnalysisUtil.createEtalon(user_id, ms.getMonitoredElementIds(), mtm));
			} catch (ApplicationException e1)
			{
				System.err.println("SaveTestSetupCommand: ApplicationException (etalon)");
				JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("createObjectProblem"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
				return;
			}
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

		ms.attachToMonitoredElement(new Identifier(bs.monitoredElementId));

		/**
		 * @todo use flush(false) to non forced saving
		 */
		try
		{
			MeasurementStorableObjectPool.flush(true);
		}
		catch(VersionCollisionException ex)
		{ // FIXME: process exception
		}
		catch(ApplicationException ex)
		{ // FIXME: process exception
		}

	}

}
