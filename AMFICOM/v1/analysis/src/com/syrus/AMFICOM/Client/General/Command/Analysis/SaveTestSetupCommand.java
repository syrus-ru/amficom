package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.io.BellcoreStructure;

public class SaveTestSetupCommand extends AbstractCommand
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
			GUIUtil.showErrorMessage("noTestArgumentsError");
			return false;
		}

		// XXX: вижу непонятный мне код -- saa
		if (msTest.getDescription().equals(""))
		{
			String s = JOptionPane.showInputDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("testsetup"),
					LangModelAnalyse.getString("newname"), JOptionPane.OK_CANCEL_OPTION);
			msTest.setDescription(s); // FIXME: этак делать не велено(?)
		}

		// создаем новый MS

		ParameterSet criteriaSet = null;
		try
		{
			if ((type & CRITERIA) != 0)
				criteriaSet = AnalysisUtil.createCriteriaSet(LoginManager.getUserId(), msTest.getMonitoredElementIds());
			else
				criteriaSet = msTest.getCriteriaSet();
		} catch (ApplicationException e)
		{
			System.err.println("SaveTestSetupCommand: ApplicationException (criterias)");
			GUIUtil.showCreateObjectProblemError();
			return false;
		}

		ParameterSet etalonSet = null;
		try
		{
			if ((type & ETALON) != 0)
			{
				ModelTraceManager mtm = Heap.getMTMEtalon();
				if (mtm == null)
				{
					// @todo: в этом случае (а тж в сл. ApplicationException) надо бы удалить созданный бесхозный criteriaSet)
					GUIUtil.showErrorMessage("noEtalonError");
					return false;
				}
				etalonSet = AnalysisUtil.createEtalon(LoginManager.getUserId(), msTest.getMonitoredElementIds(), mtm);
			} else
				etalonSet = msTest.getEtalon();
		} catch (ApplicationException e1)
		{
			System.err.println("SaveTestSetupCommand: ApplicationException (etalon)");
			GUIUtil.showCreateObjectProblemError();
			return false;
		}

		MeasurementSetup measurementSetup;
		try
		{
			measurementSetup = MeasurementSetup.createInstance(
					LoginManager.getUserId(),
					msTest.getParameterSet(),
					criteriaSet,
					msTest.getThresholdSet(),
					etalonSet,
					name,
					msTest.getMeasurementDuration(),
					msTest.getMonitoredElementIds(),
                    msTest.getMeasurementTypeIds());
			StorableObjectPool.putStorableObject(measurementSetup);
		} catch (CreateObjectException e)
		{
            GUIUtil.showCreateObjectProblemError();
			return false;
		} catch(IllegalObjectEntityException e)
		{
			// FIXME: exceptions: IllegalObjectEntityException
			System.err.println("CreateTestSetupCommand: IllegalObjectEntityException.");
			e.printStackTrace();
			return false;
		}

		/**
		 * @todo use flush(false) to non forced saving
		 */
		try
		{
			StorableObjectPool.flushGroup(
                    ObjectGroupEntities.MEASUREMENT_GROUP_CODE,
                    true);
		} catch(ApplicationException ex)
		{ // FIXME: exceptions: process exception
		}

		return true;
	}

	public static boolean checkStrangeConditions() {
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs == null)
		{
			GUIUtil.showErrorMessage("unkError");
			return false;
		}

		if (bs.monitoredElementId == null)
		{
			GUIUtil.showErrorMessage("noMonitoredElementError");
			return false;
		}

		if (bs.measurementId == null)
		{
			GUIUtil.showErrorMessage("noTestSetupError");
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
			// FIXME: exceptions/error handling: process newMSName == null -- saa
			return;
		}

		if (createNewMSAndSave(name, aContext, type))
			Heap.setNewMSName(null); // success
	}
}
