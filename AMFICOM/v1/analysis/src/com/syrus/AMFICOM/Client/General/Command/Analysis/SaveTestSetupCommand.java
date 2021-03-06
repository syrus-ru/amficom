package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.ParameterSet;

public class SaveTestSetupCommand extends AbstractCommand
{
	public static final long CRITERIA = 0x00000001;
	public static final long ETALON = 0x00000002;

	private ApplicationContext aContext;

	private long type; 

	public SaveTestSetupCommand(ApplicationContext aContext, long type) {
		this.aContext = aContext;
		this.type = type;
	}

	@Override
	public Object clone() {
		return new SaveTestSetupCommand(this.aContext, this.type);
	}

	public static boolean createNewMSAndSave(String name, ApplicationContext aContext, long type) {
		MeasurementSetup msTest = Heap.getContextMeasurementSetup();

		// XXX: ????? ?? ????? ??? ?????????
		if (msTest.getParameterSet() == null) {
			GUIUtil.showErrorMessage("noTestArgumentsError");
			return false;
		}

		// XXX: ???? ?????????? ??? ??? -- saa
		if (msTest.getDescription().equals("")) {
			String s = JOptionPane.showInputDialog(Environment.getActiveWindow(),
					LangModelAnalyse.getString("testsetup"),
					LangModelAnalyse.getString("newname"),
					JOptionPane.OK_CANCEL_OPTION);
			msTest.setDescription(s); // FIXME: ???? ?????? ?? ??????(?)
		}

		// ???? ??????????? ??????, ????????? ??? ???????
		if ((type & ETALON) != 0) {
			if (! Heap.hasEtalon()) {
				GUIUtil.showErrorMessage("noEtalonError");
				return false;
			}
		}

		// ??????? ????? MS

		ParameterSet criteriaSet = null;
		try {
			if ((type & CRITERIA) != 0) {
				criteriaSet = AnalysisUtil.createCriteriaSet(LoginManager.getUserId(), msTest.getMonitoredElementIds());
			} else {
				// ??? ????? ?? ????????????
				criteriaSet = null;//msTest.getCriteriaSet();
			}
		} catch (ApplicationException e) {
			System.err.println("SaveTestSetupCommand: ApplicationException (criterias):");
			e.printStackTrace();
			GUIUtil.showCreateObjectProblemError();
			return false;
		}

		ParameterSet etalonSet = null;
		try {
			if ((type & ETALON) != 0) {
				// ????????? ?????????? ???????
				// ????? ?? ??? ?????????, ??? ????????? MTM ????
				etalonSet = AnalysisUtil.createEtalon(LoginManager.getUserId(), msTest.getMonitoredElementIds());
			} else {
				// ???? ?????????? ??????? ?? ?????????, ??? ???? ??????? ???? ???? ?? ????
				etalonSet = null;
			}
		} catch (ApplicationException e1) {
			System.err.println("SaveTestSetupCommand: ApplicationException (etalon): ");
			e1.printStackTrace();
			GUIUtil.showCreateObjectProblemError();
			return false;
		}

		try {
			final MeasurementSetup measurementSetup =
				MeasurementSetup.createInstance(
					LoginManager.getUserId(),
					msTest.getParameterSet(),
					criteriaSet,
					msTest.getThresholdSet(),
					etalonSet,
					name,
					msTest.getMeasurementDuration(),
					msTest.getMonitoredElementIds(),
					msTest.getMeasurementTypes());
			StorableObjectPool.flush(measurementSetup, LoginManager.getUserId(), false);
		} catch (ApplicationException e) {
			e.printStackTrace();
			GUIUtil.showCreateObjectProblemError();
			return false;
		}

		// ?????????? ?????, ??? ??? ?????? ???????? ? ???????????
		// @todo: use GUIUtil.showInfoMessage instead of direct call to JOptionPane
		JOptionPane.showMessageDialog(Environment.getActiveWindow(),
				LangModelAnalyse.getString("testSetupSaved"),
				LangModelAnalyse.getString("ok"),
				JOptionPane.INFORMATION_MESSAGE);

		return true;
	}

	public static boolean checkStrangeConditions() {
		PFTrace pf = Heap.getPFTracePrimary();
		if (pf == null)
		{
			GUIUtil.showErrorMessage("unkError");
			return false;
		}

		if (pf.getBS().monitoredElementId == null)
		{
			GUIUtil.showErrorMessage("noMonitoredElementError");
			return false;
		}

		if (pf.getBS().measurementId == null)
		{
			GUIUtil.showErrorMessage("noTestSetupError");
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if (checkStrangeConditions() == false) {
			return;
		}

		final String name = Heap.getNewMSName();
		if (name == null) {
			GUIUtil.showErrorMessage("noTestSetupNameError");
			return;
		}

		if (createNewMSAndSave(name, this.aContext, this.type)) {
			Heap.setNewMSName(null); // success
		}
	}
}
