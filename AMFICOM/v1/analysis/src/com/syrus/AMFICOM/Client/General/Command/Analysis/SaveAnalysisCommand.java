package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.*;

public class SaveAnalysisCommand extends VoidCommand
{
	private ApplicationContext aContext;

	public SaveAnalysisCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SaveAnalysisCommand(aContext);
	}

	public void execute()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
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

		RefAnalysis refanalysis = Heap.getRefAnalysisPrimary();
		ModelTraceAndEventsImpl mtae = Heap.getMTAEPrimary();

		Measurement m = null;
		try
		{
			m = (Measurement)MeasurementStorableObjectPool.getStorableObject(
						 new Identifier(bs.measurementId), true);
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception retrieving measurenent with " + bs.measurementId);
			ex.printStackTrace();
			return;
		}

		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);

		Analysis a = null;
		AnalysisType type;
		ParameterType ptype0 = null;
		ParameterType ptype1 = null;
		ParameterType ptype2 = null;
		try
		{
			type = AnalysisUtil.getAnalysisType(ParameterTypeCodenames.DADARA);
			ptype0 = AnalysisUtil.getParameterType(ParameterTypeCodenames.REFLECTOGRAMMA, DataType.DATA_TYPE_RAW);
			ptype1 = AnalysisUtil.getParameterType(ParameterTypeCodenames.TRACE_EVENTS, DataType.DATA_TYPE_RAW);
			ptype2 = AnalysisUtil.getParameterType(ParameterTypeCodenames.DADARA_MTAE, DataType.DATA_TYPE_RAW);
		} catch (ApplicationException e) {
			// FIXME: add a better error processing
			System.err.println("SaveAnalysisCommand: Application exception while getAnalysisType");
			e.printStackTrace();
			return;
		}

		try
		{
			a = Analysis.createInstance( // FIXME: does this code has any effect?
				userId,
				type,
				new Identifier(bs.monitoredElementId),
				m,
				"DADARA Analysis", // @todo: give a better name
				m.getSetup().getCriteriaSet());
		} catch (CreateObjectException e1) {
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("createObjectProblem"), // FIXME: add this string
				LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

		SetParameter[] params = new SetParameter[3];
		try
		{
			params[0] = SetParameter.createInstance(ptype0,
					new BellcoreWriter().write(bs));

			ByteArrayCollector bac = new ByteArrayCollector();
			for(int i = 0; i < refanalysis.events.length; i++)
			{
				byte[] b = refanalysis.events[i].toByteArray();
				bac.add(b);
			}
			params[1] = SetParameter.createInstance(ptype1,
					bac.encode());

			params[2] = SetParameter.createInstance(ptype2,
				DataStreamableUtil.writeDataStreamableToBA(mtae));
		}
		catch (CreateObjectException e)
		{
			System.err.println("SaveAnalysisCommand: CreateObjectException.");
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("createObjectProblem"),
				LangModelAnalyse.getString("error"), JOptionPane.OK_OPTION);
			return;
		}

// FIXME: should be uncommented and fixed; hidden by saa because of modified module measurement_v1
//		try
//		{
//			a.createResult(
//					userId,
//					m,
//					AlarmLevel.ALARM_LEVEL_NONE,
//					params);
//			MeasurementStorableObjectPool.putStorableObject(a);
//			MeasurementStorableObjectPool.flush(true);
//		}
//		catch(ApplicationException ex)
//		{
//			ex.printStackTrace();
//		}
	}
}
