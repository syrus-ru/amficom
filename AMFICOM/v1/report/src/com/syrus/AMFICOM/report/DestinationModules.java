/*
 * $Id: DestinationModules.java,v 1.6 2005/10/08 13:16:31 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/10/08 13:16:31 $
 * @module report
 */
public class DestinationModules {
	//��� ��� ������ - ������ �������� �������
	/**
	 * <p>������ �� ������</p>
	 */
	public static final String EVALUATION = "report.Modules.Evaluation.moduleName";
	/**
	 * <p>������ �� �������</p>
	 */
	public static final String ANALYSIS = "report.Modules.Analysis.moduleName";
	/**
	 * <p>������ �� ������������</p>
	 */
	public static final String SURVEY = "report.Modules.Survey.moduleName";

	/**
	 * <p>������ �� ��������</p>
	 */
	public static final String PREDICTION = "report.Modules.Prediction.moduleName";
	/**
	 * <p>������ �� �������������</p>
	 */
	public static final String MODELING = "report.Modules.Modeling.moduleName";
	/**
	 * <p>������ �� �����������</p>
	 */
	public static final String OPTIMIZATION = "report.Modules.Optimization.moduleName";
	/**
	 * <p>������ �� �����</p>
	 */
	public static final String SCHEME = "report.Modules.SchemeEditor.moduleName";
	/**
	 * <p>������ �� ���������</p>
	 */
	public static final String MAP = "report.Modules.Map.moduleName";
	/**
	 * <p>������ �� ����������</p>
	 */
	public static final String OBSERVE = "report.Modules.Observation.moduleName";
	/**
	 * <p>������ �� ������������</p>
	 */
	public static final String SCHEDULER = "report.Modules.Scheduler.moduleName";
	/**
	 * <p>��������������� ������</p>
	 */
	public static final String COMBINED = "report.Modules.combinedTemplate";
	
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
