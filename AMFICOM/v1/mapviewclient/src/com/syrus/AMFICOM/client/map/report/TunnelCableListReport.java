package com.syrus.AMFICOM.client.map.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

public class TunnelCableListReport {
	protected static final int COLUMNS_COUNT = 2;
	private static final int PROPERTY_NAME_COLUMN_WIDTH = 200;
	private static final int PROPERTY_VALUE_COLUMN_WIDTH = 150;
	
	public static TableDataRenderingComponent createReport(
			TableDataStorableElement tableStorableElement,
			PhysicalLink physicalLink) throws CreateReportException {
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		TableDataRenderingComponent renderingComponent = null;
		
		try {
			renderingComponent = new TableDataRenderingComponent(
				tableStorableElement,
				new TunnelCableListTableModel(
						physicalLink,
						vertDivisionsCount),
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
			tableColumnWidths.add(Integer.valueOf(PROPERTY_NAME_COLUMN_WIDTH));
			tableColumnWidths.add(Integer.valueOf(PROPERTY_VALUE_COLUMN_WIDTH));
		}
		return tableColumnWidths;
	}
}

class TunnelCableListTableModel extends AbstractTableModel {
	private static final String PARAMETER_NAME = "report.UI.propertyName";
	private static final String PARAMETER_VALUE = "report.UI.propertyValue";
	
	private static final String NAME = "report.Modules.SchemeEditor.Common.name";		
	private static final String TYPE = "report.Modules.SchemeEditor.Common.type";
	private static final String DESCRIPTION = "report.Modules.SchemeEditor.Common.description";

	private static final String CITY_KURZ = MapEditorResourceKeys.LABEL_CITY_KURZ;
	private static final String STREET_KURZ = MapEditorResourceKeys.LABEL_STREET_KURZ;
	private static final String BUILDING_KURZ = MapEditorResourceKeys.LABEL_BUILDING_KURZ;
	
	private static final String TOPOLOGICAL_LENGTH = MapEditorResourceKeys.LABEL_TOPOLOGICAL_LENGTH;
	private static final String TUNNEL = MapEditorResourceKeys.LABEL_TUNNEL;
	private static final String COLLECTOR = MapEditorResourceKeys.ENTITY_COLLECTOR;
	private static final String CABLE = MapEditorResourceKeys.LABEL_CABLE;	
	private static final String TUNNEL_CABLE_LIST = MapEditorResourceKeys.LABEL_CABLE_LIST;
	private static final String MAP_TUNNEL_POSIT = MapEditorResourceKeys.LABEL_PLACE_IN_TUNNEL;
	private static final String MAP_COLLECTOR_POSIT = MapEditorResourceKeys.LABEL_PLACE_IN_COLLECTOR;
	private static final String START_NODE = MapEditorResourceKeys.LABEL_START_NODE;
	private static final String END_NODE = MapEditorResourceKeys.LABEL_END_NODE;	
	
	private static final String ADDRESS = "report.Modules.Map.Common.address";
	private static final String ADDRESS_SEPARATOR = ", ";	
	
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

		//Получаем коллекторы для тоннеля
		LinkedIdsCondition condition = new LinkedIdsCondition(
				physicalLink.getId(),
				ObjectEntities.COLLECTOR_CODE);
		Set<Collector> collectorsSet =
			StorableObjectPool.getStorableObjectsByCondition(condition,true);
		Iterator<Collector> collectorsIterator = collectorsSet.iterator();
		
		//Хотя коллекторов для physicalLink'а может быть больше одного,
		//это неправильная ситуация((C)А.Крупенников). Отчёт учитывает
		//один коллектор.
		Collector collector = null;
		if (collectorsIterator.hasNext())
			collector = collectorsIterator.next();
		
		String nameString = null;		
		String typeString = null;
		String descriptionString = null;		
		if(collector != null) {
			nameString = collector.getName();
			typeString = I18N.getString(COLLECTOR);
			descriptionString = collector.getDescription();
		}
		else {
			nameString = physicalLink.getName();
			typeString = I18N.getString(TUNNEL);
			descriptionString = physicalLink.getDescription();			
		}
		
		this.propertyNamesColumn.add(I18N.getString(NAME));
		this.propertyValuesColumn.add(nameString);
		this.propertyNamesColumn.add(I18N.getString(TYPE));
		this.propertyValuesColumn.add(typeString);
		this.propertyNamesColumn.add(I18N.getString(DESCRIPTION));
		this.propertyValuesColumn.add(descriptionString);
		
		this.propertyNamesColumn.add(I18N.getString(START_NODE));
		this.propertyValuesColumn.add(physicalLink.getStartNode().getName());
		this.propertyNamesColumn.add(I18N.getString(END_NODE));
		this.propertyValuesColumn.add(physicalLink.getEndNode().getName());
		
		this.propertyNamesColumn.add(I18N.getString(TOPOLOGICAL_LENGTH));
		this.propertyValuesColumn.add(Double.toString(physicalLink.getLengthLt()));

		String cityString = physicalLink.getCity();
		String streetString = physicalLink.getStreet();
		String buildingString = physicalLink.getBuilding();
		String addressString = "";
		if (!cityString.equals(EMPTY_STRING))
			addressString += 
				(I18N.getString(CITY_KURZ)
				+ cityString
				+ ADDRESS_SEPARATOR);		
		if (!streetString.equals(EMPTY_STRING))
			addressString += 
				(I18N.getString(STREET_KURZ)
				+ streetString
				+ ADDRESS_SEPARATOR);		
		if (!buildingString.equals(EMPTY_STRING))
			addressString += 
				(I18N.getString(BUILDING_KURZ)
				+ buildingString
				+ ADDRESS_SEPARATOR);		

		this.propertyNamesColumn.add(I18N.getString(ADDRESS));
		this.propertyValuesColumn.add(addressString);
		
		this.originalRowCount += 7;
		
		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(I18N.getString(TUNNEL_CABLE_LIST));
		this.propertyValuesColumn.add(EMPTY_STRING);
		
		this.propertyNamesColumn.add(I18N.getString(CABLE));
		String subTableColumnHeader = null;
		if(collector != null)
			subTableColumnHeader = I18N.getString(MAP_TUNNEL_POSIT);
		else
			subTableColumnHeader = I18N.getString(MAP_COLLECTOR_POSIT);			
		this.propertyValuesColumn.add(subTableColumnHeader);

		this.originalRowCount += 3;

		//Получаем кабели для тоннеля
		LinkedIdsCondition condition1 = new LinkedIdsCondition(
				physicalLink.getId(),
				ObjectEntities.CABLECHANNELINGITEM_CODE);
		Set<CableChannelingItem> cableChannellingItemsSet =
			StorableObjectPool.getStorableObjectsByCondition(condition1,true);

		for(CableChannelingItem ccItem : cableChannellingItemsSet) {
			// Имя колодца/узла
			this.propertyNamesColumn.add(ccItem.getParentPathOwner().getName());
			// Информация о тоннеле - строка типа Тоннель тон.1, место N, L =
			// xxx
			this.propertyValuesColumn.add(
					ccItem.getRowX() + ":" + ccItem.getPlaceY());
			this.originalRowCount++;
		}
		
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

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex % TunnelCableListReport.COLUMNS_COUNT) {
		case 0:
			return I18N.getString(PARAMETER_NAME);
		case 1:
			return I18N.getString(PARAMETER_VALUE);
			
		}
		throw new AssertionError("TestReportTableModel.getColumnName | Unreachable code");
    }	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		int index = this.getRowCount() * (columnIndex / TunnelCableListReport.COLUMNS_COUNT) + rowIndex;
		if (index >= this.originalRowCount)
			return EMPTY_STRING;

		switch (columnIndex % TunnelCableListReport.COLUMNS_COUNT) {
			case 0:
				return this.propertyNamesColumn.get(index);
			case 1:
				return this.propertyValuesColumn.get(index);
		}

		throw new AssertionError("SchemeElementTableModel.getValueAt | Unreachable code");
	}
}
