/*
 * $Id: ReportDataChecker.java,v 1.1 2005/09/20 09:25:54 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.AbstractSchemeLink;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

public class ReportDataChecker {
	public static final String REPORT_NAME = "reportName";
	public static final String MODEL_CLASS_NAME = "modelClassName";
	
	public static boolean isObjectInstallable(Object objectToInstall) {
		if (	(objectToInstall instanceof Scheme)
				||	(objectToInstall instanceof SchemeElement)
				||	(objectToInstall instanceof AbstractSchemePort)
				||	(objectToInstall instanceof AbstractSchemeLink)
				||	(objectToInstall instanceof SchemePath)
				||	(objectToInstall instanceof PhysicalLink)
				||	(objectToInstall instanceof SiteNode)
				||	(objectToInstall instanceof Collector)) {
			return true;
		}
		return true;
	}
	public static Map<String,String> getObjectReportAttributes(
			Object objectToInstall) {
		Map<String,String> attributes = null;
		if (	(objectToInstall instanceof Scheme)
				||	(objectToInstall instanceof SchemeElement)
				||	(objectToInstall instanceof AbstractSchemePort)
				||	(objectToInstall instanceof AbstractSchemeLink)
				||	(objectToInstall instanceof SchemePath)) {
			attributes = new HashMap<String,String>();
			attributes.put(
					MODEL_CLASS_NAME,
					SchemeReportModel.class.getName());
			attributes.put(
					REPORT_NAME,
					SchemeReportModel.SELECTED_OBJECT_CHARS);
		}
		//Для сиюминутных отчётов по карте
		else if (	(objectToInstall instanceof PhysicalLink)
				||	(objectToInstall instanceof SiteNode)
				||	(objectToInstall instanceof Collector)) {
			attributes = new HashMap<String,String>();			
			attributes.put(
					MODEL_CLASS_NAME,
					MapReportModel.class.getName());
			attributes.put(
					REPORT_NAME,
					MapReportModel.SELECTED_OBJECT_CHARS);
		}

		return attributes;
	}
}
