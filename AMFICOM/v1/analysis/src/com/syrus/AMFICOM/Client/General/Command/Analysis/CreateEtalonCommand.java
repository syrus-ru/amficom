package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.util.Collection;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;

/**
 *  оманда создани€ эталона.
 * –аньше называлась MinuitAnalysisCommand.
 * —оздает Heap.MTMEtalon
 */
public class CreateEtalonCommand extends VoidCommand
{
	public CreateEtalonCommand()
	{
		// empty
	}

	public Object clone()
	{
		return super.clone();
	}

	public void execute()
	{
		Collection bsColl = Heap.getBSCollection();

		// если не открыта ни одна р/г, просто игнорируем команду.
		// в такой ситуации она не должна была запускатьс€,
		// т.ч. обработка ошибки, в принципе, не нужна
		if (bsColl.isEmpty())
			return;

		Environment.getActiveWindow().setCursor(
			Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		AnalysisParameters ap = Heap.getMinuitAnalysisParams();
		if (ap == null) {
			new ClientAnalysisManager();
			ap = Heap.getMinuitAnalysisParams();
		}

		// проводим анализ дл€ всего набора р/г

		ModelTraceManager mtm;
		try {
			mtm = CoreAnalysisManager.makeEtalon(bsColl, ap);
		} catch (IncompatibleTracesException e){
			GUIUtil.showErrorMessage("incompatibleTraces");
			return;
		}

		Heap.setMTMEtalon(mtm);

		Environment.getActiveWindow().setCursor(
			Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
