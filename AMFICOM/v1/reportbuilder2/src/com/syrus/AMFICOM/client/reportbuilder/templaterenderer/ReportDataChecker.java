/*
 * $Id: ReportDataChecker.java,v 1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.Client.Analysis.Report.AESMPReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.AnalysisReportModel;
import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.scheduler.report.SchedulerReportModel;
import com.syrus.AMFICOM.client_.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Test;
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
				||	(objectToInstall instanceof Collector)
				||	(objectToInstall instanceof Test)
				||	(objectToInstall instanceof Measurement)) {
			return true;
		}
		return false;
	}
	
	public static Map<String,String> getObjectReportAttributes(
			Object objectToInstall) {
		Map<String,String> attributes = null;
		//Для сиюминутных отчётов по схеме		
		if (objectToInstall instanceof Scheme) {
			attributes = new HashMap<String,String>();
			attributes.put(
					MODEL_CLASS_NAME,
					SchemeReportModel.class.getName());
			attributes.put(
					REPORT_NAME,
					SchemeReportModel.ON_SCREEN_SCHEME_CELL_CONTAINER);
		}
		else if (	(objectToInstall instanceof SchemeElement)
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
		//Для сиюминутных отчётов по измерениям
		else if (	(objectToInstall instanceof Test)) {
			attributes = new HashMap<String,String>();			
			attributes.put(
					MODEL_CLASS_NAME,
					SchedulerReportModel.class.getName());
			attributes.put(
					REPORT_NAME,
					SchedulerReportModel.TEST_PARAMETERS);
		}
		//Для сиюминутных отчётов по анализу
		else if (	(objectToInstall instanceof Measurement)) {
			attributes = new HashMap<String,String>();			
			attributes.put(
					MODEL_CLASS_NAME,
					AnalysisReportModel.class.getName());
			attributes.put(
					REPORT_NAME,
					AESMPReportModel.REFLECTOGRAMM);
		}
		
		return attributes;
	}
}
