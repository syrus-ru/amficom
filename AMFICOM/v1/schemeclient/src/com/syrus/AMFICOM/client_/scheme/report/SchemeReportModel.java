/*
 * $Id: SchemeReportModel.java,v 1.1 2005/10/12 10:10:51 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client_.scheme.report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeCableLinkPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeCablePortPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeElementPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeLinkPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePathPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePortPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePropertiesManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.scheme.AbstractSchemeLink;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

public class SchemeReportModel extends ReportModel
{
	//�������� ������ ��� ���� ������� (3 ������� + ������� � �������������)
	/**
	 * ������������ �� ������ �����
	 */
	public static String ON_SCREEN_SCHEME_CELL_CONTAINER = "onScreenScheme";
	/**
	 * �������� ����������� �����������
	 */
	public static String SELECTED_OBJECT_UGO = "selectedObjectUGO";
	/**
	 * �������������� �������
	 */
	public static String SELECTED_OBJECT_CHARS = "selectedObjectChars";
	public SchemeReportModel(){
	}

	@Override
	public ReportType getReportKind(String reportName){
		ReportType result = ReportType.TABLE;
		if (	reportName.equals(ON_SCREEN_SCHEME_CELL_CONTAINER)
			||	reportName.equals(SELECTED_OBJECT_UGO))
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
		
		if (!(data instanceof Identifier))
			throw new CreateReportException(
					reportName,
					modelClassName,
					CreateReportException.WRONG_DATA_TO_INSTALL);
		
		Identifier objectId = (Identifier)data;
		
		try {
			if (reportName.equals(ON_SCREEN_SCHEME_CELL_CONTAINER)) {
				if (objectId.getMajor() == ObjectEntities.SCHEME_CODE) {
					Scheme scheme = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemeReport.createReport(scheme,element,aContext);
				}
			}
			else if (element.getReportName().equals(SELECTED_OBJECT_UGO)) {
				VisualManager visualManager = null;
				Object dataObject = null;
				if (objectId.getMajor() == ObjectEntities.SCHEME_CODE) {
					Scheme scheme = StorableObjectPool.getStorableObject(objectId,true);
					visualManager = SchemePropertiesManager.getInstance(aContext);
					dataObject = scheme;
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
					SchemeElement schemeElement = StorableObjectPool.getStorableObject(objectId,true);
					visualManager = SchemeElementPropertiesManager.getInstance(aContext);
					dataObject = schemeElement;					
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMEPORT_CODE) {
					AbstractSchemePort schemePort = StorableObjectPool.getStorableObject(objectId,true);
					visualManager = SchemePortPropertiesManager.getInstance(aContext);
					dataObject = schemePort;
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
					AbstractSchemePort schemePort = StorableObjectPool.getStorableObject(objectId,true);
					visualManager = SchemeCablePortPropertiesManager.getInstance(aContext);
					dataObject = schemePort;
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMELINK_CODE) {
					AbstractSchemeLink schemeLink = StorableObjectPool.getStorableObject(objectId,true);
					visualManager = SchemeLinkPropertiesManager.getInstance(aContext);
					dataObject = schemeLink;
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMECABLELINK_CODE) {
					AbstractSchemeLink schemeLink = StorableObjectPool.getStorableObject(objectId,true);
					visualManager = SchemeCableLinkPropertiesManager.getInstance(aContext);
					dataObject = schemeLink;
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMEPATH_CODE) {
					SchemePath schemePath = StorableObjectPool.getStorableObject(objectId,true);
					visualManager = SchemePathPropertiesManager.getInstance(aContext);
					dataObject = schemePath;
				}
				if (visualManager != null)
					result = UGOReport.createReport(
							element,
							visualManager,
							dataObject);
			}
			else if (reportName.equals(SELECTED_OBJECT_CHARS)) {
				if (objectId.getMajor() == ObjectEntities.SCHEME_CODE) {
					Scheme scheme = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemeReport.createReport(scheme,element,aContext);
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
					SchemeElement schemeElement = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemeElementReport.createReport(
							schemeElement,
							(TableDataStorableElement)element);
				}
				else if (	(objectId.getMajor() == ObjectEntities.SCHEMEPORT_CODE)
						||	(objectId.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE)) {
					AbstractSchemePort schemePort = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemePortReport.createReport(
							schemePort,
							(TableDataStorableElement)element);
				}
				else if (	(objectId.getMajor() == ObjectEntities.SCHEMELINK_CODE)
						||	(objectId.getMajor() == ObjectEntities.SCHEMECABLELINK_CODE)) {
					AbstractSchemeLink schemeLink = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemeLinkReport.createReport(
							schemeLink,
							(TableDataStorableElement)element);
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMEPATH_CODE) {
					SchemePath schemePath = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemePathReport.createReport(
							schemePath,
							(TableDataStorableElement)element);
				}
			}
		} catch (ApplicationException e) {
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
		// TODO ������-��, ��� ���������� ������ ��������� �
		// ������ LangModel'�� �, ��������������, ������ ������
		//���� � ������� ������� - �����������
		String langReportName = null;
		if (	reportName.equals(ON_SCREEN_SCHEME_CELL_CONTAINER)
			||	reportName.equals(SELECTED_OBJECT_UGO)
			||	reportName.equals(SELECTED_OBJECT_CHARS))
			langReportName = LangModelReport.getString("report.Modules.SchemeEditor." + reportName);
		
		return langReportName;
	}

	@Override
	public String getName() {
		return DestinationModules.SCHEME;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(ON_SCREEN_SCHEME_CELL_CONTAINER);
		result.add(SELECTED_OBJECT_UGO);
		result.add(SELECTED_OBJECT_CHARS);
		
		return result;
	}
}