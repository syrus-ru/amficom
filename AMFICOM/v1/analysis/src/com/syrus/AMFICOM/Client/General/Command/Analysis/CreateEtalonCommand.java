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
 * Команда создания эталона.
 * Раньше называлась MinuitAnalysisCommand.
 * Создает Heap.MTMEtalon
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
		// в такой ситуации она не должна была запускаться,
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

		// проводим анализ для всего набора р/г

		ModelTraceManager mtm;
		BellcoreStructure bs;
		try {
			if (Heap.hasEtalon()) { // если эталон уже есть
				// расширяем эталон (содержит ли bsColl эталонную р/г,
				// не должно влиять на результат, т.к. она уже должна быть
				// покрыта этим эталоном)
				mtm = Heap.getMTMEtalon();
				CoreAnalysisManager.updateEtalon(mtm, bsColl, ap);
				// Согласно текущему поведению Heap, эталон уже входит в bsColl,
				// поэтому мы
				// выбираем наиболее типичную с учетом существующего эталона.
				//
				// На самом деле это не очень точно, и корректнее было бы
				// учитывать эталон с весом, равным количеству рефлектограмм,
				// по которым он был создан, но которые в данный момент не
				// загружены.
				//
				// С другой стороны, самым простым (и, видимо, вполне приемлемым)
				// решением было бы вообще не менять эталонную bs.
				//
				// Текущее же решение (учитывающее старый эталон и новые р/г
				// с одинаковым весом), несмотря на свою матем. нестрогость,
				// имеет преимущество перед описанными двумя за счет
				// эффекта "забывания" (уменьшения веса) старых р/г
				bs = CoreAnalysisManager.getMostTypicalTrace(bsColl);
			} else { // если эталона еще нет
				mtm = CoreAnalysisManager.makeEtalon(bsColl, ap);
				bs = CoreAnalysisManager.getMostTypicalTrace(bsColl);
			}
			String name = LangModelAnalyse.getString("etalon");
			{
				// определяем временное название для эталона
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
