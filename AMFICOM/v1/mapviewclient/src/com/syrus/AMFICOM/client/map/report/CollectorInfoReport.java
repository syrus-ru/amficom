/*
 * $Id: CollectorInfoReport.java,v 1.3 2005/10/26 10:12:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.report;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.report.TableDataStorableElement;

public class CollectorInfoReport {
	protected static final int COLUMNS_COUNT = 2;
	private static final int PROPERTY_NAME_COLUMN_WIDTH = 200;
	private static final int PROPERTY_VALUE_COLUMN_WIDTH = 150;
	
	public static TableDataRenderingComponent createReport(
			TableDataStorableElement tableStorableElement,
			Collector collector) {
		int vertDivisionsCount = tableStorableElement.getVerticalDivisionsCount();
		TableDataRenderingComponent renderingComponent = null;
		
		renderingComponent = new TableDataRenderingComponent(
			tableStorableElement,
			new CollectorInfoReportTableModel(
					collector,
					vertDivisionsCount),
			getTableColumnWidths(vertDivisionsCount));
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

class CollectorInfoReportTableModel extends AbstractTableModel {
	private static final String PARAMETER_NAME = "report.UI.propertyName";
	private static final String PARAMETER_VALUE = "report.UI.propertyValue";
	
	private static final String NAME = "report.Modules.SchemeEditor.Common.name";		
	private static final String TYPE = "report.Modules.SchemeEditor.Common.type";
	private static final String DESCRIPTION = "report.Modules.SchemeEditor.Common.description";
	private static final String SUMM_LENGTH = "report.Modules.Map.CollectorInfoReport.summLength";
	private static final String COLLECTOR = MapEditorResourceKeys.ENTITY_COLLECTOR;
	private static final String METRIC = MapEditorResourceKeys.VALUE_METRIC;
	private static final String INCLUDING_NODES = "report.Modules.Map.CollectorInfoReport.includingNodes";
	private static final String INCLUDING_PIQUETS = "report.Modules.Map.CollectorInfoReport.includingPiquets";
	
	private static final String EMPTY_STRING = "";
	
	private int vertDivisionsCount = 1;
	private int originalRowCount = 0;	
	private int rowCount = 1;
	private int columnCount = 1;	
	
	private List<String> propertyNamesColumn = new ArrayList<String>();
	private List<String> propertyValuesColumn = new ArrayList<String>();		
	
	protected CollectorInfoReportTableModel (
			Collector collector,
			int vertDivisionsCount) {
		this.vertDivisionsCount = vertDivisionsCount;

		this.propertyNamesColumn.add(I18N.getString(NAME));
		this.propertyValuesColumn.add(collector.getName());
		this.propertyNamesColumn.add(I18N.getString(TYPE));
		this.propertyValuesColumn.add(I18N.getString(COLLECTOR));
		this.propertyNamesColumn.add(I18N.getString(DESCRIPTION));
		this.propertyValuesColumn.add(collector.getDescription());
		
		this.propertyNamesColumn.add(I18N.getString(SUMM_LENGTH));
		this.propertyValuesColumn.add(
				Double.toString(collector.getLengthLt())
				+ I18N.getString(METRIC));
		this.originalRowCount += 4;

		Set<SiteNode> collectorNodes = new HashSet<SiteNode>();
		for (PhysicalLink physicalLink : collector.getPhysicalLinks()) {
			AbstractNode startNode = physicalLink.getStartNode();
			if (startNode instanceof SiteNode)
				collectorNodes.add((SiteNode)startNode);
			
			AbstractNode endNode = physicalLink.getEndNode();
			if (endNode instanceof SiteNode)
				collectorNodes.add((SiteNode)endNode);
		}
		
		//Соседние узлы
		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(I18N.getString(INCLUDING_NODES));
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.originalRowCount += 2;		
		
		for (SiteNode siteNode : collectorNodes) {
			SiteNodeType siteNodeType = siteNode.getType();
			if (!siteNodeType.getSort().equals(SiteNodeTypeSort.WELL))
				continue;
			
			this.propertyNamesColumn.add(siteNodeType.getName());
			this.propertyValuesColumn.add(siteNode.getName());
			this.originalRowCount++;			
		}
		
		//Пикеты
		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(I18N.getString(INCLUDING_PIQUETS));
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.originalRowCount += 2;		
		
		for (SiteNode siteNode : collectorNodes) {
			SiteNodeType siteNodeType = siteNode.getType();
			if (!siteNodeType.getSort().equals(SiteNodeTypeSort.PIQUET))
				continue;
			
			this.propertyNamesColumn.add(siteNodeType.getName());
			this.propertyValuesColumn.add(siteNode.getName());
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

	public String getColumnName(int columnIndex) {
		switch (columnIndex % CollectorInfoReport.COLUMNS_COUNT) {
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
