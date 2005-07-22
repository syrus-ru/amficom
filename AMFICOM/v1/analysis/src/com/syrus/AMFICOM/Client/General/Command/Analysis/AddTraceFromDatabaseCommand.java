package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
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

	/**
	 * �������� ����� Traces �� ������ ����������� ���������.
	 * ������� ��� ����������� ��������� �� �������.
	 * @param results
	 * @return ����� Traces, ��������, ������
	 * (���� ���� �������� ����� ����������� ���� �� ������� ��������� �� ����
	 * ��������������)
	 */
	public static Set<Trace> getTracesFromResults(Set<Result> results) {
		Set<Trace> traces = new HashSet<Trace>(results.size());
		int totalCounter = 0;
		int loadProblemsCounter = 0;
		for (Result result1: results) {
			totalCounter++;
			try {
				traces.add(new Trace(result1, Heap.getMinuitAnalysisParams()));
			} catch (SimpleApplicationException e) {
				// ������ - � ����������� ������� ��� ��������������
				loadProblemsCounter++;
			}
		}

		// ������������ ������ ��������
		if (loadProblemsCounter != 0) {
			if (loadProblemsCounter == totalCounter) {
				// ��� �������� ���� ��� ��������� �����������, �� �� � ����� ��� ��������������
				GUIUtil.showErrorMessage(GUIUtil.MSG_ERROR_NO_ONE_RESULT_HAS_TRACE);
			} else {
				// ��������� ������� ���������, � ��������� - ���
				GUIUtil.showWarningMessage(GUIUtil.MSG_WARNING_SOME_RESULTS_HAVE_NO_TRACE);
			}
		}

		return traces;
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

		// �������� ����� �����������, ������� ���� ���������
		JFrame parent = Environment.getActiveWindow();
		if(TraceLoadDialog.showDialog(parent) == JOptionPane.CANCEL_OPTION)
			return;
		Set<Result> results = TraceLoadDialog.getResults();
		Set<Trace> traces = getTracesFromResults(results);

		int alreadyLoadedCounter = 0;
		for (Trace tr: traces) {
			if (Heap.hasSecondaryBSKey(tr.getKey())
					|| Heap.getPrimaryTrace().getKey().equals(tr.getKey())) {
				alreadyLoadedCounter++;
			} else {
				Heap.putSecondaryTrace(tr);
				Heap.setCurrentTrace(tr.getKey());
			}
		}

		if (alreadyLoadedCounter != 0) {
			if (alreadyLoadedCounter == traces.size()) {
				// ��� ����������� �������������� ��� ���������
				GUIUtil.showErrorMessage(GUIUtil.MSG_ERROR_TRACE_ALREADY_LOADED);
			} else {
				// ��������� ����������� �������������� ��� ���������
				GUIUtil.showWarningMessage(GUIUtil.MSG_WARNING_SOME_TRACES_ARE_ALREADY_LOADED);
			}
		}
	}
}
