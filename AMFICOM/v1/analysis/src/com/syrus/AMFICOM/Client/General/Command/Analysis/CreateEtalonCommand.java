package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.util.Collection;
import java.util.logging.Level;

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
import com.syrus.util.Log;

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

	@Override
	public void execute()
	{
		Collection<PFTrace> trColl = Heap.getPFTraceCollection();

		// если не открыта ни одна р/г, просто игнорируем команду.
		// ¬ такой ситуации команда не должна была вызыватьс€,
		// поэтому никака€ user-friendly обработка этой ошибки не нужна.
		if (trColl.isEmpty())
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
		PFTrace pf;
		try {
			if (Heap.hasEtalon()) { // если эталон уже есть
				// расшир€ем эталон (содержит ли trColl эталонную р/г,
				// не должно вли€ть на результат, т.к. она уже должна быть
				// покрыта этим эталоном)
				mtm = Heap.getMTMEtalon();
				CoreAnalysisManager.updateEtalon(mtm, trColl, ap);
				// —огласно текущему поведению Heap, эталон уже входит в bsColl,
				// поэтому мы
				// выбираем наиболее типичную с учетом существующего эталона.
				//
				// Ќа самом деле это не очень точно, и корректнее было бы
				// учитывать эталон с весом, равным количеству рефлектограмм,
				// по которым он был создан, но которые в данный момент не
				// загружены.
				//
				// — другой стороны, самым простым (и, видимо, вполне приемлемым)
				// решением было бы вообще не мен€ть эталонную bs.
				//
				// “екущее же решение (учитывающее старый эталон и новые р/г
				// с одинаковым весом), несмотр€ на свою матем. нестрогость,
				// имеет преимущество перед описанными двум€ за счет
				// эффекта "забывани€" (уменьшени€ веса) старых р/г
				pf = CoreAnalysisManager.getMostTypicalTrace(trColl);
			} else { // если эталона еще нет
				mtm = CoreAnalysisManager.makeEtalon(trColl, ap);
				Log.debugMessage("created MTM: " + mtm, Level.FINEST);
				pf = CoreAnalysisManager.getMostTypicalTrace(trColl);
			}
			String name = LangModelAnalyse.getString("etalon");
			{
				// определ€ем временное название дл€ эталона
				Measurement m = Heap.getPrimaryTrace().getMeasurement();
				if (m != null) {
					name = AnalysisUtil.makeEtalonRefName(
							AnalysisUtil.getMEbyMS(AnalysisUtil.getMSbyM(m)),
							null);
				}
			}
			Heap.setEtalonTraceFromPFTrace(pf, name);
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
