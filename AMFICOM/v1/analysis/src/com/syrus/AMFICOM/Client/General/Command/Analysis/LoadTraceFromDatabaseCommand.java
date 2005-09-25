package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.util.Collection;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Trace;
import com.syrus.AMFICOM.Client.Analysis.UI.TraceLoadDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.DataFormatException;

public class LoadTraceFromDatabaseCommand extends AbstractCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public LoadTraceFromDatabaseCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		System.out.println("LoadTraceFromDatabaseCommand constructor");
		this.dispatcher = dispatcher;
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
		return new LoadTraceFromDatabaseCommand(this.dispatcher, this.aContext);
	}

	@Override
	public void execute()
	{
		// �������� ����� �����������, ������� ���� ���������
		JFrame parent = Environment.getActiveWindow();
		if(TraceLoadDialog.showDialog(parent) == JOptionPane.CANCEL_OPTION)
			return;
		Set<Result> results = TraceLoadDialog.getResults();

		// XXX: ���� ����� ����������� ����, ������ �� ������
		if (results.isEmpty())
			return;

		// ����������� ��������� ����� ����������� � ����� �������������
		Collection<Trace> traces;
		try {
			traces = AddTraceFromDatabaseCommand.getTracesFromResults(results);
		} catch (DataFormatException e) {
			// ������ ������� ������ - �������� ��������
			GUIUtil.showDataFormatError();
			return;
		} catch (ApplicationException e) {
			GUIUtil.processApplicationException(e);
			return;
		}
		// ��������� ��������� ����� �������������
		Heap.openManyTraces(traces);

		// ���� ��������� ���������� primaryTrace ������� � ���������� ���������,
		// �� ������������� ms �, ���� ����, ������
		// �������� ����� ���������
		// XXX: ���, ��� ������� ������, ��������� ������������ ���������
		// ������������ �������������� � ������� MS ��� ���� ������ ������������
		// (��� ���� ���� �� ��� ����� ������� ���������, � � �������� MS
		// ����� ������ �� MS), ����� ������������� ��������� �/������
		// � ������� ���� (��� ���� MS ��������� �� ������ ���������),
		// � ����� ��������� ������. ��� ���� ����� ������ ������ �
		// MS ������ ��������� �/�, �� �� ������ ����� ��������� �/�.
		// ������, ��� ���� ���������, ��������, ����� �� ����� ��������:
		// 1. ��������� ��� ����������� �/� (��� � OPEN, ��� � � ADD).
		//    ���� ��� �� ������ MS,
		//    �� ������� MS:=null (� ��������������� ���� ��� ����);
		// 2. ��� ����� ��������� �/� �������� MS (����� ������� � ��������?);
		// 3. ������������ MS ��������� �/�, � �� ����������� �������� MS
		//    ������ �����������;
		// 4. ��� �������� (��� ����������) ������� ������������� ������������,
		//    ��� ���� ��������� MS, � �������������,
		//    ��� ����� ���� MS ���������.
		Result primaryTraceResult = Heap.getPrimaryTrace().getResult();
		if (primaryTraceResult != null
				&& AnalysisUtil.hasMeasurementByResult(primaryTraceResult)) {
			Measurement m = AnalysisUtil.getMeasurementByResult(primaryTraceResult);
			MeasurementSetup ms = m.getSetup();
			Heap.setContextMeasurementSetup(ms);

			try {
				AnalysisUtil.loadCriteriaSet(LoginManager.getUserId(), ms);
				if (ms.getEtalon() != null) {
					AnalysisUtil.loadEtalon(ms);
				} else {
					Heap.unSetEtalonPair();
				}
				Heap.makePrimaryAnalysis();
			} catch (DataFormatException e) {
				GUIUtil.showDataFormatError();
			} catch (ApplicationException e) {
				GUIUtil.processApplicationException(e);
			}
		}
		Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
