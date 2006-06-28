/*
 * $Id: SchemeReportModel.java,v 1.8 2006/06/06 12:58:03 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client_.scheme.report;

import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_ADDITIONAL_PROPERIES;
import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_CHARACTERISTICS;
import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_EDITOR_MAIN;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeCableLinkPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeCablePortPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeElementPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeLinkPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePathPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePortPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePropertiesManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.scheme.AbstractSchemeLink;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

public class SchemeReportModel extends ReportModel {
	@Override
	public ReportType getReportKind(String reportName) {
		if (reportName.equals(FRAME_EDITOR_MAIN)
			|| reportName.equals(FRAME_ADDITIONAL_PROPERIES)) {
			return ReportType.GRAPH;
		}
		return ReportType.TABLE;
	}
	
	@Override
	public RenderingComponent createReport(
			AbstractDataStorableElement element,
			Object data,
			ApplicationContext aContext) throws CreateReportException{
		RenderingComponent result = null;
		
		String reportName = element.getReportName();
		String modelClassName = element.getModelClassName();
		
		if (!(data instanceof Identifiable))
			throw new CreateReportException(
					reportName,
					modelClassName,
					CreateReportException.WRONG_DATA_TO_INSTALL);
		
		Identifier objectId = ((Identifiable)data).getId();
		
		try {
			if (reportName.equals(FRAME_EDITOR_MAIN)) {
				if (objectId.getMajor() == ObjectEntities.SCHEME_CODE) {
					Scheme scheme = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemeReport.createReport(scheme,element,aContext);
				}
			}
			else if (element.getReportName().equals(FRAME_ADDITIONAL_PROPERIES)) {
				VisualManager visualManager = null;
				StorableObject dataObject = null;
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
			else if (reportName.equals(FRAME_CHARACTERISTICS)) {
				if (objectId.getMajor() == ObjectEntities.SCHEME_CODE) {
					Scheme scheme = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemeReport.createReport(scheme,element,aContext);
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
					SchemeElement schemeElement = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemeElementReport.createReport(
							schemeElement,
							(TableDataStorableElement) element);
				}
				else if (	(objectId.getMajor() == ObjectEntities.SCHEMEPORT_CODE)
						||	(objectId.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE)) {
					AbstractSchemePort schemePort = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemePortReport.createReport(
							schemePort,
							(TableDataStorableElement) element);
				}
				else if (	(objectId.getMajor() == ObjectEntities.SCHEMELINK_CODE)
						||	(objectId.getMajor() == ObjectEntities.SCHEMECABLELINK_CODE)) {
					AbstractSchemeLink schemeLink = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemeLinkReport.createReport(
							schemeLink,
							(TableDataStorableElement) element);
				}
				else if (objectId.getMajor() == ObjectEntities.SCHEMEPATH_CODE) {
					SchemePath schemePath = StorableObjectPool.getStorableObject(objectId,true);
					result = SchemePathReport.createReport(
							schemePath,
							(TableDataStorableElement) element);
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
		return I18N.getString(reportName);
	}

	@Override
	public String getName() {
		return DestinationModules.SCHEME;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(FRAME_EDITOR_MAIN);
		result.add(FRAME_ADDITIONAL_PROPERIES);
		result.add(FRAME_CHARACTERISTICS);
		
		return result;
	}
}
