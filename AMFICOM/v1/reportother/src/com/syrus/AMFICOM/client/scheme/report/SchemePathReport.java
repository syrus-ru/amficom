/*
 * $Id: SchemePathReport.java,v 1.2 2005/09/16 13:26:27 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.scheme.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.AbstractSchemeLink;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class SchemePathReport {
	protected static final int COLUMNS_COUNT = 2;
	private static final int PROPERTY_NAME_COLUMN_WIDTH = 200;
	private static final int PROPERTY_VALUE_COLUMN_WIDTH = 150;
	
	public static TableDataRenderingComponent createReport (
			SchemePath schemePath,
			TableDataStorableElement tableStorableElement) throws CreateReportException{
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		
		TableDataRenderingComponent renderingComponent = null;
		
		try {
			renderingComponent = new TableDataRenderingComponent(
				tableStorableElement,
				new SchemePathTableModel(schemePath,vertDivisionsCount),
				createTableColumnModel(vertDivisionsCount));
		} catch (ApplicationException e) {
			Log.errorMessage("SchemePathReport.createReport | " + e.getMessage());
			Log.errorException(e);			
			
			throw new CreateReportException(
					tableStorableElement.getReportName(),
					tableStorableElement.getModelClassName(),
					CreateReportException.ERROR_GETTING_FROM_POOL);
		}
		
		return renderingComponent;
	}

	private static TableColumnModel createTableColumnModel(int vertDivisionsCount) {
		TableColumnModel tableColumnModel = new DefaultTableColumnModel();
		
		for (int j = 0; j < vertDivisionsCount; j++) {
			tableColumnModel.addColumn(new TableColumn(
					j * COLUMNS_COUNT,
					PROPERTY_NAME_COLUMN_WIDTH));
			tableColumnModel.addColumn(new TableColumn(
					j * COLUMNS_COUNT + 1,
					PROPERTY_VALUE_COLUMN_WIDTH));
		}
		return tableColumnModel;
	}
}

class SchemePathTableModel extends AbstractTableModel {
	private static final String NAME = "report.Modules.SchemeEditor.Common.name";		
	private static final String TYPE = "report.Modules.SchemeEditor.Common.type";
	private static final String DESCRIPTION = "report.Modules.SchemeEditor.Common.description";
	private static final String OBJECT_CHARS = "report.Modules.SchemeEditor.Common.objectChars";
	
	private static final String TEMP_PATH_ELEMENTS_LIST = "report.Modules.SchemeEditor.Common.pathElementsList";	
	
	private static final String EMPTY_STRING = "";
	
	private int vertDivisionsCount = 1;
	private int originalRowCount = 0;	
	private int rowCount = 1;
	private int columnCount = 1;	
	
	private List<String> propertyNamesColumn = new ArrayList<String>();
	private List<String> propertyValuesColumn = new ArrayList<String>();		
	
	protected SchemePathTableModel (
			SchemePath schemePath,
			int vertDivisionsCount) throws ApplicationException {
		this.vertDivisionsCount = vertDivisionsCount;
		
		this.propertyNamesColumn.add(LangModelReport.getString(NAME));
		this.propertyValuesColumn.add(schemePath.getName());
		this.propertyNamesColumn.add(LangModelReport.getString(TYPE));
		this.propertyValuesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SCHEME_PATH));
		this.propertyNamesColumn.add(LangModelReport.getString(DESCRIPTION));
		this.propertyValuesColumn.add(schemePath.getDescription());

		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(LangModelReport.getString(OBJECT_CHARS));
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.originalRowCount += 5;

		Set<Characteristic> objectChars = schemePath.getCharacteristics(true);
		for (Characteristic characteristic : objectChars) {
			this.propertyNamesColumn.add(characteristic.getName());
			this.propertyValuesColumn.add(characteristic.getValue());
			this.originalRowCount++;
		}

		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.START_ELEMENT));
		this.propertyValuesColumn.add(schemePath.getStartSchemeElement().getName());
		this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.END_ELEMENT));
		this.propertyValuesColumn.add(schemePath.getEndSchemeElement().getName());
		this.originalRowCount += 2;
		
		this.propertyNamesColumn.add(LangModelReport.getString(TEMP_PATH_ELEMENTS_LIST));
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.originalRowCount++;
		final SortedSet<PathElement> pathElements = schemePath.getPathMembers();
		for (final PathElement pe : pathElements) {
			this.propertyNamesColumn.add(EMPTY_STRING);
			this.propertyValuesColumn.add(pe.getName());
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

	public Object getValueAt(int rowIndex, int columnIndex) {
		int index = this.getRowCount() * (columnIndex / SchemeElementReport.COLUMNS_COUNT) + rowIndex;
		if (index >= this.originalRowCount)
			throw new AssertionError("SchemeLinkTableModel.getValueAt | Index exceeds data size");

		switch (columnIndex % SchemeElementReport.COLUMNS_COUNT) {
			case 0:
				return this.propertyNamesColumn.get(index);
			case 1:
				return this.propertyValuesColumn.get(index);
		}

		throw new AssertionError("SchemeLinkTableModel.getValueAt | Unreachable code");
	}
}
