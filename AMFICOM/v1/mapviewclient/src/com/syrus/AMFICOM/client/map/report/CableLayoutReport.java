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
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * Отчёт "Прокладка кабеля"
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2005/10/26 10:12:32 $
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
			SchemeCableLink cableLink) throws CreateReportException {
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		TableDataRenderingComponent renderingComponent = null;
		
		try {
			renderingComponent = new TableDataRenderingComponent(
				tableStorableElement,
				new CableLayoutReportTableModel(
						cableLink,
						vertDivisionsCount),
				getTableColumnWidths(vertDivisionsCount));
		} catch (ApplicationException e) {
			Log.errorMessage("TunnelCableListReport.createReport | " + e.getMessage());
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
			tableColumnWidths.add(Integer.valueOf(SITE_NAME_COLUMN_WIDTH));
			tableColumnWidths.add(Integer.valueOf(CABLE_ENTRANCE_RESERVE_COLUMN_WIDTH));
			tableColumnWidths.add(Integer.valueOf(CABLE_EXIT_RESERVE_COLUMN_WIDTH));
			tableColumnWidths.add(Integer.valueOf(TUNNEL_INFO_COLUMN_WIDTH));
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
			int vertDivisionsCount) throws ApplicationException{
		this.vertDivisionsCount = vertDivisionsCount;

		this.siteNodeNameColumn.add(I18N.getString(START_NODE));
		this.startSpareColumn.add(I18N.getString(START_SPARE));
		this.endSpareColumn.add(I18N.getString(END_SPARE));
		this.startLinkColumn.add(I18N.getString(START_LINK));
		
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
			StringBuffer tunnelInfoStringBuffer = new StringBuffer();
			if(currentItemIndex != channelingItems.size() - 1) {
				//Тип и имя тоннеля
				PhysicalLink physicalLink = chanellingItem.getPhysicalLink();
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
				if (collectorsIterator.hasNext()) {
					Collector collector = collectorsIterator.next();
					tunnelInfoStringBuffer.append(I18N.getString(COLLECTOR));
					tunnelInfoStringBuffer.append(collector.getName());
				}
				else {
					tunnelInfoStringBuffer.append(I18N.getString(TUNNEL));
					tunnelInfoStringBuffer.append(physicalLink.getName());
				}

				// Место в тоннеле
				tunnelInfoStringBuffer.append(",");
				tunnelInfoStringBuffer.append(I18N.getString(MAP_TUNNEL_POSIT));
				tunnelInfoStringBuffer.append(": (");
				tunnelInfoStringBuffer.append(chanellingItem.getRowX());
				tunnelInfoStringBuffer.append(chanellingItem.getPlaceY());
				tunnelInfoStringBuffer.append(")");

				// Длина тоннеля
				tunnelInfoStringBuffer.append(", L = ");
				tunnelInfoStringBuffer.append(physicalLink.getLengthLt());
				tunnelInfoStringBuffer.append(I18N.getString(METRIC));
			}
			this.startLinkColumn.add(tunnelInfoStringBuffer.toString());
			
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
			return I18N.getString(PARAMETER_NAME);
		case 1:
			return I18N.getString(PARAMETER_VALUE);
			
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
