package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.util.Collection;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.BellcoreStructure;

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
		Collection bsColl = Heap.getBSCollection();

		// ���� �� ������� �� ���� �/�, ������ ���������� �������.
		// � ����� �������� ������� �� ������ ���� ����������,
		// ������� ������� user-friendly ��������� ���� ������ �� �����.
		if (bsColl.isEmpty())
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
		BellcoreStructure bs;
		try {
			if (Heap.hasEtalon()) { // ���� ������ ��� ����
				// ��������� ������ (�������� �� bsColl ��������� �/�,
				// �� ������ ������ �� ���������, �.�. ��� ��� ������ ����
				// ������� ���� ��������)
				mtm = Heap.getMTMEtalon();
				CoreAnalysisManager.updateEtalon(mtm, bsColl, ap);
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
				bs = CoreAnalysisManager.getMostTypicalTrace(bsColl);
			} else { // ���� ������� ��� ���
				mtm = CoreAnalysisManager.makeEtalon(bsColl, ap);
				bs = CoreAnalysisManager.getMostTypicalTrace(bsColl);
			}
			String name = LangModelAnalyse.getString("etalon");
			{
				// ���������� ��������� �������� ��� �������
				Result result = Heap.getPrimaryTrace().getResult();
				if (result != null) {
					Measurement m = AnalysisUtil.getMeasurementByResult(result);
					name = AnalysisUtil.makeEtalonRefName(
							AnalysisUtil.getMEbyMS(m.getSetup()),
							null);
				}
			}
			Heap.setEtalonTraceFromBS(bs, name);
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
