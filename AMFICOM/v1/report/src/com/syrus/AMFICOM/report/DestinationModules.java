/*
 * $Id: DestinationModules.java,v 1.4 2005/09/07 08:43:27 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.4 $, $Date: 2005/09/07 08:43:27 $
 * @module commonreport_v1
 */
public class DestinationModules {
	//Эти три модуля - разные варианты анализа
	/**
	 * <p>Шаблон по оценке</p>
	 */
	public static final String EVALUATION = "report.Modules.Evaluation.moduleName";
	/**
	 * <p>Шаблон по анализу</p>
	 */
	public static final String ANALYSIS = "report.Modules.Analysis.moduleName";
	/**
	 * <p>Шаблон по исследованию</p>
	 */
	public static final String SURVEY = "report.Modules.Survey.moduleName";

	/**
	 * <p>Шаблон по прогнозу</p>
	 */
	public static final String PREDICTION = "report.Modules.Prediction.moduleName";
	/**
	 * <p>Шаблон по моделированию</p>
	 */
	public static final String MODELING = "report.Modules.Modeling.moduleName";
	/**
	 * <p>Шаблон по оптимизации</p>
	 */
	public static final String OPTIMIZATION = "report.Modules.Optimization.moduleName";
	/**
	 * <p>Шаблон по схеме</p>
	 */
	public static final String SCHEME = "report.Modules.SchemeEditor.moduleName";
	/**
	 * <p>Шаблон по топологии</p>
	 */
	public static final String MAP = "report.Modules.Map.moduleName";
	/**
	 * <p>Шаблон по наблюдению</p>
	 */
	public static final String OBSERVE = "report.Modules.Observation.moduleName";
	/**
	 * <p>Шаблон по планированию</p>
	 */
	public static final String SCHEDULER = "report.Modules.Scheduler.moduleName";

	public static final String UNKNOWN_MODULE = "report.ReportTemplateType.unknownModule";
	
	private static final String SHORT_SUBSTRING = "shortModuleName";
	private static final String STRING_TO_REMOVE = "moduleName";	
	
	public static final String getShortName(String name) {
		int stringToRemovePosition = name.indexOf(STRING_TO_REMOVE);
		if (stringToRemovePosition < 0)
			return null;
		
		return name.substring(0,stringToRemovePosition) + SHORT_SUBSTRING;
	}
}
