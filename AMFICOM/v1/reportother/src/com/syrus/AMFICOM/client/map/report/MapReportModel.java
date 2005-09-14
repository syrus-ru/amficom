/*
 * $Id: MapReportModel.java,v 1.2 2005/09/14 14:35:45 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.report;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.TableDataStorableElement;

public class MapReportModel extends ReportModel {
	// Названия отчётов для карты
	/**
	 * Изображение карты
	 */ 
	public static String TOPOLOGY_IMAGE = "topologyImage";
	/**
	 * Список кабелей по тоннелю + их расположение в тоннеле
	 */ 
	public static String TUNNEL_CABLE_LIST = "tunnelCableList";
	/**
	 * Информация по коллектору (список пикетов, колодцев + длина)
	 */ 
	public static String COLLECTOR_INFO = "collectorInfo";
	/**
	 * Информация по колодцу/узлу
	 */ 
	public static String SITE_NODE_INFO = "shaftInfo";
	
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
		
		try {
			if (element.getReportName().equals(TOPOLOGY_IMAGE)) {
				if (!(data instanceof BufferedImage))
					throw new CreateReportException(
							element.getReportName(),
							CreateReportException.WRONG_DATA_TO_INSTALL);
					
				BufferedImage image = (BufferedImage)data;
				result = new ImageRenderingComponent(element,image);
				result.setWidth(element.getWidth());
				result.setHeight(element.getHeight());			
			}
			else {
				if (!(data instanceof Identifier))
					throw new CreateReportException(
							element.getReportName(),
							CreateReportException.WRONG_DATA_TO_INSTALL);
				
				Identifier objectID = (Identifier)data;
				
				if (element.getReportName().equals(TUNNEL_CABLE_LIST)) {
					if (objectID.getMajor() == ObjectEntities.PHYSICALLINK_CODE) {
						PhysicalLink physicalLink =
							StorableObjectPool.getStorableObject(objectID,true);
						result = TunnelCableListReport.createReport(
								(TableDataStorableElement)element,
								physicalLink);
					}
				}
				else if (element.getReportName().equals(SITE_NODE_INFO)) {
					if (objectID.getMajor() == ObjectEntities.SITENODE_CODE) {
						SiteNode siteNode =
							StorableObjectPool.getStorableObject(objectID,true);
						result = SiteNodeReport.createReport(
								(TableDataStorableElement)element,
								siteNode);
					}
				}

			}
		} catch (ApplicationException e) {
			throw new CreateReportException(
					element.getReportName(),
					CreateReportException.ERROR_GETTING_FROM_POOL);
		}
	
		if (result == null)
			throw new CreateReportException(
				element.getReportName(),
				CreateReportException.WRONG_DATA_TO_INSTALL);
		
		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		// TODO Вообще-то, эта информация должна храниться в
		// других LangModel'ах и, соответственно, методы должны
		//быть в моделях отчётов - наследницах
		String langReportName = null;
		if (	reportName.equals(TOPOLOGY_IMAGE)
			||	reportName.equals(TUNNEL_CABLE_LIST)
			||	reportName.equals(COLLECTOR_INFO)
			||	reportName.equals(SITE_NODE_INFO))
			langReportName = LangModelReport.getString("report.Modules.Map." + reportName);
		
		return langReportName;
	}
	
	@Override
	public Collection<String> getReportElementNames() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public String getName() {
		return DestinationModules.MAP;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(TOPOLOGY_IMAGE);
		result.add(TUNNEL_CABLE_LIST);
		result.add(COLLECTOR_INFO);
		result.add(SITE_NODE_INFO);		
		
		return result;
	}
}
