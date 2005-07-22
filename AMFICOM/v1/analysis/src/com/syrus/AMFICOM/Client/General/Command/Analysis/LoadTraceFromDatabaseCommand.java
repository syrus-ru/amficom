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
import com.syrus.AMFICOM.analysis.dadara.DataFormatException;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;

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
		// ѕолучаем набор результатов, которые надо загрузить
		JFrame parent = Environment.getActiveWindow();
		if(TraceLoadDialog.showDialog(parent) == JOptionPane.CANCEL_OPTION)
			return;
		Set<Result> results = TraceLoadDialog.getResults();

		// XXX: ≈сли набор результатов пуст, ничего не делаем
		if (results.isEmpty())
			return;

		// преобразуем выбранный набор результатов в набор рефлектограмм
		Collection<Trace> traces =
				AddTraceFromDatabaseCommand.getTracesFromResults(results);
		// открываем выбранный набор рефлектограмм
		Heap.openManyTraces(traces);

		// если результат выбранного primaryTrace получен в результате измерени€,
		// то устанавливаем ms и, если есть, эталон
		// согласно этому измерению
		// XXX: так, как сделано сейчас, позвол€ет пользователю загрузить
		// одновременно рефлектограммы с разными MS или даже пут€ми тестировани€
		// (при этом одна из них будет выбрана первичной, и в качестве MS
		// будет выбран ее MS), затем переназначить первичной р/грамму
		// с другого пути (при этом MS останетс€ от старой первичной),
		// а затем сохранить шаблон. ѕри этом будет создан шаблон с
		// MS старой первичной р/г, но на основе новой первичной р/г.
		// ¬идимо, это надо исправить, например, одним из таких способов:
		// 1. ѕровер€ть все загружаемые р/г (как в OPEN, так и в ADD).
		//    ≈сли они от разных MS,
		//    то ставить MS:=null (с предупреждением либо без него);
		// 2. ѕри смене первичной р/г обнул€ть MS (самое простое и надежное?);
		// 3. »спользовать MS первичной р/г, а не сохраненный отдельно MS
		//    первой загруженной;
		// 4. ѕри создании (или сохранении) шаблона предупреждать пользовател€,
		//    что есть несколько MS, и предупреждать,
		//    что будет вз€т MS первичной.
		Result primaryTraceResult = Heap.getPrimaryTrace().getResult();
		if (primaryTraceResult != null
				&& primaryTraceResult.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
			Measurement m = (Measurement)primaryTraceResult.getAction();
			MeasurementSetup ms = m.getSetup();
			Heap.setContextMeasurementSetup(ms);

			try {
				AnalysisUtil.load_CriteriaSet(LoginManager.getUserId(), ms);
				if (ms.getEtalon() != null)
					AnalysisUtil.load_Etalon(ms);
				else
					Heap.unSetEtalonPair();
				Heap.makePrimaryAnalysis();
			} catch (DataFormatException e) {
				GUIUtil.showDataFormatError();
				return;
			}
		}
		Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
