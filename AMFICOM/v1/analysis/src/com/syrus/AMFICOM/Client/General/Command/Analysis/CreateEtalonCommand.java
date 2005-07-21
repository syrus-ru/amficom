package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.util.Collection;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.io.BellcoreStructure;

/**
 *  оманда создани€ эталона.
 * –аньше называлась MinuitAnalysisCommand.
 * —оздает Heap.MTMEtalon
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
		BellcoreStructure bs;
		try {
			mtm = CoreAnalysisManager.makeEtalon(bsColl, ap);
			bs = CoreAnalysisManager.getMostTypicalTrace(bsColl);
			Heap.setEtalonTraceFromBS(bs);
			Heap.setMTMEtalon(mtm);
		} catch (IncompatibleTracesException e){
			GUIUtil.showErrorMessage("incompatibleTraces");
		}

		Environment.getActiveWindow().setCursor(
			Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
