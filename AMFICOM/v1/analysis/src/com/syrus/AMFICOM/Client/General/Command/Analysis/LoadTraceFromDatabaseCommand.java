package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.UI.TraceLoadDialog;
import com.syrus.AMFICOM.analysis.dadara.DataFormatException;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.io.*;

public class LoadTraceFromDatabaseCommand extends AbstractCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public LoadTraceFromDatabaseCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new LoadTraceFromDatabaseCommand(dispatcher, aContext);
	}

	public void execute()
	{
//		ReflectogrammLoadDialog dialog;
		JFrame parent = Environment.getActiveWindow();

//		if(Heap.getRLDialogByKey(parent.getName()) != null)
//		{
//			dialog = Heap.getRLDialogByKey(parent.getName());
//		}
//		else
//		{
//			dialog = new ReflectogrammLoadDialog (aContext);
//			Heap.setRLDialogByKey(parent.getName(), dialog);
//		}

		//Environment.getActiveWindow()

		if(TraceLoadDialog.showDialog(parent) == JOptionPane.CANCEL_OPTION)
			return;
			
		
		Set<Result> results = TraceLoadDialog.getResults();
		if (results.isEmpty())
			return;

		Result result1 = results.iterator().next();
		BellcoreStructure bs = null;
		

		Parameter[] parameters = result1.getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter param = parameters[i];
			ParameterType type = (ParameterType)param.getType();
			if (type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
				bs = new BellcoreReader().getData(param.getValue());
		}
		if (bs == null)
			return; // FIXME: exceptions/error handling: �������� �������� �� ������

		if (!Heap.hasEmptyAllBSMap())
		{
			if (Heap.getBSPrimaryTrace() != null)
				new FileCloseCommand(aContext).execute();
		}
		Heap.setBSPrimaryTrace(bs);

		if (result1.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
			Measurement m = (Measurement)result1.getAction();
//			Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
			bs.title = m.getName();
			bs.monitoredElementId = m.getMonitoredElementId().getIdentifierString();
	
			bs.measurementId = m.getId().getIdentifierString();
			MeasurementSetup ms = m.getSetup();
			Heap.setContextMeasurementSetup(ms);

            try {
    			AnalysisUtil.load_CriteriaSet(LoginManager.getUserId(), ms);
    
    			if (ms.getEtalon() != null)
    				AnalysisUtil.load_Etalon(ms);
    			else
    			{
    				Heap.unSetEtalonPair();
    			}
    			Heap.makePrimaryAnalysis();
            } catch (DataFormatException e) {
                GUIUtil.showDataFormatError();
                return;
            }
		}
		Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
