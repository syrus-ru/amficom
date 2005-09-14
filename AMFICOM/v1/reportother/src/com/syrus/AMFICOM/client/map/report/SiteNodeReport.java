/*
 * $Id: SiteNodeReport.java,v 1.1 2005/09/14 14:35:45 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkWrapper;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.resource.IntPoint;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

public class SiteNodeReport {
	protected static final int COLUMNS_COUNT = 2;
	private static final int PROPERTY_NAME_COLUMN_WIDTH = 200;
	private static final int PROPERTY_VALUE_COLUMN_WIDTH = 150;
	
	public static TableDataRenderingComponent createReport(
			TableDataStorableElement tableStorableElement,
			SiteNode siteNode) throws CreateReportException {
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		TableDataRenderingComponent renderingComponent = null;
		
		try {
			renderingComponent = new TableDataRenderingComponent(
				tableStorableElement,
				new SiteNodeInfoTableModel(
						siteNode,
						vertDivisionsCount),
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

class SiteNodeInfoTableModel extends AbstractTableModel {
	private static final String NAME = "report.Modules.SchemeEditor.Common.name";		
	private static final String TYPE = "report.Modules.SchemeEditor.Common.type";
	private static final String DESCRIPTION = "report.Modules.SchemeEditor.Common.description";

	private static final String CITY_KURZ = "CityKurz";
	private static final String STREET_KURZ = "StreetKurz";
	private static final String BUILDING_KURZ = "BuildingKurz";
	
	private static final String TOPOLOGICAL_LENGTH = "TopologicalLength";
	private static final String TUNNEL = "Tunnel";
	private static final String COLLECTOR = "Collector";
	private static final String CABLE = "cable";	
	private static final String TUNNEL_CABLE_LIST = "tunnelCableList";
	private static final String MAP_TUNNEL_POSIT = "maptunnelposit";
	private static final String MAP_COLLECTOR_POSIT = "mapcollectorposit";	
	
	
	private static final String ADDRESS = "report.Modules.Map.Common.address";
	private static final String ADDRESS_SEPARATOR = ", ";	
	
	private static final String EMPTY_STRING = "";
	
	private int vertDivisionsCount = 1;
	private int originalRowCount = 0;	
	private int rowCount = 1;
	private int columnCount = 1;	
	
	private List<String> propertyNamesColumn = new ArrayList<String>();
	private List<String> propertyValuesColumn = new ArrayList<String>();		
	
	protected SiteNodeInfoTableModel (
			SiteNode siteNode,
			int vertDivisionsCount) throws ApplicationException {
		this.vertDivisionsCount = vertDivisionsCount;

		//TODO Получается из Condition
		MapView mapViewObject = null;
//		mapViewObject.get
//		Collector pipePath = mapViewObject.getCollector(siteNode);
//		String nameString = null;		
//		String typeString = null;
//		String descriptionString = null;		
//		if(pipePath != null) {
//			nameString = pipePath.getName();
//			typeString = LangModelMap.getString(COLLECTOR);
//			descriptionString = pipePath.getDescription();
//		}
//		else {
//			nameString = siteNode.getName();
//			typeString = LangModelMap.getString(TUNNEL);
//			descriptionString = siteNode.getDescription();			
//		}
//		
//		this.propertyNamesColumn.add(LangModelReport.getString(NAME));
//		this.propertyValuesColumn.add(nameString);
//		this.propertyNamesColumn.add(LangModelReport.getString(TYPE));
//		this.propertyValuesColumn.add(typeString);
//		this.propertyNamesColumn.add(LangModelReport.getString(DESCRIPTION));
//		this.propertyValuesColumn.add(descriptionString);
//		
//		this.propertyNamesColumn.add(LangModelMap.getString(PhysicalLinkWrapper.COLUMN_START_NODE_ID));
//		this.propertyValuesColumn.add(siteNode.getStartNode().getName());
//		this.propertyNamesColumn.add(LangModelMap.getString(PhysicalLinkWrapper.COLUMN_END_NODE_ID));
//		this.propertyValuesColumn.add(siteNode.getEndNode().getName());
//		
//		this.propertyNamesColumn.add(LangModelReport.getString(TOPOLOGICAL_LENGTH));
//		this.propertyValuesColumn.add(Double.toString(siteNode.getLengthLt()));
//
//		this.propertyNamesColumn.add(LangModelReport.getString(ADDRESS));
//		this.propertyValuesColumn.add(
//				CITY_KURZ
//				+ siteNode.getCity()
//				+ ADDRESS_SEPARATOR
//				+ STREET_KURZ
//				+ siteNode.getStreet()
//				+ ADDRESS_SEPARATOR
//				+ BUILDING_KURZ
//				+ siteNode.getBuilding());
//		
//		this.originalRowCount += 7;
//		
//		this.propertyNamesColumn.add(EMPTY_STRING);
//		this.propertyValuesColumn.add(EMPTY_STRING);
//		this.propertyNamesColumn.add(LangModelMap.getString(TUNNEL_CABLE_LIST));
//		this.propertyValuesColumn.add(EMPTY_STRING);
//		
//		this.propertyNamesColumn.add(LangModelMap.getString(CABLE));
//		String subTableColumnHeader = null;
//		if(pipePath != null)
//			subTableColumnHeader = LangModelMap.getString(MAP_TUNNEL_POSIT);
//		else
//			subTableColumnHeader = LangModelMap.getString(MAP_COLLECTOR_POSIT);			
//		this.propertyValuesColumn.add(subTableColumnHeader);
//
//		this.originalRowCount += 3;
//		
//		// Getting scheme cable link iterator
//		Iterator sclIterator = siteNode.getBinding().getBindObjects().iterator();
//		for(; sclIterator.hasNext();) {
//			SchemeCableLink cableLink = (SchemeCableLink)sclIterator.next();
//			// Имя колодца/узла
//			this.propertyNamesColumn.add(cableLink.getName());
//			// Информация о тоннеле - строка типа Тоннель тон.1, место N, L =
//			// xxx
//			IntPoint binding = siteNode.getBinding().getBinding(cableLink);
//			this.propertyValuesColumn.add(Integer.toString(binding.x) + ":"
//					+ Integer.toString(binding.y));
//			this.originalRowCount++;
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
