package com.syrus.AMFICOM.client.map.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * Отчёт "Прокладка кабеля"
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/09/25 16:23:18 $
 * @module reportother
 */
public class CableLayoutReport {
	protected static final int COLUMNS_COUNT = 4;
	private static final int SITE_NAME_COLUMN_WIDTH = 100;
	private static final int CABLE_ENTRANCE_RESERVE_COLUMN_WIDTH = 100;
	private static final int CABLE_EXIT_RESERVE_COLUMN_WIDTH = 100;	
	private static final int TUNNEL_INFO_COLUMN_WIDTH = 150;
	
	public static TableDataRenderingComponent createReport(
			TableDataStorableElement tableStorableElement,
			SchemeCableLink cableLink) {
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		TableDataRenderingComponent renderingComponent = null;
		
		renderingComponent = new TableDataRenderingComponent(
			tableStorableElement,
			new CableLayoutReportTableModel(
					cableLink,
					vertDivisionsCount),
			getTableColumnWidths(vertDivisionsCount));
		
		return renderingComponent;
	}
	
	private static List<Integer> getTableColumnWidths(int vertDivisionsCount) {
		List<Integer> tableColumnWidths = new ArrayList<Integer>();
		
		for (int j = 0; j < vertDivisionsCount; j++) {
			tableColumnWidths.add(SITE_NAME_COLUMN_WIDTH);
			tableColumnWidths.add(CABLE_ENTRANCE_RESERVE_COLUMN_WIDTH);
			tableColumnWidths.add(CABLE_EXIT_RESERVE_COLUMN_WIDTH);
			tableColumnWidths.add(TUNNEL_INFO_COLUMN_WIDTH);
		}
		return tableColumnWidths;
	}
}

class CableLayoutReportTableModel extends AbstractTableModel {
	private static final String START_NODE = MapEditorResourceKeys.LABEL_START_NODE;
	private static final String START_SPARE = MapEditorResourceKeys.LABEL_START_SPARE;
	private static final String END_SPARE = MapEditorResourceKeys.LABEL_END_SPARE;
	private static final String START_LINK = MapEditorResourceKeys.LABEL_END_NODE;
	private static final String PARAMETER_NAME = "report.UI.propertyName";
	private static final String PARAMETER_VALUE = "report.UI.propertyValue";
	
	private static final String METRIC = MapEditorResourceKeys.VALUE_METRIC;
	private static final String TUNNEL = MapEditorResourceKeys.LABEL_TUNNEL;
	private static final String COLLECTOR = MapEditorResourceKeys.ENTITY_COLLECTOR;
	private static final String MAP_TUNNEL_POSIT = MapEditorResourceKeys.LABEL_PLACE_IN_TUNNEL;
	private static final String EMPTY_OBJECT_STRING = "--";
	private static final String EMPTY_STRING = "";
	
	private int vertDivisionsCount = 1;
	private int originalRowCount = 0;	
	private int rowCount = 1;
	private int columnCount = 1;	
	
	private List<String> siteNodeNameColumn = new ArrayList<String>();
	private List<String> startSpareColumn = new ArrayList<String>();
	private List<String> endSpareColumn = new ArrayList<String>();
	private List<String> startLinkColumn = new ArrayList<String>();
	
	protected CableLayoutReportTableModel (
			SchemeCableLink cableLink,
			int vertDivisionsCount) {
		this.vertDivisionsCount = vertDivisionsCount;

		this.siteNodeNameColumn.add(LangModelMap.getString(START_NODE));
		this.startSpareColumn.add(LangModelMap.getString(START_SPARE));
		this.endSpareColumn.add(LangModelMap.getString(END_SPARE));
		this.startLinkColumn.add(LangModelMap.getString(START_LINK));
		
		int currentItemIndex = 0;
		Set<CableChannelingItem> channelingItems = cableLink.getPathMembers();
		for(CableChannelingItem chanellingItem : channelingItems) {
			// Имя колодца/узла
			String siteNodeName = this.getSiteNodeFullName(chanellingItem.getStartSiteNode());
			this.siteNodeNameColumn.add(siteNodeName);
			
			// Запас на входе
			this.startSpareColumn.add(Double.toString(chanellingItem.getStartSpare()));
			
			// Запас на выходе
			String endSpareString = null;
			if(currentItemIndex != 0)
				endSpareString = Double.toString(chanellingItem.getEndSpare());
			else
				endSpareString = EMPTY_OBJECT_STRING;
			
			this.endSpareColumn.add(endSpareString);
			
			// Информация о тоннеле - строка типа Тоннель тон.1, место N, L =
			// xxx
			String tunnelInfoString = null;
			if(currentItemIndex == channelingItems.size() - 1)
				tunnelInfoString = EMPTY_OBJECT_STRING;
			else {
				//Тип и имя тоннеля
				PhysicalLink physicalLink = chanellingItem.getPhysicalLink();

				//TODO получаем Map по кондишену
				Map map = null;
				
				Collector pipePath = map.getCollector(physicalLink);
				if(pipePath != null)
					tunnelInfoString =
						LangModelMap.getString(COLLECTOR)
						+ pipePath.getName();
				else
					tunnelInfoString =
						LangModelMap.getString(TUNNEL)
						+ physicalLink.getName();

				//TODO Как-то вычисляется положение. Без биндинга надо
				int place = physicalLink.getBinding().getSequenceNumber(
						chanellingItem.getRowX(),
						chanellingItem.getPlaceY());

				// Место в тоннеле
				tunnelInfoString += 
					(","
					+ LangModelMap.getString(MAP_TUNNEL_POSIT)
					+ ": " + Integer.toString(place));

				// Длина тоннеля
				tunnelInfoString += 
					(", L = "
					+ physicalLink.getLengthLt()
					+ LangModelMap.getString(METRIC));
			}
			this.startLinkColumn.add(tunnelInfoString);
			
			currentItemIndex++;
		}
		
		this.originalRowCount = channelingItems.size() + 1;
		//Вычисляем число строк и столбцов для таблицы
		this.rowCount = this.originalRowCount / this.vertDivisionsCount;
		if (this.originalRowCount % this.vertDivisionsCount > 0)		
			this.rowCount++;
		
		this.columnCount = CableLayoutReport.COLUMNS_COUNT * this.vertDivisionsCount;
	}
	
	private String getSiteNodeFullName (SiteNode siteNode) {
		SiteNodeType nodeType = siteNode.getType();
		return nodeType.getName() + " "	+ siteNode.getName();
	}
	
	public int getRowCount() {
		return this.rowCount;
	}

	public int getColumnCount() {
		return this.columnCount;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex % CableLayoutReport.COLUMNS_COUNT) {
		case 0:
			return LangModelReport.getString(PARAMETER_NAME);
		case 1:
			return LangModelReport.getString(PARAMETER_VALUE);
			
		}
		throw new AssertionError("TestReportTableModel.getColumnName | Unreachable code");
    }	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		int index = this.getRowCount() * (columnIndex / CableLayoutReport.COLUMNS_COUNT) + rowIndex;
		if (index >= this.originalRowCount)
			return EMPTY_STRING;

		switch (columnIndex % CableLayoutReport.COLUMNS_COUNT) {
			case 0:
				return this.siteNodeNameColumn.get(index);
			case 1:
				return this.endSpareColumn.get(index);
			case 2:
				return this.startLinkColumn.get(index);
			case 3:
				return this.startSpareColumn.get(index);
		}

		throw new AssertionError("CableLayoutReportTableModel.getValueAt | Unreachable code");
	}
}
