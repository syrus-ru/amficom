package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.io.BellcoreStructure;

public class SaveTestSetupAsCommand extends VoidCommand
{
	private ApplicationContext aContext;
	private long type;

	public SaveTestSetupAsCommand(ApplicationContext aContext, long type)
	{
		this.aContext = aContext;
		this.type = type;
	}

	public Object clone()
	{
		return new SaveTestSetupAsCommand(aContext, type);
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

		if (((type & SaveTestSetupCommand.ETALON) != 0 ||
				 (type & SaveTestSetupCommand.THRESHOLDS) != 0) &&
				 !Heap.hasEventParamsForPrimaryTrace())
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelAnalyse.getString("noAnalysisError"),
					LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		CreateTestSetupCommand command = new CreateTestSetupCommand(aContext);
		command.execute();
		MeasurementSetup newms = Heap.getContextMeasurementSetup();
		if (newms == null)
			return;

		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
		ModelTraceManager mtm = Heap.getMTMByKey(Heap.PRIMARY_TRACE_KEY);
		newms.setCriteriaSet(AnalysisUtil.createCriteriaSetFromParams(
				userId,
				newms.getMonitoredElementIds()));
		newms.setEtalon(AnalysisUtil.createEtalon(
				userId,
				newms.getMonitoredElementIds(),
				mtm));

		if ((type & SaveTestSetupCommand.THRESHOLDS) != 0)
		{
			newms.setThresholdSet(AnalysisUtil.createThresholdSet(
					userId,
					newms.getMonitoredElementIds(),
					mtm));
		}

		new SaveTestSetupCommand(aContext, type).execute();

		aContext.getDispatcher().notify(new RefChangeEvent(RefUpdateEvent.PRIMARY_TRACE,
				RefChangeEvent.THRESHOLDS_CALC_EVENT));
	}
}
