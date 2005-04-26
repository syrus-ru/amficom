package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.io.BellcoreStructure;

public class MinuitAnalyseCommand extends VoidCommand
{
	private ApplicationContext aContext;

	public MinuitAnalyseCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MinuitAnalyseCommand(aContext);
	}

	public void execute()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs != null)
		{
			Environment.getActiveWindow().setCursor(
				Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// double deltaX = bs.getResolution();
			double[] y = bs.getTraceData();

			AnalysisParameters ap = Heap.getMinuitAnalysisParams();
			if (ap == null) {
				new ClientAnalysisManager();
				ap = Heap.getMinuitAnalysisParams();
			}

			// XXX: ������� �������� ������ ��� ����� primary trace, � �����, ���������� �� �����, �� ����� ������ �/� - ��� �������� �������
			
			// ������� ������ ��� primary trace
			// XXX: ���� InitialAnalysisCommand ��� ��������, �� � �������� �� ����� (���� ������ "IA" � "MA" ����� ��������)

			ModelTraceAndEventsImpl mtaePri =
				CoreAnalysisManager.makeAnalysis(bs, ap);

			RefAnalysis a = new RefAnalysis();
			a.decode(y, mtaePri);

			Heap.setRefAnalysisByKey(Heap.PRIMARY_TRACE_KEY, a);
			Heap.setMTAEPrimary(mtaePri);

			// �������� ������ ��� ����� ������ �/�

			ModelTraceManager mtm;
			try {
				mtm = CoreAnalysisManager.makeEtalon(Heap.getBSCollection(), ap);
			} catch (IncompatibleTracesException e){
				GUIUtil.showErrorMessage("incompatibleTraces");
				return;
			}

			Heap.setMTMEtalon(mtm);

			Environment.getActiveWindow().setCursor(
				Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
