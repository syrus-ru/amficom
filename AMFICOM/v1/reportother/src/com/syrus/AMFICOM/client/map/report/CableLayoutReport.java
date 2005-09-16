package com.syrus.AMFICOM.client.map.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.client.resource.LangModelMap;
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
 * @author $Author: peskovsky $
 * @version $Revision: 1.3 $, $Date: 2005/09/16 13:26:27 $
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
		
		renderingComponent = new TableDataRenderingComponent(
			tableStorableElement,
			new CableLayoutReportTableModel(
					cableLink,
					vertDivisionsCount),
			createTableColumnModel(vertDivisionsCount));
		
		return renderingComponent;
	}
	
	private static TableColumnModel createTableColumnModel(int vertDivisionsCount) {
		TableColumnModel tableColumnModel = new DefaultTableColumnModel();
		
		for (int j = 0; j < vertDivisionsCount; j++) {
			tableColumnModel.addColumn(new TableColumn(
					j * COLUMNS_COUNT,
					SITE_NAME_COLUMN_WIDTH));
			tableColumnModel.addColumn(new TableColumn(
					j * COLUMNS_COUNT + 1,
					CABLE_ENTRANCE_RESERVE_COLUMN_WIDTH));
			tableColumnModel.addColumn(new TableColumn(
					j * COLUMNS_COUNT + 2,
					CABLE_EXIT_RESERVE_COLUMN_WIDTH));
			tableColumnModel.addColumn(new TableColumn(
					j * COLUMNS_COUNT + 3,
					TUNNEL_INFO_COLUMN_WIDTH));
		}
		return tableColumnModel;
	}
}

class CableLayoutReportTableModel extends AbstractTableModel {
	private static final String START_NODE = "StartNode";
	private static final String START_SPARE = "StartSpare";
	private static final String END_SPARE = "EndSpare";
	private static final String START_LINK = "StartLink";
	
	private static final String METRIC = "metric";
	private static final String TUNNEL = "Tunnel";
	private static final String COLLECTOR = "Collector";
	private static final String MAP_TUNNEL_POSIT = "maptunnelposit";
	private static final String EMPTY_STRING = "--";
	
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
				endSpareString = EMPTY_STRING;
			
			this.endSpareColumn.add(endSpareString);
			
			// Информация о тоннеле - строка типа Тоннель тон.1, место N, L =
			// xxx
			String tunnelInfoString = null;
			if(currentItemIndex == channelingItems.size() - 1)
				tunnelInfoString = EMPTY_STRING;
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

	public Object getValueAt(int rowIndex, int columnIndex) {
		int index = this.getRowCount() * (columnIndex / CableLayoutReport.COLUMNS_COUNT) + rowIndex;
		if (index >= this.originalRowCount)
			throw new AssertionError("CableLayoutReportTableModel.getValueAt | Index exceeds data size");

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
