/*
 * $Id: SchedulerReportModel.java,v 1.1 2005/09/16 13:26:27 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.scheduler.report;

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

public class SchedulerReportModel extends ReportModel {
	// Названия отчётов для карты
	/**
	 * График тестов
	 */ 
	public static String TESTS_GRAPHIC = "testsGraphic";
	/**
	 * Параметры теста
	 */ 
	public static String TEST_PARAMETERS = "testParameters";
	
	public SchedulerReportModel(){
	}

	@Override
	public ReportType getReportKind(String reportName){
		ReportType result = ReportType.TABLE;
		if (reportName.equals(TESTS_GRAPHIC))
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
		
		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		// TODO Вообще-то, эта информация должна храниться в
		// других LangModel'ах и, соответственно, методы должны
		//быть в моделях отчётов - наследницах
		String langReportName = null;
		if (	reportName.equals(TESTS_GRAPHIC)
			||	reportName.equals(TEST_PARAMETERS))
			langReportName = LangModelReport.getString("report.Modules.Scheduler." + reportName);
			
		return langReportName;
	}

	@Override
	public String getName() {
		return DestinationModules.SCHEDULER;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(TESTS_GRAPHIC);
		result.add(TEST_PARAMETERS);
		
		return result;
	}
}
