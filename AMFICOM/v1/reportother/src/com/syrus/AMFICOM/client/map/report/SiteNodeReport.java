/*
 * $Id: SiteNodeReport.java,v 1.7 2005/10/04 08:33:46 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.TableDataRenderingComponent;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

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
				getTableColumnWidths(vertDivisionsCount));
		} catch (ApplicationException e) {
			Log.errorMessage("SiteNodeReport.createReport | " + e.getMessage());
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
			tableColumnWidths.add(PROPERTY_NAME_COLUMN_WIDTH);
			tableColumnWidths.add(PROPERTY_VALUE_COLUMN_WIDTH);
		}
		return tableColumnWidths;
	}
}

class SiteNodeInfoTableModel extends AbstractTableModel {
	private static final String PARAMETER_NAME = "report.UI.propertyName";
	private static final String PARAMETER_VALUE = "report.UI.propertyValue";
	
	private static final String NAME = "report.Modules.SchemeEditor.Common.name";		
	private static final String TYPE = "report.Modules.SchemeEditor.Common.type";
	private static final String DESCRIPTION = "report.Modules.SchemeEditor.Common.description";

	private static final String CITY_KURZ = MapEditorResourceKeys.LABEL_CITY_KURZ;
	private static final String STREET_KURZ = MapEditorResourceKeys.LABEL_STREET_KURZ;
	private static final String BUILDING_KURZ = MapEditorResourceKeys.LABEL_BUILDING_KURZ;
	
	private static final String ATTACHED_SCHEME_ELEMENTS =
		"report.Modules.Map.SiteNodeReport.schemeElementsAttached";
	private static final String IN_OUT_CABLES =
		"report.Modules.Map.SiteNodeReport.cablesPassingInOut";
	private static final String THROUGH_CABLES =
		"report.Modules.Map.SiteNodeReport.cablesPassingThrough";	
	
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

		this.propertyNamesColumn.add(LangModelReport.getString(NAME));
		this.propertyValuesColumn.add(siteNode.getName());
		this.propertyNamesColumn.add(LangModelReport.getString(TYPE));
		this.propertyValuesColumn.add(siteNode.getType().getName());
		this.propertyNamesColumn.add(LangModelReport.getString(DESCRIPTION));
		this.propertyValuesColumn.add(siteNode.getDescription());
		
		DoublePoint location = siteNode.getLocation();
		this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.LONGITUDE));
		this.propertyValuesColumn.add(Double.toString(location.getX()));
		this.propertyNamesColumn.add(LangModelScheme.getString(SchemeResourceKeys.LATITUDE));
		this.propertyValuesColumn.add(Double.toString(location.getY()));

		String cityString = siteNode.getCity();
		String streetString = siteNode.getStreet();
		String buildingString = siteNode.getBuilding();
		String addressString = "";
		if (!cityString.equals(EMPTY_STRING))
			addressString += 
				(LangModelMap.getString(CITY_KURZ)
				+ cityString
				+ ADDRESS_SEPARATOR);		
		if (!streetString.equals(EMPTY_STRING))
			addressString += 
				(LangModelMap.getString(STREET_KURZ)
				+ streetString
				+ ADDRESS_SEPARATOR);		
		if (!buildingString.equals(EMPTY_STRING))
			addressString += 
				(LangModelMap.getString(BUILDING_KURZ)
				+ buildingString
				+ ADDRESS_SEPARATOR);		

		this.propertyNamesColumn.add(LangModelReport.getString(ADDRESS));
		this.propertyValuesColumn.add(addressString);
		
		this.originalRowCount += 6;
		
		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(LangModelReport.getString(ATTACHED_SCHEME_ELEMENTS));
		this.propertyValuesColumn.add(EMPTY_STRING);
		
		this.originalRowCount += 2;
		
		//�������� �� ������������, ������������� � ����
		LinkedIdsCondition condition = new LinkedIdsCondition(
				siteNode.getId(),
				ObjectEntities.SCHEMEELEMENT_CODE);
		Set<SchemeElement> schemeElementsSet =
			StorableObjectPool.getStorableObjectsByCondition(condition,true);
		for (SchemeElement schemeElement : schemeElementsSet) {
			this.propertyNamesColumn.add(
					schemeElement.getProtoEquipment().getName());
			this.propertyValuesColumn.add(schemeElement.getName());
			this.originalRowCount++;
		}
		
		//�������� ��� ������, ���������� ����� ����
		LinkedIdsCondition condition1 = new LinkedIdsCondition(
				siteNode.getId(),
				ObjectEntities.CABLECHANNELINGITEM_CODE);
		Set<CableChannelingItem> cableChanellingItemsSet =
			StorableObjectPool.getStorableObjectsByCondition(condition1,true);
		
		//TODO ����� ���� ���������� ��������/��������� ��� ����� �������
//		Set<SchemeCableLink> cableLinksPassingThroughSet =
//			new HashSet<SchemeCableLink>();
//		for (SchemeCableLink cableLink : cableLinksSet) {
//
//		}
		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(LangModelReport.getString(THROUGH_CABLES));
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.originalRowCount += 2;		
		for (CableChannelingItem ccItem : cableChanellingItemsSet) {
			this.propertyNamesColumn.add(
				ccItem.getParentPathOwner().getName());
			this.propertyValuesColumn.add(EMPTY_STRING);
			this.originalRowCount++;			
		}
		
		this.propertyNamesColumn.add(EMPTY_STRING);
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.propertyNamesColumn.add(LangModelReport.getString(IN_OUT_CABLES));
		this.propertyValuesColumn.add(EMPTY_STRING);
		this.originalRowCount += 2;		
//		for (SchemeCableLink cableLink : cableLinksSet) {
//			this.propertyNamesColumn.add(cableLink.getName());
//			this.propertyValuesColumn.add(EMPTY_STRING);
//			this.originalRowCount++;			
//		}
		
		//��������� ����� ����� � �������� ��� �������
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
		switch (columnIndex % SiteNodeReport.COLUMNS_COUNT) {
		case 0:
			return LangModelReport.getString(PARAMETER_NAME);
		case 1:
			return LangModelReport.getString(PARAMETER_VALUE);
			
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
