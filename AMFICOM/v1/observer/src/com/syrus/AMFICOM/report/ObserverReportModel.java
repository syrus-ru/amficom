/*
 * $Id: ObserverReportModel.java,v 1.3 2006/04/11 14:27:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.Client.Analysis.Report.AnalysisReportModel;
import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateModelException;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client_.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.resource.ObserverResourceKeys;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

public class ObserverReportModel extends ReportModel {

	@Override
	public RenderingComponent createReport(AbstractDataStorableElement element, Object data, ApplicationContext aContext)
		throws CreateReportException, CreateModelException {
		RenderingComponent result = null;
		
		String reportName = element.getReportName();
		String modelClassName = element.getModelClassName();
		
		if (reportName.equals(SchemeResourceKeys.FRAME_EDITOR_MAIN)) {
			SchemeReportModel schemeReportModel =
				(SchemeReportModel)ReportModelPool.getModel(
						SchemeReportModel.class.getName());
			result = schemeReportModel.createReport(element,data,aContext);
		} else if (reportName.equals(MapReportModel.TOPOLOGY_IMAGE)) {
			MapReportModel mapReportModel =
				(MapReportModel)ReportModelPool.getModel(
						MapReportModel.class.getName());
			result = mapReportModel.createReport(element,data,aContext);
		} else if (reportName.equals(ObserverResourceKeys.FRAME_ALARM)) {
			AnalysisReportModel aReportModel =
				(AnalysisReportModel)ReportModelPool.getModel(
						AnalysisReportModel.class.getName());
			result = aReportModel.createReport(element,data,aContext);
		} else if (reportName.equals(ObserverResourceKeys.FRAME_RESULT)) {
			AnalysisReportModel aReportModel =
				(AnalysisReportModel)ReportModelPool.getModel(
						AnalysisReportModel.class.getName());
			result = aReportModel.createReport(element,data,aContext);
		}

		if (result == null)
			throw new CreateReportException(
				reportName,
				modelClassName,
				CreateReportException.WRONG_DATA_TO_INSTALL);
		
		return result;
	}

	@Override
	public String getName() {
		return DestinationModules.OBSERVE;
	}

	@Override
	public String getReportElementName(String reportName) {
		return I18N.getString(reportName);
	}

	@Override
	public ReportType getReportKind(String reportName) {
		if (reportName.equals(SchemeResourceKeys.FRAME_EDITOR_MAIN)
			|| reportName.equals(MapReportModel.TOPOLOGY_IMAGE)
			|| reportName.equals(ObserverResourceKeys.FRAME_RESULT)) {
			return ReportType.GRAPH;
		}
		return ReportType.TABLE;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(ObserverResourceKeys.FRAME_ALARM);		
		result.add(MapReportModel.TOPOLOGY_IMAGE);		
		result.add(SchemeResourceKeys.FRAME_EDITOR_MAIN);
		result.add(ObserverResourceKeys.FRAME_RESULT);

		return result;
	}
}
