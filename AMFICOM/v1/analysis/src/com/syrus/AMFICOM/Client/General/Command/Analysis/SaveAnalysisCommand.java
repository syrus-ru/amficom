package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.*;

public class SaveAnalysisCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	String traceid;

	public SaveAnalysisCommand()
	{
	}

	public SaveAnalysisCommand(Dispatcher dispatcher, ApplicationContext aContext, String id)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
		this.traceid = id;
	}

	public Object clone()
	{
		return new SaveAnalysisCommand(dispatcher, aContext, traceid);
	}

	public void execute()
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", traceid);
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

		RefAnalysis refanalysis = (RefAnalysis)Pool.get("refanalysis", traceid);
		ModelTraceManager mtm = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, traceid);

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
//		String s = JOptionPane.showInputDialog(
//				Environment.getActiveWindow(),
//				LangModelAnalyse.getString("newname"),
//				LangModelAnalyse.getString("analysis"),
//				JOptionPane.OK_CANCEL_OPTION);
//		if (s == null || s.equals(""))
//			return;
//		a.setName(s);

		AnalysisType type = AnalysisUtil.getAnalysisType(userId, ParameterTypeCodenames.DADARA);
		Analysis a = null;
		try
		{
			a = Analysis.createInstance(
					userId,
					type,
					new Identifier(bs.monitoredElementId),
					m.getSetup().getCriteriaSet());
		}
		catch(CreateObjectException ex)
		{
			ex.printStackTrace();
			return;
		}

		SetParameter[] params = new SetParameter[3];
		try
		{
			ParameterType ptype = AnalysisUtil.getParameterType(userId, ParameterTypeCodenames.REFLECTOGRAMMA);
			params[0] = SetParameter.createInstance(ptype,
					new BellcoreWriter().write(bs));
	
			ByteArrayCollector bac = new ByteArrayCollector();
			for(int i = 0; i < refanalysis.events.length; i++)
			{
				byte[] b = refanalysis.events[i].toByteArray();
				bac.add(b);
			}
			ptype = AnalysisUtil.getParameterType(userId, ParameterTypeCodenames.TRACE_EVENTS);
			params[1] = SetParameter.createInstance(ptype,
					bac.encode());
	
			ptype = AnalysisUtil.getParameterType(userId, ParameterTypeCodenames.DADARA_EVENTS);
			params[2] = SetParameter.createInstance(ptype,
				mtm.toEventsByteArray());
		}
	    catch (CreateObjectException e)
	    {
		    // FIXME
		    System.err.println("SaveAnalysisCommand: CreateObjectException.");
			e.printStackTrace();
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