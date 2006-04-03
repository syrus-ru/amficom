/*-
 * $Id: ReportModels.java,v 1.2 2006/04/03 09:21:19 stas Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import com.syrus.AMFICOM.Client.Analysis.Report.AnalysisReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.EvaluationReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.SurveyReportModel;
import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.report.ModelingReportModel;
import com.syrus.AMFICOM.report.ObserverReportModel;
import com.syrus.AMFICOM.report.PredictionReportModel;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.report.SchedulerReportModel;
import com.syrus.AMFICOM.client_.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.report.DestinationModules;

/**
 * @author max
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2006/04/03 09:21:19 $
 * @module reportbuilder
 */

public enum ReportModels {
	ANALYSIS(new AnalysisReportModel()),
	EVALUATION(new EvaluationReportModel()),
	MAP(new MapReportModel()),
	MODELING(new ModelingReportModel()),
	OBSERVE(new ObserverReportModel()),
	OPTIMIZATION(null),
	PREDICTION(new PredictionReportModel()),
	SCHEDULER(new SchedulerReportModel()),
	SCHEME(new SchemeReportModel()),
	SURVEY(new SurveyReportModel()),
	COMBINED(null);
	
	ReportModel reportModel;
	
	ReportModels(ReportModel reportModel) {
		this.reportModel = reportModel;
	}
	
	public ReportModel getReportModel() {
		return this.reportModel;
	}
	
	public String getName() {
		if (this.reportModel == null) {
			return DestinationModules.COMBINED;
		}
		return this.reportModel.getName();
	}
	
	public static int getIndex(String moduleName) {
		ReportModels[] values = ReportModels.values();
		for (int i = 0; i < values.length; i++) {
			if(moduleName.equals(values[i].getName())) {
				return i;
			}
		}
		return -1;
	}
}