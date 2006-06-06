/*-
 * $Id: ReportModels.java,v 1.6 2006/06/06 17:36:28 arseniy Exp $
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
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client_.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.ModelingReportModel;
import com.syrus.AMFICOM.report.ObserverReportModel;
import com.syrus.AMFICOM.report.PredictionReportModel;

/**
 * @author max
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2006/06/06 17:36:28 $
 * @module reportbuilder
 */

public enum ReportModels {
	OBSERVE(new ObserverReportModel()),
	ANALYSIS(new AnalysisReportModel()),
	EVALUATION(new EvaluationReportModel()),
	SURVEY(new SurveyReportModel()),
	MODELING(new ModelingReportModel()),
	PREDICTION(new PredictionReportModel()),
	MAP(new MapReportModel()),
	SCHEME(new SchemeReportModel());
//	OPTIMIZATION(null),
//	SCHEDULER(new SchedulerReportModel());
//	COMBINED(null);
	
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