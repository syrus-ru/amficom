package com.syrus.AMFICOM.client.map.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.scheme.report.SchemeElementReport;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeElement;

public class TunnelCableListReport {
	protected static final int COLUMNS_COUNT = 2;
	private static final int PROPERTY_NAME_COLUMN_WIDTH = 200;
	private static final int PROPERTY_VALUE_COLUMN_WIDTH = 150;
	
	public static TableDataRenderingComponent createReport(
			TableDataStorableElement tableStorableElement,
			PhysicalLink physicalLink)throws CreateReportException {
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		TableDataRenderingComponent renderingComponent = null;
		
		try {
			renderingComponent = new TableDataRenderingComponent(
				tableStorableElement,
				new TunnelCableListTableModel(physicalLink,vertDivisionsCount),
				createTableColumnModel(vertDivisionsCount));
		} catch (ApplicationException e) {
			throw new CreateReportException(
					tableStorableElement.getReportName(),
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

class TunnelCableListTableModel extends AbstractTableModel {
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
	
	protected TunnelCableListTableModel (
			PhysicalLink physicalLink,
			int vertDivisionsCount) throws ApplicationException {
		this.vertDivisionsCount = vertDivisionsCount;
//
//		Collector pipePath = physicalLink.getTgetCollector(physicalLink);
//		if(pipePath != null)
//			fullLinkName += LangModelMap.getString("Collector")
//					+ pipePath.getName();
//		else
//			fullLinkName += LangModelMap.getString("Tunnel")
//					+ physicalLink.getName();
//		
//		this.propertyNamesColumn.add(LangModelReport.getString(NAME));
//		this.propertyValuesColumn.add(pipePath.getName());
//		this.propertyNamesColumn.add(LangModelReport.getString(TYPE));
//		this.propertyValuesColumn.add(eqType.getName());
//		this.propertyNamesColumn.add(LangModelReport.getString(DESCRIPTION));
//		this.propertyValuesColumn.add(schemeElement.getDescription());
//
//		this.propertyNamesColumn.add(EMPTY_STRING);
//		this.propertyValuesColumn.add(EMPTY_STRING);
//		this.propertyNamesColumn.add(LangModelReport.getString(OBJECT_CHARS));
//		this.propertyValuesColumn.add(EMPTY_STRING);
//		
//		this.originalRowCount += 5;
//		
//		Set<Characteristic> objectChars = schemeElement.getCharacteristics(true);
//		
//		for (Characteristic characteristic : objectChars) {
//			this.propertyNamesColumn.add(characteristic.getName());
//			this.propertyValuesColumn.add(characteristic.getValue());
//			this.originalRowCount++;
//		}
//
//		this.propertyNamesColumn.add(EMPTY_STRING);
//		this.propertyValuesColumn.add(EMPTY_STRING);
//		this.propertyNamesColumn.add(LangModelReport.getString(TYPE_CHARS));
//		this.propertyValuesColumn.add(EMPTY_STRING);
//		
//		this.originalRowCount += 2;
//
//		Set<Characteristic> typeChars = eqType.getCharacteristics(true);
//		
//		for (Characteristic characteristic : typeChars) {
//			this.propertyNamesColumn.add(characteristic.getName());
//			this.propertyValuesColumn.add(characteristic.getValue());
//			this.originalRowCount++;			
//		}
//
//		Equipment equipment = schemeElement.getEquipment();
//		if (equipment != null) {
//			this.propertyNamesColumn.add(EMPTY_STRING);
//			this.propertyValuesColumn.add(EMPTY_STRING);
//			this.propertyNamesColumn.add(LangModelReport.getString(EQUIPMENT_CHARS));
//			this.propertyValuesColumn.add(EMPTY_STRING);
//
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.INVNUMBER));
//			this.propertyValuesColumn.add(equipment.getInventoryNumber());
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER));
//			this.propertyValuesColumn.add(eqType.getManufacturer());
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.MANUFACTURER_CODE));
//			this.propertyValuesColumn.add(eqType.getManufacturerCode());
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER));
//			this.propertyValuesColumn.add(equipment.getSupplier());
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SUPPLIER_CODE));
//			this.propertyValuesColumn.add(equipment.getSupplierCode());
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.HARDWARE));
//			this.propertyValuesColumn.add(EMPTY_STRING);
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SERNUM));
//			this.propertyValuesColumn.add(equipment.getHwSerial());
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.VERSION));
//			this.propertyValuesColumn.add(equipment.getHwVersion());
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SOFTWARE));
//			this.propertyValuesColumn.add(EMPTY_STRING);
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.SERNUM));
//			this.propertyValuesColumn.add(equipment.getSwSerial());
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.VERSION));
//			this.propertyValuesColumn.add(equipment.getSwVersion());
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.LONGITUDE));
//			this.propertyValuesColumn.add(Float.toString(equipment.getLongitude()));
//			this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.LATITUDE));
//			this.propertyValuesColumn.add(Float.toString(equipment.getLatitude()));
//			this.originalRowCount += 15;
//			
//			KIS kis = schemeElement.getKis();
//			if (kis != null) {
//				this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.KIS));
//				this.propertyValuesColumn.add(kis.getName());
//				this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.ADDRESS));
//				this.propertyValuesColumn.add(kis.getHostName());
//				this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.PORT));
//				this.propertyValuesColumn.add(Integer.toString(kis.getTCPPort()));
//				this.originalRowCount += 3;
//			}
//			
//			Set<Characteristic> equipmentChars = equipment.getCharacteristics(true);
//			for (Characteristic characteristic : equipmentChars) {
//				this.propertyNamesColumn.add(characteristic.getName());
//				this.propertyValuesColumn.add(characteristic.getValue());
//				this.originalRowCount++;			
//			}
//		}
		
		//Вычисляем число строк и столбцов для таблицы
		this.rowCount = this.originalRowCount / this.vertDivisionsCount;
		if (this.originalRowCount % this.vertDivisionsCount > 0)		
			this.rowCount++;
		
		this.columnCount = TunnelCableListReport.COLUMNS_COUNT * this.vertDivisionsCount;
	}
	
	public int getRowCount() {
		return this.rowCount;
	}

	public int getColumnCount() {
		return this.columnCount;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		int index = this.getRowCount() * (columnIndex / TunnelCableListReport.COLUMNS_COUNT) + rowIndex;
		if (index >= this.originalRowCount)
			throw new AssertionError("SchemeElementTableModel.getValueAt | Index exceeds data size");

		switch (columnIndex % TunnelCableListReport.COLUMNS_COUNT) {
			case 0:
				return this.propertyNamesColumn.get(index);
			case 1:
				return this.propertyValuesColumn.get(index);
		}

		throw new AssertionError("SchemeElementTableModel.getValueAt | Unreachable code");
	}
}


//class TunnelCableListReportTableModel extends DividableTableModel
//{
//		//TODO retreive Map from somewhere
//		Map map = null;
////		Map map = physicalLink.getMap(); // Возможно лажа!!
//
//		String fullLinkName = "";
//		Collector pipePath = map.getCollector(physicalLink);
//		if(pipePath != null)
//			fullLinkName += LangModelMap.getString("Collector")
//					+ pipePath.getName();
//		else
//			fullLinkName += LangModelMap.getString("Tunnel")
//					+ physicalLink.getName();
//
//		this.tableData[0][tdIterator] = fullLinkName;
//		this.tableData[1][tdIterator++] = "";
//
//		this.tableData[0][tdIterator] = LangModelMap.getString("Start_node_id");
//		this.tableData[1][tdIterator++] = physicalLink.getStartNode().getName();
//
//		this.tableData[0][tdIterator] = LangModelMap.getString("End_node_id");
//		this.tableData[1][tdIterator++] = physicalLink.getStartNode().getName();
//
//		this.tableData[0][tdIterator] = "";
//		this.tableData[1][tdIterator++] = "";
//
//		this.tableData[0][tdIterator] = LangModelMap.getString("tunnelCableList");
//		this.tableData[1][tdIterator++] = "";
//
//		this.tableData[0][tdIterator] = LangModelMap.getString("Cable");
//		if(pipePath != null)
//			this.tableData[1][tdIterator++] = LangModelMap
//					.getString("maptunnelposit");
//		else
//			this.tableData[1][tdIterator++] = LangModelMap
//					.getString("mapcollectorposit");
//
//		// Getting scheme cable link iterator
//		ListIterator sclIterator = physicalLink.getBinding().getBindObjects()
//				.listIterator();
//
//		for(; sclIterator.hasNext();)
//		{
//			SchemeCableLink cableLink = (SchemeCableLink )sclIterator.next();
//
//			// Имя колодца/узла
//			this.tableData[0][tdIterator] = cableLink.getName();
//			// Информация о тоннеле - строка типа Тоннель тон.1, место N, L =
//			// xxx
//			IntPoint binding = physicalLink.getBinding().getBinding(cableLink);
//			this.tableData[1][tdIterator++] = Integer.toString(binding.x) + ":"
//					+ Integer.toString(binding.y);
//		}
//	}
//
//	public int getRowCount()
//	{
//		// Если данные можно разложить поровну на такое количество столбцов
//		if(this.length % this.getDivisionsNumber() == 0)
//			return (this.length / this.getDivisionsNumber());
//		// а если нельзя, то добавляем ещё ряд
//		return (this.length / this.getDivisionsNumber()) + 1;
//	}
//
//	public Object getValueAt(int row, int col)
//	{
//		int index = this.getRowCount()
//				* (col / this.getBaseColumnCount()) + row - 1;
//		if(index >= this.length)
//			return "";
//
//		return this.tableData[col % this.getBaseColumnCount()][index];
//	}
//}
