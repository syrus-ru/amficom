/*
 * $Id: SchemeElementReport.java,v 1.3 2005/10/30 14:49:20 bass Exp $
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
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

public class SchemeElementReport {
	protected static final int COLUMNS_COUNT = 2;
	private static final int PROPERTY_NAME_COLUMN_WIDTH = 200;
	private static final int PROPERTY_VALUE_COLUMN_WIDTH = 150;
	
	public static TableDataRenderingComponent createReport (
			SchemeElement schemeElement,
			TableDataStorableElement tableStorableElement) throws CreateReportException{
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		
		TableDataRenderingComponent renderingComponent = null;
		
		try {
			renderingComponent = new TableDataRenderingComponent(
				tableStorableElement,
				new SchemeElementTableModel(schemeElement,vertDivisionsCount),
				getTableColumnWidths(vertDivisionsCount));
		} catch (ApplicationException e) {
			Log.errorMessage(e.getMessage());
			Log.errorMessage(e);			
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

class SchemeElementTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3923910067250887384L;
	private static final String PARAMETER_NAME = "report.UI.propertyName";
	private static final String PARAMETER_VALUE = "report.UI.propertyValue";
	
	private static final String NAME = "report.Modules.SchemeEditor.Common.name";		
	private static final String TYPE = "report.Modules.SchemeEditor.Common.type";
	private static final String DESCRIPTION = "report.Modules.SchemeEditor.Common.description";
	private static final String OBJECT_CHARS = "report.Modules.SchemeEditor.Common.objectChars";
	private static final String TYPE_CHARS = "report.Modules.SchemeEditor.Common.typeChars";
	private static final String EQUIPMENT_CHARS = "report.Modules.SchemeEditor.Common.equipmentChars";	
	
	private static final String EMPTY_STRING = "";
	
	private int vertDivisionsCount = 1;
	private int originalRowCount = 0;	
	private int rowCount = 1;
	private int columnCount = 1;	
	
	private List<String> propertyNamesColumn = new ArrayList<String>();
	private List<String> propertyValuesColumn = new ArrayList<String>();		
	
	protected SchemeElementTableModel (
			SchemeElement schemeElement,
			int vertDivisionsCount) throws ApplicationException {
		this.vertDivisionsCount = vertDivisionsCount;
		
		ProtoEquipment protoEquipment = schemeElement.getProtoEquipment();
		this.propertyNamesColumn.add(I18N.getString(NAME));
		this.propertyValuesColumn.add(schemeElement.getName());
		this.propertyNamesColumn.add(I18N.getString(TYPE));
		this.propertyValuesColumn.add(protoEquipment.getName());
		this.propertyNamesColumn.add(I18N.getString(DESCRIPTION));
		this.propertyValuesColumn.add(schemeElement.getDescription());

		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(I18N.getString(OBJECT_CHARS));
		this.propertyValuesColumn.add(EMPTY_STRING);
		
		this.originalRowCount += 5;
		
		Set<Characteristic> objectChars = schemeElement.getCharacteristics(true);
		
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

		Set<Characteristic> typeChars = protoEquipment.getCharacteristics(true);
		
		for (Characteristic characteristic : typeChars) {
			this.propertyNamesColumn.add(characteristic.getName());
			this.propertyValuesColumn.add(characteristic.getValue());
			this.originalRowCount++;			
		}

		Equipment equipment = schemeElement.getEquipment();
		if (equipment != null) {
			this.propertyNamesColumn.add(EMPTY_STRING);
			this.propertyValuesColumn.add(EMPTY_STRING);
			this.propertyNamesColumn.add(I18N.getString(EQUIPMENT_CHARS));
			this.propertyValuesColumn.add(EMPTY_STRING);

			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.INVNUMBER));
			this.propertyValuesColumn.add(equipment.getInventoryNumber());
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER));
			this.propertyValuesColumn.add(protoEquipment.getManufacturer());
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER_CODE));
			this.propertyValuesColumn.add(protoEquipment.getManufacturerCode());
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER));
			this.propertyValuesColumn.add(equipment.getSupplier());
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER_CODE));
			this.propertyValuesColumn.add(equipment.getSupplierCode());
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.HARDWARE));
			this.propertyValuesColumn.add(EMPTY_STRING);
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SERNUM));
			this.propertyValuesColumn.add(equipment.getHwSerial());
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.VERSION));
			this.propertyValuesColumn.add(equipment.getHwVersion());
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SOFTWARE));
			this.propertyValuesColumn.add(EMPTY_STRING);
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SERNUM));
			this.propertyValuesColumn.add(equipment.getSwSerial());
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.VERSION));
			this.propertyValuesColumn.add(equipment.getSwVersion());
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.LONGITUDE));
			this.propertyValuesColumn.add(Float.toString(equipment.getLongitude()));
			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.LATITUDE));
			this.propertyValuesColumn.add(Float.toString(equipment.getLatitude()));
			this.originalRowCount += 15;
			
			KIS kis = schemeElement.getKis();
			if (kis != null) {
				this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.KIS));
				this.propertyValuesColumn.add(kis.getName());
				this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.ADDRESS));
				this.propertyValuesColumn.add(kis.getHostName());
				this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.PORT));
				this.propertyValuesColumn.add(Integer.toString(kis.getTCPPort()));
				this.originalRowCount += 3;
			}
			
			Set<Characteristic> equipmentChars = equipment.getCharacteristics(true);
			for (Characteristic characteristic : equipmentChars) {
				this.propertyNamesColumn.add(characteristic.getName());
				this.propertyValuesColumn.add(characteristic.getValue());
				this.originalRowCount++;			
			}
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
		switch (columnIndex % SchemeElementReport.COLUMNS_COUNT) {
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

		throw new AssertionError("SchemeElementTableModel.getValueAt | Unreachable code");
	}
}
