package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.util.Collection;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.PermissionManager;
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
		// Получаем набор результатов, которые надо загрузить
		// XXX: performance: загружаем - иногда долго
		JFrame parent = Environment.getActiveWindow();
		if(TraceLoadDialog.showDialog(parent) == JOptionPane.CANCEL_OPTION) {
			return;
		}
		Set<Result> results = TraceLoadDialog.getResults();

		// XXX: Если набор результатов пуст, ничего не делаем
		if (results.isEmpty())
			return;

		// преобразуем выбранный набор результатов в набор рефлектограмм
		// XXX: performance: 40-80% time, because of waiting for empty set of analysis for each measurement
		Collection<Trace> traces;
		try {
			traces = AddTraceFromDatabaseCommand.getTracesFromResults(results);
		} catch (DataFormatException e) {
			// ошибка формата данных - отменяем загрузку
			GUIUtil.showDataFormatError();
			return;
		} catch (ApplicationException e) {
			GUIUtil.processApplicationException(e);
			return;
		}
		// открываем выбранный набор рефлектограмм
		// XXX: performance: 20-50% time?
		Heap.openManyTraces(traces);

		// если результат выбранного primaryTrace получен в результате измерения,
		// то устанавливаем ms и, если есть, эталон
		// согласно этому измерению
		// XXX: так, как сделано сейчас, позволяет пользователю загрузить
		// одновременно рефлектограммы с разными MS или даже путями тестирования
		// (при этом одна из них будет выбрана первичной, и в качестве MS
		// будет выбран ее MS), затем переназначить первичной р/грамму
		// с другого пути (при этом MS останется от старой первичной),
		// а затем сохранить шаблон. При этом будет создан шаблон с
		// MS старой первичной р/г, но на основе новой первичной р/г.
		// Видимо, это надо исправить, например, одним из таких способов:
		// 1. Проверять все загружаемые р/г (как в OPEN, так и в ADD).
		//    Если они от разных MS,
		//    то ставить MS:=null (с предупреждением либо без него);
		// 2. При смене первичной р/г обнулять MS (самое простое и надежное?);
		// 3. Использовать MS первичной р/г, а не сохраненный отдельно MS
		//    первой загруженной;
		// 4. При создании (или сохранении) шаблона предупреждать пользователя,
		//    что есть несколько MS, и предупреждать,
		//    что будет взят MS первичной.
		Result primaryTraceResult = Heap.getPrimaryTrace().getResult();
		if (primaryTraceResult != null
				&& AnalysisUtil.hasMeasurementByResult(primaryTraceResult)) {
			Measurement m = AnalysisUtil.getMeasurementByResult(primaryTraceResult);
			MeasurementSetup ms = m.getSetup();
			Heap.setContextMeasurementSetup(ms);

			try {
				// пытаемся загрузить параметры анализа
				AnalysisUtil.loadCriteriaSet(LoginManager.getUserId(), ms);
				// первичная р/г анализируется безусловно - чтобы получить RefAnalysis, который не сохраняется на БД
				// считаем, что результат будет тем же самым, т.к. параметры анализа загружены те же самые
				// XXX: вообще, неплохо бы сохранять и RefAnalysis или какую-то его замену - тогда не надо будет проводить повторный анализ.
				Heap.makePrimaryAnalysis();
				// пытаемся загрузить эталон
				if (ms.getEtalon() != null &&
						PermissionManager.isPermitted(PermissionManager.Operation.LOAD_ETALON)) {
					AnalysisUtil.loadEtalon(ms);
				} else {
					Heap.unSetEtalonPair();
				}
				// загружаем результаты сравнения с эталоном (если есть)
				// NB: при загрузке всех р/г уже были загружены
				// результаты анализа. Теперь мы повторно загружаем
				// результаты анализа + результаты оценки,
				// но уже только для одной р/г - той, что выбрана как первичная.
				AnalysisUtil.loadEtalonComparison(m);
			} catch (DataFormatException e) {
				GUIUtil.showDataFormatError();
			} catch (ApplicationException e) {
				GUIUtil.processApplicationException(e);
			}
		}
		Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
