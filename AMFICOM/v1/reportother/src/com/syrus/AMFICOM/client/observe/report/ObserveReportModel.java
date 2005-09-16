/*
 * $Id: ObserveReportModel.java,v 1.2 2005/09/16 13:26:27 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.observe.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateModelException;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.client.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;

public class ObserveReportModel extends ReportModel {
	/**
	 * ���������� � ������� (������, �������, ����� � �������,
	 * ���������� �� �����, �������������� ����������)
	 */ 
	public static String MARKER_INFO = "markerInfo";
	/**
	 * ������ �������� �������
	 */ 
	public static String ALARMS_LIST = "alarmsList";

	@Override
	public RenderingComponent createReport(DataStorableElement element, Object data, ApplicationContext aContext)
		throws CreateReportException, CreateModelException {
		RenderingComponent result = null;
		
		String reportName = element.getReportName();
		String modelClassName = element.getModelClassName();
		
		if (reportName.equals(SchemeReportModel.ON_SCREEN_SCHEME)) {
			SchemeReportModel schemeReportModel =
				(SchemeReportModel)ReportModelPool.getModel(
						SchemeReportModel.class.getName());
			result = schemeReportModel.createReport(element,data,aContext);
		}
		else if (reportName.equals(MapReportModel.TOPOLOGY_IMAGE)) {
			MapReportModel mapReportModel =
				(MapReportModel)ReportModelPool.getModel(
						MapReportModel.class.getName());
			result = mapReportModel.createReport(element,data,aContext);
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
		// TODO ������-��, ��� ���������� ������ ��������� �
		// ������ LangModel'�� �, ��������������, ������ ������
		//���� � ������� ������� - �����������
		String langReportName = null;
		if (	reportName.equals(MARKER_INFO)
			||	reportName.equals(ALARMS_LIST))
			langReportName = LangModelReport.getString("report.Modules.Observation." + reportName);
		else if (reportName.equals(SchemeReportModel.ON_SCREEN_SCHEME))
			langReportName = LangModelReport.getString("report.Modules.SchemeEditor." + reportName);
		else if (reportName.equals(MapReportModel.TOPOLOGY_IMAGE))
			langReportName = LangModelReport.getString("report.Modules.Map." + reportName);
		
		
		return langReportName;
	}

	@Override
	public ReportType getReportKind(String reportName) {
		ReportType result = ReportType.TABLE;
		if (	reportName.equals(SchemeReportModel.ON_SCREEN_SCHEME)
			||	reportName.equals(MapReportModel.TOPOLOGY_IMAGE))
			result = ReportType.GRAPH;
		
		return result;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();
		
		result.add(MARKER_INFO);
		result.add(ALARMS_LIST);		
		result.add(MapReportModel.TOPOLOGY_IMAGE);		
		result.add(SchemeReportModel.ON_SCREEN_SCHEME);
		
		return result;
	}

}
