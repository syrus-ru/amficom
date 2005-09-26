package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.util.Collection;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;

/**
 * ������� �������� �������.
 * ������ ���������� MinuitAnalysisCommand.
 * ������� Heap.MTMEtalon
 */
public class CreateEtalonCommand extends AbstractCommand
{
	public CreateEtalonCommand()
	{
		// empty
	}

	public void execute()
	{
		Collection<PFTrace> trColl = Heap.getBSCollection();

		// ���� �� ������� �� ���� �/�, ������ ���������� �������.
		// � ����� �������� ������� �� ������ ���� ����������,
		// ������� ������� user-friendly ��������� ���� ������ �� �����.
		if (trColl.isEmpty())
			return;

		Environment.getActiveWindow().setCursor(
			Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		AnalysisParameters ap = Heap.getMinuitAnalysisParams();
		if (ap == null) {
			new ClientAnalysisManager();
			ap = Heap.getMinuitAnalysisParams();
		}

		// �������� ������ ��� ����� ������ �/�

		ModelTraceManager mtm;
		PFTrace pf;
		try {
			if (Heap.hasEtalon()) { // ���� ������ ��� ����
				// ��������� ������ (�������� �� trColl ��������� �/�,
				// �� ������ ������ �� ���������, �.�. ��� ��� ������ ����
				// ������� ���� ��������)
				mtm = Heap.getMTMEtalon();
				CoreAnalysisManager.updateEtalon(mtm, trColl, ap);
				// �������� �������� ��������� Heap, ������ ��� ������ � bsColl,
				// ������� ��
				// �������� �������� �������� � ������ ������������� �������.
				//
				// �� ����� ���� ��� �� ����� �����, � ���������� ���� ��
				// ��������� ������ � �����, ������ ���������� �������������,
				// �� ������� �� ��� ������, �� ������� � ������ ������ ��
				// ���������.
				//
				// � ������ �������, ����� ������� (�, ������, ������ ����������)
				// �������� ���� �� ������ �� ������ ��������� bs.
				//
				// ������� �� ������� (����������� ������ ������ � ����� �/�
				// � ���������� �����), �������� �� ���� �����. �����������,
				// ����� ������������ ����� ���������� ����� �� ����
				// ������� "���������" (���������� ����) ������ �/�
				pf = CoreAnalysisManager.getMostTypicalTrace(trColl);
			} else { // ���� ������� ��� ���
				mtm = CoreAnalysisManager.makeEtalon(trColl, ap);
				pf = CoreAnalysisManager.getMostTypicalTrace(trColl);
			}
			String name = LangModelAnalyse.getString("etalon");
			{
				// ���������� ��������� �������� ��� �������
				Result result1 = Heap.getPrimaryTrace().getResult();
				if (result1 != null) {
					Measurement m = AnalysisUtil.getMeasurementByResult(result1);
					name = AnalysisUtil.makeEtalonRefName(
							AnalysisUtil.getMEbyMS(m.getSetup()),
							null);
				}
			}
			Heap.setEtalonTraceFromBS(pf, name);
			Heap.setMTMEtalon(mtm);
		} catch (IncompatibleTracesException e){
			GUIUtil.showErrorMessage("incompatibleTraces");
		} catch (ApplicationException e) {
			GUIUtil.processApplicationException(e);
		}

		Environment.getActiveWindow().setCursor(
			Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
