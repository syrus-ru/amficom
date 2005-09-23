/*
 * $Id: TestReport.java,v 1.3 2005/09/23 12:10:04 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.scheduler.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.IntervalToDate;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.MeasurementUnit;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.util.Log;

public class TestReport {
	protected static final int COLUMNS_COUNT = 2;
	private static final int PROPERTY_NAME_COLUMN_WIDTH = 200;
	private static final int PROPERTY_VALUE_COLUMN_WIDTH = 100;
	
	public static TableDataRenderingComponent createReport (
			Test test,
			TableDataStorableElement tableStorableElement) throws CreateReportException{
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		
		TableDataRenderingComponent renderingComponent = null;
		
		try {
			renderingComponent = new TableDataRenderingComponent(
				tableStorableElement,
				new TestReportTableModel(test,vertDivisionsCount),
				getTableColumnWidths(vertDivisionsCount));
		} catch (ApplicationException e) {
			Log.errorMessage("SchemeElementReport.createReport | " + e.getMessage());
			Log.errorException(e);			
			throw new CreateReportException(
					tableStorableElement.getReportName(),
					tableStorableElement.getModelClassName(),
					CreateReportException.ERROR_GETTING_FROM_POOL);
		}
		
		return renderingComponent;
	}

	private static List<Integer> getTableColumnWidths(int vertDivisionsCount) {
		List<Integer> tableColumnWidths = new ArrayList<Integer>();
		
		for (int j = 0; j < vertDivisionsCount; j++) {
			tableColumnWidths.add(PROPERTY_NAME_COLUMN_WIDTH);
			tableColumnWidths.add(PROPERTY_VALUE_COLUMN_WIDTH);
		}
		return tableColumnWidths;
	}
}

class TestReportTableModel extends AbstractTableModel {
	private static final String PARAMETER_NAME = "report.UI.propertyName";
	private static final String PARAMETER_VALUE = "report.UI.propertyValue";
	
	private static final String NAME = "report.Modules.SchemeEditor.Common.name";
	
	private static final String TEMPORAL_PARAMETERS = "Text.TimePanel.Title";
	private static final String TEMPORAL_TYPE_ONETIME = "Text.Test.TemporalType.Onetime";
	private static final String TEMPORAL_TYPE_CONTINUAL = "Text.Test.TemporalType.Continual";
	private static final String TEMPORAL_TYPE_PERIODICAL = "Text.Test.TemporalType.Periodical";

	private static final String START_TIME = "Text.TimePanel.Start";
	private static final String FINISH_TIME = "Text.TimePanel.Finish";
	private static final String INTERVAL = "Text.TimePanel.Interval";
	
	private static final String MEASUREMENT_PARAMETERS = "Text.MeasurementParameter.Title";	
	
	private static final String EMPTY_STRING = "";
	private static final String SEPARATOR = ", ";	
	
	private int vertDivisionsCount = 1;
	private int originalRowCount = 0;	
	private int rowCount = 1;
	private int columnCount = 1;	
	
	private List<String> propertyNamesColumn = new ArrayList<String>();
	private List<String> propertyValuesColumn = new ArrayList<String>();		
	
	protected TestReportTableModel (
			Test test,
			int vertDivisionsCount) throws ApplicationException {
		this.vertDivisionsCount = vertDivisionsCount;

		TestWrapper wrapper = TestWrapper.getInstance();
		
		this.propertyNamesColumn.add(LangModelReport.getString(NAME));
		this.propertyValuesColumn.add(test.getName());
		this.originalRowCount++;		
		
		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(LangModelSchedule.getString(TEMPORAL_PARAMETERS));
		String temporalTypeString = null;
		TestTemporalType temporalType = test.getTemporalType();
		switch (temporalType.value()) {
		case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
			temporalTypeString = LangModelSchedule.getString(TEMPORAL_TYPE_ONETIME);
			break;
		case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
			temporalTypeString = LangModelSchedule.getString(TEMPORAL_TYPE_PERIODICAL);
			break;
		case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
			temporalTypeString = LangModelSchedule.getString(TEMPORAL_TYPE_CONTINUAL);
			break;
		}
		this.propertyValuesColumn.add(temporalTypeString);

		this.propertyNamesColumn.add(LangModelSchedule.getString(START_TIME));
		Date startTime = (Date)wrapper.getValue(test,TestWrapper.COLUMN_START_TIME);
		this.propertyValuesColumn.add(startTime.toString());

		this.propertyNamesColumn.add(LangModelSchedule.getString(FINISH_TIME));
		Date endTime = (Date)wrapper.getValue(test,TestWrapper.COLUMN_END_TIME);
		this.propertyValuesColumn.add(endTime.toString());
		
		this.originalRowCount += 4;		

		if (temporalType.value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL) {
			
			PeriodicalTemporalPattern ptPattern =
				(PeriodicalTemporalPattern)StorableObjectPool.getStorableObject(test.getTemporalPatternId(),true);
			long period = ptPattern.getPeriod();
			this.propertyNamesColumn.add(LangModelSchedule.getString(INTERVAL));
			this.propertyValuesColumn.add(IntervalToDate.toDate(period));
			this.originalRowCount++;
		}

		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(LangModelSchedule.getString(MEASUREMENT_PARAMETERS));
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.originalRowCount += 2;
		
		MeasurementSetup measurementSetup = StorableObjectPool.getStorableObject(
				test.getMainMeasurementSetupId(),
				true);
		ParameterSet parameterSet = measurementSetup.getParameterSet();
		for (Parameter parameter : parameterSet.getParameters()) {
			ParameterType parameterType = parameter.getType();
			MeasurementUnit measurementUnit = parameterType.getMeasurementUnit();

			String valueName = parameterType.getDescription();
			if (measurementUnit != MeasurementUnit.NONDIMENSIONAL)
				valueName += (SEPARATOR + measurementUnit.getName());
			
			this.propertyNamesColumn.add(valueName);
			this.propertyValuesColumn.add(parameter.getStringValue());
			this.originalRowCount++;
		}
		
		//��������� ����� ����� � �������� ��� �������
		this.rowCount = this.originalRowCount / this.vertDivisionsCount;
		if (this.originalRowCount % this.vertDivisionsCount > 0)		
			this.rowCount++;
		
		this.columnCount = TestReport.COLUMNS_COUNT * this.vertDivisionsCount;
	}
	
	public int getRowCount() {
		return this.rowCount;
	}

	public int getColumnCount() {
		return this.columnCount;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex % TestReport.COLUMNS_COUNT) {
		case 0:
			return LangModelReport.getString(PARAMETER_NAME);
		case 1:
			return LangModelReport.getString(PARAMETER_VALUE);
			
		}
		throw new AssertionError("TestReportTableModel.getColumnName | Unreachable code");
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		int index = this.getRowCount() * (columnIndex / TestReport.COLUMNS_COUNT) + rowIndex;
		if (index >= this.originalRowCount)
			return EMPTY_STRING;

		switch (columnIndex % TestReport.COLUMNS_COUNT) {
			case 0:
				return this.propertyNamesColumn.get(index);
			case 1:
				return this.propertyValuesColumn.get(index);
		}

		throw new AssertionError("TestReportTableModel.getValueAt | Unreachable code");
	}
}
