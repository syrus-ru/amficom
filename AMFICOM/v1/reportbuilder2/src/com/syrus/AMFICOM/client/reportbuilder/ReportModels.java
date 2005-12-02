/*-
 * $Id: ReportModels.java,v 1.1 2005/12/02 11:37:17 bass Exp $
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
import com.syrus.AMFICOM.client.modelling.report.ModelingReportModel;
import com.syrus.AMFICOM.client.observe.report.ObserveReportModel;
import com.syrus.AMFICOM.client.prediction.report.PredictionReportModel;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.scheduler.report.SchedulerReportModel;
import com.syrus.AMFICOM.client_.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.report.DestinationModules;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/12/02 11:37:17 $
 * @module reportbuilder
 */

public enum ReportModels {
	ANALYSIS(new AnalysisReportModel()),
	EVALUATION(new EvaluationReportModel()),
	MAP(new MapReportModel()),
	MODELING(new ModelingReportModel()),
	OBSERVE(new ObserveReportModel()),
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