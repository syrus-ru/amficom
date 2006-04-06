package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.util.Log;

public class SaveTestSetupCommand extends AbstractCommand
{
	public static final long CRITERIA = 0x00000001;
	public static final long ETALON = 0x00000002;

	private ApplicationContext aContext; // unused?

	private long type; 

	public SaveTestSetupCommand(ApplicationContext aContext, long type) {
		this.aContext = aContext;
		this.type = type;
	}

	@Override
	public Object clone() {
		return new SaveTestSetupCommand(this.aContext, this.type);
	}

	public static boolean createNewMSAndSave(String name, long type) {
		MeasurementSetup msTest = Heap.getContextMeasurementSetup();

		if (msTest == null) {
			GUIUtil.showErrorMessage("noTestArgumentsError");
			return false;
		}

		// ���� ����������� ������, ��������� ��� �������
		if ((type & ETALON) != 0) {
			if (! Heap.hasEtalon()) {
				GUIUtil.showErrorMessage("noEtalonError");
				return false;
			}
		}

		final MeasurementSetup measurementSetup;

		try {
			// XXX: ���������� ME �� MS, ���� ���� ������ �� ���, � ����� �� Measurement'�
			MonitoredElement me = AnalysisUtil.getMEbyMS(msTest);
	
			// ������� ������ ������� � �������
			final Identifier analysisTemplateId =
				AnalysisUtil.createAnalysisTemplate(
						LoginManager.getUserId(),
						me.getMeasurementPort().getType(),
						msTest.getMonitoredElementIds(),
						(type & ETALON) != 0).getId();

			// ������� ���������� ������
			measurementSetup = MeasurementSetup.createInstance(
				LoginManager.getUserId(),
				msTest.getMeasurementTemplateId(),
				analysisTemplateId,
				name,
				msTest.getMonitoredElementIds());
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			GUIUtil.showCreateObjectProblemError();
			return false;
		}

		try {
			StorableObjectPool.flush(measurementSetup, LoginManager.getUserId(), false);
		} catch(ApplicationException ex) {
			ex.printStackTrace();
			GUIUtil.showErrorMessage(GUIUtil.MSG_ERROR_DATABASE_FLUSH_ERROR);
		}

		// ���������� �����, ��� ��� ������ �������� � �����������
		GUIUtil.showInfoMessage("testSetupSaved");

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

		if (createNewMSAndSave(name, this.type)) {
			Heap.setNewMSName(null); // success
		}
	}
}
