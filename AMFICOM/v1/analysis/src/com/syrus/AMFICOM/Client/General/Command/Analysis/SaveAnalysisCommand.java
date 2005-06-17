package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.io.ByteArrayCollector;

public class SaveAnalysisCommand extends AbstractCommand
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
        if (true) // FIXME: UnsupportedOperationException
            throw new UnsupportedOperationException("The current version cannot save RefAnalysis.events");
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
			m = (Measurement)StorableObjectPool.getStorableObject(
						 new Identifier(bs.measurementId), true);
		} catch(ApplicationException ex)
		{
			System.err.println("Exception retrieving measurenent with " + bs.measurementId);
			ex.printStackTrace();
			return;
		}

//		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);

		Analysis a = null;
		AnalysisType type;
		ParameterType ptype0 = null;
		ParameterType ptype1 = null;
		try
		{
			type = AnalysisUtil.getAnalysisType(ParameterTypeCodenames.DADARA);
			ptype0 = AnalysisUtil.getParameterType(ParameterTypeCodenames.REFLECTOGRAMMA, DataType.DATA_TYPE_RAW);
			ptype1 = AnalysisUtil.getParameterType(ParameterTypeCodenames.DADARA_MTAE, DataType.DATA_TYPE_RAW);
		} catch (ApplicationException e) {
			// FIXME: exceptions: add a better error processing
			System.err.println("SaveAnalysisCommand: Application exception while getAnalysisType");
			e.printStackTrace();
			return;
		}

		try
		{
			a = Analysis.createInstance( // FIXME: does this code has any effect?
				LoginManager.getUserId(),
				type,
				new Identifier(bs.monitoredElementId),
				m,
				"DADARA Analysis", // @todo: give a better name
				m.getSetup().getCriteriaSet());
		} catch (CreateObjectException e1) {
            GUIUtil.showCreateObjectProblemError();
			return;
		}

		Parameter[] params = new Parameter[2];
		try
		{
			params[0] = Parameter.createInstance(ptype0,
					new BellcoreWriter().write(bs));

			params[1] = Parameter.createInstance(ptype1,
				DataStreamableUtil.writeDataStreamableToBA(mtae));
		} catch (CreateObjectException e)
		{
			System.err.println("SaveAnalysisCommand: CreateObjectException.");
            GUIUtil.showCreateObjectProblemError();
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
