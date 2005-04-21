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

	public static boolean createNewMSAndSave(String name, ApplicationContext aContext, long type) {
		MeasurementSetup msTest = Heap.getContextMeasurementSetup();

		// XXX: нужна ли здесь эта проверка?
		if (msTest.getParameterSet() == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noTestArgumentsError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return false;
		}

		// XXX: вижу непонятный мне код -- saa
		if (msTest.getDescription().equals(""))
		{
			String s = JOptionPane.showInputDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("testsetup"),
					LangModelAnalyse.getString("newname"), JOptionPane.OK_CANCEL_OPTION);
			if (!MiscUtil.validName(s))
				return false;
			msTest.setDescription(s); // FIXME: этак делать не велено(?)
		}

		// создаем новый MS

		Identifier userId = new Identifier(((RISDSessionInfo)aContext
				.getSessionInterface()).getAccessIdentifier().user_id);

		Set criteriaSet = null;
		try
		{
			if ((type & CRITERIA) != 0)
				criteriaSet = AnalysisUtil.createCriteriaSetFromParams(userId, msTest.getMonitoredElementIds());
			else
				criteriaSet = msTest.getCriteriaSet();
		} catch (ApplicationException e)
		{
			System.err.println("SaveTestSetupCommand: ApplicationException (criterias)");
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("createObjectProblem"),
				LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return false;
		}

		Set etalonSet = null;
		try
		{
			if ((type & ETALON) != 0)
			{
				ModelTraceManager mtm = Heap.getMTMEtalon();
				if (mtm == null)
				{
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModelAnalyse.getString("noAnalysisError"),
							LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
					return false;
				}
				etalonSet = AnalysisUtil.createEtalon(userId, msTest.getMonitoredElementIds(), mtm);
			}
			else
				etalonSet = msTest.getEtalon();
		} catch (ApplicationException e1)
		{
			System.err.println("SaveTestSetupCommand: ApplicationException (etalon)");
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("createObjectProblem"),
				LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return false;
		}

		MeasurementSetup measurementSetup;
		try
		{
			measurementSetup = MeasurementSetup.createInstance(
					((RISDSessionInfo)aContext.getSessionInterface()).getUserIdentifier(),
					msTest.getParameterSet(),
					criteriaSet,
					msTest.getThresholdSet(),
					etalonSet,
					name,
					msTest.getMeasurementDuration(),
					msTest.getMonitoredElementIds(),
                    msTest.getMeasurementTypeIds());
			MeasurementStorableObjectPool.putStorableObject(measurementSetup);
		}
		catch (CreateObjectException e)
		{
			// FIXME: CreateObjectException
			System.err.println("CreateTestSetupCommand: CreateObjectException.");
			e.printStackTrace();
			return false;
		}
		catch(IllegalObjectEntityException e)
		{
			// FIXME: IllegalObjectEntityException
			System.err.println("CreateTestSetupCommand: IllegalObjectEntityException.");
			e.printStackTrace();
			return false;
		}

		BellcoreStructure bs = Heap.getBSPrimaryTrace();

		if (bs != null)
		{
			// XXX: вижу непонятный мне код -- saa
			measurementSetup.attachToMonitoredElement(new Identifier(bs.monitoredElementId));
		}

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

		return true;
	}

	public static boolean checkStrangeConditions() {
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("unkError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return false;
		}

		if (bs.monitoredElementId == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noMonitoredElementError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return false;
		}

		if (bs.measurementId == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noTestSetupError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return false;
		}
		return true;
	}

	public void execute()
	{
		if (checkStrangeConditions() == false)
			return;

		String name = Heap.getNewMSName();
		if (name == null)
		{
			System.err.println("no name for creating new TestSetup");
			// @todo: process newMSName == null -- saa
			return;
		}

		if (createNewMSAndSave(name, aContext, type))
			Heap.setNewMSName(null); // success
	}
}
