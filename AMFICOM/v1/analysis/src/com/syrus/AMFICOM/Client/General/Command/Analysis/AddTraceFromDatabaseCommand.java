package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Trace;
import com.syrus.AMFICOM.Client.Analysis.UI.TraceLoadDialog;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.measurement.Result;

public class AddTraceFromDatabaseCommand extends AbstractCommand
{
	private ApplicationContext aContext;

	public AddTraceFromDatabaseCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	@Override
	public void setParameter(String field, Object value)
	{
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	@Override
	public Object clone()
	{
		return new AddTraceFromDatabaseCommand(this.aContext);
	}

	@Override
	public void execute()
	{
		// @todo: ReflectogramLoadDialog, Heap.setRLDialogByKey seem to be unused
//		ReflectogrammLoadDialog dialog;
//		JFrame parent = Environment.getActiveWindow();
//		if(Heap.getRLDialogByKey(parent.getName()) != null)
//		{
//			dialog = Heap.getRLDialogByKey(parent.getName());
//		} else
//		{
//			dialog = new ReflectogrammLoadDialog (this.aContext);
//			Heap.setRLDialogByKey(parent.getName(), dialog);
//		}
//		
//		if(dialog.showDialog() == JOptionPane.CANCEL_OPTION)
//			return;
//		
//		Result result1 = dialog.getResult();
//		if (result1 == null)
//			return;

		// Получаем набор результатов, которые надо загрузить
		JFrame parent = Environment.getActiveWindow();
		if(TraceLoadDialog.showDialog(parent) == JOptionPane.CANCEL_OPTION)
			return;
		Set<Result> results = TraceLoadDialog.getResults();

//		int totalCounter = 0;
//		int failureCounter = 0;
//		boolean hasLoadProblems = false;
//		boolean hasAlreadyLoadedProblem = false;
		for (Result result1: results) {
			Trace tr;
			try {
				tr = new Trace(result1, Heap.getMinuitAnalysisParams());
			} catch (SimpleApplicationException e) {
				// ошибка - в результатах анализа нет рефлектограммы
				continue; // FIXME: exceptions/error handling: считать число ошибок, выдавать собщение об ошибке
			}
	
			if (Heap.hasSecondaryBSKey(tr.getKey())
					|| Heap.getPrimaryTrace().getKey().equals(tr.getKey())) {
				continue; // FIXME: exceptions/error handling: считать число ошибок, выдавать сообщения "некоторые или все рефлектограммы уже загружены"
			}
	
			Heap.putSecondaryTrace(tr);
			Heap.setCurrentTrace(tr.getKey());
		}
	}
}
