/*
 * $Id: MapReportModel.java,v 1.18 2005/10/31 12:30:08 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.report;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.StandAloneNetMapViewGenerator;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

public class MapReportModel extends ReportModel {
	// Названия отчётов для карты
	/**
	 * Изображение карты
	 */ 
	public static String TOPOLOGY_IMAGE = "topologyImage";
	/**
	 * Прокладка кабеля по тоннелям (список колодцев и тоннелей)
	 */ 
	public static String CABLE_LAYOUT = "cableLayout";
	/**
	 * Характеристики объекта
	 */
	public static String SELECTED_OBJECT_CHARS = "selectedObjectChars";
	
	//Параметры для отображения топографии для элемента
	public static String MAPVIEW_OBJECT = "mapViewObject";
	public static String CENTER = "center";
	public static String SCALE = "scale";
	public static String MAPFRAME_SIZE = "size";	
	
	public MapReportModel(){
	}

	@Override
	public ReportType getReportKind(String reportName){
		ReportType result = ReportType.TABLE;
		if (reportName.equals(TOPOLOGY_IMAGE))
			result = ReportType.GRAPH;
		return result;
	}
	
	@Override
	public RenderingComponent createReport(
			DataStorableElement element,
			Object data,
			ApplicationContext aContext) throws CreateReportException{
		RenderingComponent result = null;
		String reportName = element.getReportName();
		String modelClassName = element.getModelClassName();		
		try {
			if (reportName.equals(TOPOLOGY_IMAGE)) {
				if (!(data instanceof Map))
					throw new CreateReportException(
							reportName,
							modelClassName,							
							CreateReportException.WRONG_DATA_TO_INSTALL);
				
				Map topoImageReportData = (Map)data;
				MapView mapViewObject = (MapView)topoImageReportData.get(MAPVIEW_OBJECT);
				DoublePoint center = (DoublePoint)topoImageReportData.get(CENTER);
				Double scale = (Double)topoImageReportData.get(SCALE);
				Dimension mapFrameSize = (Dimension)topoImageReportData.get(MAPFRAME_SIZE);				
				
				if (	mapViewObject == null
					||	center == null
					||	scale == null
					||	mapFrameSize == null) {
					Log.errorMessage("MapReportModel.createReport - got null data from Command.");
					throw new CreateReportException(
							reportName,
							modelClassName,					
							CreateReportException.NO_DATA_TO_INSTALL);
				}
				
				BufferedImage image = null;
				try {
					image = StandAloneNetMapViewGenerator.getMapShot(
							mapViewObject,
							center,
							scale.doubleValue() * mapFrameSize.getWidth() / element.getWidth(),
							new Dimension(element.getWidth(),element.getHeight()));
				} catch (MapException e) {
					Log.errorMessage(e.getMessage());
					Log.errorMessage(e);			
					throw new CreateReportException(
							reportName,
							modelClassName,					
							CreateReportException.NO_DATA_TO_INSTALL);
				}
				result = new ImageRenderingComponent(element,image);
				result.setWidth(element.getWidth());
				result.setHeight(element.getHeight());			
			}
			else {
				if (!(data instanceof Identifier))
					throw new CreateReportException(
							reportName,
							modelClassName,							
							CreateReportException.WRONG_DATA_TO_INSTALL);
				
				Identifier objectID = (Identifier)data;
				
				if (reportName.equals(MapReportModel.CABLE_LAYOUT)) {
					if (objectID.getMajor() == ObjectEntities.SCHEMECABLELINK_CODE) {
						SchemeCableLink schemeCableLink = StorableObjectPool.getStorableObject(objectID,true);
						result = CableLayoutReport.createReport(
								(TableDataStorableElement)element,
								schemeCableLink);
					}
				}
				else if (reportName.equals(SELECTED_OBJECT_CHARS)) {
					if (objectID.getMajor() == ObjectEntities.PHYSICALLINK_CODE) {
						PhysicalLink physicalLink =
							StorableObjectPool.getStorableObject(objectID,true);
						result = TunnelCableListReport.createReport(
								(TableDataStorableElement)element,
								physicalLink);
					}
					else if (objectID.getMajor() == ObjectEntities.COLLECTOR_CODE) {
						Collector collector =
							StorableObjectPool.getStorableObject(objectID,true);
						result = CollectorInfoReport.createReport(
								(TableDataStorableElement)element,
								collector);
					}
					else if (objectID.getMajor() == ObjectEntities.SITENODE_CODE) {
						SiteNode siteNode =
							StorableObjectPool.getStorableObject(objectID,true);
						result = SiteNodeReport.createReport(
								(TableDataStorableElement)element,
								siteNode);
					}
				}
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e.getMessage());
			Log.errorMessage(e);			
			throw new CreateReportException(
					reportName,
					modelClassName,					
					CreateReportException.ERROR_GETTING_FROM_POOL);
		}
	
		if (result == null)
			throw new CreateReportException(
				reportName,
				modelClassName,				
				CreateReportException.WRONG_DATA_TO_INSTALL);
		
		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		// TODO Вообще-то, эта информация должна храниться в
		// других I18N'ах и, соответственно, методы должны
		//быть в моделях отчётов - наследницах
		String langReportName = null;
		if (	reportName.equals(TOPOLOGY_IMAGE)
			||	reportName.equals(CABLE_LAYOUT)			
			||	reportName.equals(SELECTED_OBJECT_CHARS))
			langReportName = I18N.getString("report.Modules.Map." + reportName);
			
		return langReportName;
	}
	
	@Override
	public String getName() {
		return DestinationModules.MAP;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(TOPOLOGY_IMAGE);
		result.add(CABLE_LAYOUT);
		result.add(SELECTED_OBJECT_CHARS);		
		
		return result;
	}
}
