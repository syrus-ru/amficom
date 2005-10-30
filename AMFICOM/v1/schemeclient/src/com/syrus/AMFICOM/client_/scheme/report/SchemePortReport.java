/*
 * $Id: SchemePortReport.java,v 1.4 2005/10/30 15:20:54 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client_.scheme.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.util.Log;

public class SchemePortReport {
	protected static final int COLUMNS_COUNT = 2;
	private static final int PROPERTY_NAME_COLUMN_WIDTH = 200;
	private static final int PROPERTY_VALUE_COLUMN_WIDTH = 150;
	
	public static TableDataRenderingComponent createReport (
			AbstractSchemePort abstractPort,
			TableDataStorableElement tableStorableElement) throws CreateReportException{
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		
		TableDataRenderingComponent renderingComponent = null;
		
		try {
			renderingComponent = new TableDataRenderingComponent(
				tableStorableElement,
				new AbstractPortTableModel(abstractPort,vertDivisionsCount),
				getTableColumnWidths(vertDivisionsCount));
		} catch (ApplicationException e) {
			assert Log.errorMessage(e.getMessage());
			assert Log.errorMessage(e);			
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
			tableColumnWidths.add(new Integer(PROPERTY_NAME_COLUMN_WIDTH));
			tableColumnWidths.add(new Integer(PROPERTY_VALUE_COLUMN_WIDTH));
		}
		return tableColumnWidths;
	}
}

class AbstractPortTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8910382472510534505L;
	private static final String PARAMETER_NAME = "report.UI.propertyName";
	private static final String PARAMETER_VALUE = "report.UI.propertyValue";
	
	private static final String NAME = "report.Modules.SchemeEditor.Common.name";		
	private static final String TYPE = "report.Modules.SchemeEditor.Common.type";
	private static final String DESCRIPTION = "report.Modules.SchemeEditor.Common.description";
	private static final String OBJECT_CHARS = "report.Modules.SchemeEditor.Common.objectChars";
	private static final String TYPE_CHARS = "report.Modules.SchemeEditor.Common.typeChars";
	
	private static final String EMPTY_STRING = "";
	
	private int vertDivisionsCount = 1;
	private int originalRowCount = 0;	
	private int rowCount = 1;
	private int columnCount = 1;	
	
	private List<String> propertyNamesColumn = new ArrayList<String>();
	private List<String> propertyValuesColumn = new ArrayList<String>();		
	
	protected AbstractPortTableModel (
			AbstractSchemePort abstractPort,
			int vertDivisionsCount) throws ApplicationException {
		this.vertDivisionsCount = vertDivisionsCount;
		
		PortType portType = abstractPort.getPortType();
		this.propertyNamesColumn.add(I18N.getString(NAME));
		this.propertyValuesColumn.add(abstractPort.getName());
		this.propertyNamesColumn.add(I18N.getString(TYPE));
		this.propertyValuesColumn.add(portType.getName());
		this.propertyNamesColumn.add(I18N.getString(DESCRIPTION));
		this.propertyValuesColumn.add(abstractPort.getDescription());

		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(I18N.getString(OBJECT_CHARS));
		this.propertyValuesColumn.add(EMPTY_STRING);
		
		this.originalRowCount += 5;
		
		Set<Characteristic> objectChars = abstractPort.getCharacteristics(true);
		
		for (Characteristic characteristic : objectChars) {
			this.propertyNamesColumn.add(characteristic.getName());
			this.propertyValuesColumn.add(characteristic.getValue());
			this.originalRowCount++;
		}

		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(I18N.getString(TYPE_CHARS));
		this.propertyValuesColumn.add(EMPTY_STRING);
		
		this.originalRowCount += 2;

		Set<Characteristic> typeChars = portType.getCharacteristics(true);
		
		for (Characteristic characteristic : typeChars) {
			this.propertyNamesColumn.add(characteristic.getName());
			this.propertyValuesColumn.add(characteristic.getValue());
			this.originalRowCount++;			
		}
		
		//Вычисляем число строк и столбцов для таблицы
		this.rowCount = this.originalRowCount / this.vertDivisionsCount;
		if (this.originalRowCount % this.vertDivisionsCount > 0)		
			this.rowCount++;
		
		this.columnCount = SchemeElementReport.COLUMNS_COUNT * this.vertDivisionsCount;
	}
	
	public int getRowCount() {
		return this.rowCount;
	}

	public int getColumnCount() {
		return this.columnCount;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex % SchemePortReport.COLUMNS_COUNT) {
		case 0:
			return I18N.getString(PARAMETER_NAME);
		case 1:
			return I18N.getString(PARAMETER_VALUE);
			
		}
		throw new AssertionError("TestReportTableModel.getColumnName | Unreachable code");
    }	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		int index = this.getRowCount() * (columnIndex / SchemeElementReport.COLUMNS_COUNT) + rowIndex;
		if (index >= this.originalRowCount)
			return EMPTY_STRING;

		switch (columnIndex % SchemeElementReport.COLUMNS_COUNT) {
			case 0:
				return this.propertyNamesColumn.get(index);
			case 1:
				return this.propertyValuesColumn.get(index);
		}

		throw new AssertionError("AbstractPortTableModel.getValueAt | Unreachable code");
	}
}
