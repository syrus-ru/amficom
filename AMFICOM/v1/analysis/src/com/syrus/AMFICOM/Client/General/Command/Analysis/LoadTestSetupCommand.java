package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class LoadTestSetupCommand extends AbstractCommand
{
	private ApplicationContext aContext;

	public LoadTestSetupCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	@Override
	public Object clone()
	{
		return new LoadTestSetupCommand(this.aContext);
	}

	@Override
	public void execute()
	{
	/*	BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs == null || bs.monitoredElementId == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noMonitoredElementError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		TestSetupLoadDialog dialog = new TestSetupLoadDialog ();
		dialog.show();

		if(dialog.ret_code == 0)
			return;
//		if (!(dialog.resource instanceof MeasurementSetup))
//			return;

		MeasurementSetup ms = dialog.resource;
		Heap.setContextMeasurementSetup(ms);

//		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
//		bs.test_setup_id = ts.getId();

		if (Heap.hasEventParamsForEtalonTrace()) // ���� ������ ���� (��� ������) - �� �������
		{
			Heap.notifyBsHashRemove(Heap.ETALON_TRACE_KEY); // XXX: ���������� ��� ��� � ��� ������, ����� ������ �� ������
			Heap.setCurrentTracePrimary();
		}

		try {
			AnalysisUtil.load_CriteriaSet(LoginManager.getUserId(), ms);
	
			if (ms.getEtalon() != null)
				AnalysisUtil.load_Etalon(ms);
		} catch (DataFormatException e) {
			GUIUtil.showDataFormatError();
		}
//
//		if (ms.getThresholdSet() != null)
//			AnalysisUtil.load_Thresholds(userId, ms);

		// XXX: are these notifications needed?
		Heap.notifyPrimaryTraceClosed();
		Heap.notifyPrimaryTraceOpened();
		Heap.setCurrentTracePrimary();*/
	}
}
