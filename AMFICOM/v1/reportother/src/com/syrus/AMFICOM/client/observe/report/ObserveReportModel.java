/*
 * $Id: ObserveReportModel.java,v 1.1 2005/09/13 13:44:19 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.observe.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;

public class ObserveReportModel extends ReportModel {
	/**
	 * Информация о маркере (кабель, тоннель, место в тоннеле,
	 * расстояние до узлов, географические координаты)
	 */ 
	public static String MARKER_INFO = "markerInfo";

	@Override
	public RenderingComponent createReport(DataStorableElement element, Object data, ApplicationContext aContext) throws CreateReportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return DestinationModules.OBSERVE;
	}

	@Override
	public String getReportElementName(String reportName) {
		// TODO Вообще-то, эта информация должна храниться в
		// других LangModel'ах и, соответственно, методы должны
		//быть в моделях отчётов - наследницах
		String langReportName = null;
		if (reportName.equals(MARKER_INFO))
			langReportName = LangModelReport.getString("report.Modules.Observation." + reportName);
		
		return langReportName;
	}

	@Override
	public ReportType getReportKind(String reportName) {
		ReportType result = ReportType.TABLE;
		return result;
	}

	@Override
	public Collection<String> getReportElementNames() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();
		
		result.add(MARKER_INFO);
		
		return result;
	}

}
