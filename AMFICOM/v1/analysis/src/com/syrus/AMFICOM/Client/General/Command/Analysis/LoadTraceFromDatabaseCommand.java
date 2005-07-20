package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.UI.TraceLoadDialog;
import com.syrus.AMFICOM.analysis.dadara.DataFormatException;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodename;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

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
		JFrame parent = Environment.getActiveWindow();
		if(TraceLoadDialog.showDialog(parent) == JOptionPane.CANCEL_OPTION)
			return;
		Set<Result> results = TraceLoadDialog.getResults();

		// XXX: Если набор результатов пуст, ничего не делаем
		if (results.isEmpty())
			return;

		// Выбираем первую рефлектограмму
		// @todo загружать все рефлектограммы: самую типичную - как первичную, остальные - как вторичные

		Result result1 = results.iterator().next();
		BellcoreStructure bs = null;

		// достаем собственно рефлектограмму из параметры результатов
		{
			Parameter[] parameters = result1.getParameters();
			for (int i = 0; i < parameters.length; i++) {
				Parameter param = parameters[i];
				ParameterType type = (ParameterType)param.getType();
				if (type.getCodename().equals(ParameterTypeCodename.REFLECTOGRAMMA.stringValue()))
					bs = new BellcoreReader().getData(param.getValue());
			}
		}

		// ошибка - в результатах анализа нет рефлектограммы
		if (bs == null)
			return; // FIXME: exceptions/error handling: выдавать собщение об ошибке

		// закрываем все открытые рефлектограммы
		if (Heap.getBSPrimaryTrace() != null)
			new FileCloseCommand(this.aContext).execute();

		// открываем загруженную рефлектограмму как первичную
		Heap.setBSPrimaryTrace(bs);

		// если загружаемый результат получен в результате измерения,
		// то устанавливаем заголовок, meId, measId, ms и, если есть, эталон,
		// согласно этому измерению
		if (result1.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
			Measurement m = (Measurement)result1.getAction();
			bs.title = m.getName();
			bs.monitoredElementId = m.getMonitoredElementId().getIdentifierString();
			bs.measurementId = m.getId().getIdentifierString();
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
