package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.UI.TestSetupLoadDialog;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.io.BellcoreStructure;

public class LoadTestSetupCommand extends VoidCommand
{
	private ApplicationContext aContext;

	public LoadTestSetupCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new LoadTestSetupCommand(aContext);
	}

	public void execute()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs == null || bs.monitoredElementId == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noMonitoredElementError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		TestSetupLoadDialog dialog = new TestSetupLoadDialog (aContext);
		dialog.show();

		if(dialog.ret_code == 0)
			return;
//		if (!(dialog.resource instanceof MeasurementSetup))
//			return;

		MeasurementSetup ms = dialog.resource;
		Heap.setContextMeasurementSetup(ms);

		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
//		bs.test_setup_id = ts.getId();

		if (Heap.hasEventParamsForEtalonTrace()) // если эталон есть (уже открыт) - то закрыть
		{
			aContext.getDispatcher().notify(new RefChangeEvent(AnalysisUtil.ETALON, RefChangeEvent.CLOSE_EVENT));
			aContext.getDispatcher().notify(new RefChangeEvent(RefUpdateEvent.PRIMARY_TRACE, RefChangeEvent.SELECT_EVENT));
		}

		AnalysisUtil.load_CriteriaSet(userId, ms);

		if (ms.getEtalon() != null)
			AnalysisUtil.load_Etalon(ms);

		if (ms.getThresholdSet() != null)
			AnalysisUtil.load_Thresholds(userId, ms);

		aContext.getDispatcher().notify(new RefUpdateEvent(AnalysisUtil.ETALON,
				RefUpdateEvent.THRESHOLDS_UPDATED_EVENT));
		aContext.getDispatcher().notify(new RefChangeEvent(RefUpdateEvent.PRIMARY_TRACE,
				RefChangeEvent.THRESHOLDS_CALC_EVENT));
		aContext.getDispatcher().notify(new RefChangeEvent(RefUpdateEvent.PRIMARY_TRACE,
				RefChangeEvent.CLOSE_EVENT));
		aContext.getDispatcher().notify(new RefChangeEvent(RefUpdateEvent.PRIMARY_TRACE,
				RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
		aContext.getDispatcher().notify(new RefUpdateEvent(RefUpdateEvent.PRIMARY_TRACE,
				RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
	}
}
