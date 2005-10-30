/*-
 * $Id: PermissionManager.java,v 1.6 2005/10/30 15:20:48 bass Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.util.Log;

/**
 * @author saa
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/10/30 15:20:48 $
 * @module analysis
 */
public class PermissionManager {
	public static enum Operation {
		READ_TRACE_FILE,
		SAVE_TRACE_FILE,
		LOAD_TRACE,
		LOAD_ETALON,
		EDIT_ETALON,
		SAVE_MEASUREMENT_SETUP;
	}

	private static Map<Operation, PermissionCodename> analysisTranslation;
	private static Map<Operation, PermissionCodename> researchTranslation;
	private static Map<Operation, PermissionCodename> evaluationTranslation;
	private static Map<Operation, PermissionCodename> defaultTranslation;
	private static Map<Operation, PermissionCodename> currentTranslation;

	public static boolean isPermitted(Operation op) {
		PermissionCodename code = currentTranslation.get(op);
		if (code == null) {
			// the functionality requested is not defined for this mode
			return false;
		}
		try {
			return Checker.isPermitted(code);
		} catch (ApplicationException e) {
			assert Log.errorMessage(e);
			// XXX: if an ApplicationException happens, treat as 'disallow'
			return false;
		}
	}

	/**
	 * Sets Analysis permissions mode
	 */
	public static void setAnalysisTranslation() {
		currentTranslation = analysisTranslation;
	}
	/**
	 * Sets Reseach permissions mode
	 */
	public static void setReseachTranslation() {
		currentTranslation = researchTranslation;
	}
	/**
	 * Sets Evaluation permissions mode
	 */
	public static void setEvaluationTranslation() {
		currentTranslation = evaluationTranslation;
	}
	/**
	 * Sets default permissions mode, nothing allowed
	 */
	public static void setDefaultTranslation() {
		currentTranslation = defaultTranslation;
	}

	static {
		analysisTranslation = new HashMap<Operation, PermissionCodename>();
		analysisTranslation.put(Operation.READ_TRACE_FILE, PermissionCodename.ANALYSIS_OPEN_REFLECTOGRAM_FILE);
		analysisTranslation.put(Operation.LOAD_TRACE, PermissionCodename.ANALYSIS_OPEN_REFLECTOGRAM);

		researchTranslation = new HashMap<Operation, PermissionCodename>();
		researchTranslation.put(Operation.READ_TRACE_FILE, PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM_FILE);
		researchTranslation.put(Operation.SAVE_TRACE_FILE, PermissionCodename.RESEARCH_SAVE_REFLECTOGRAM_FILE);
		researchTranslation.put(Operation.LOAD_TRACE, PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM);
		researchTranslation.put(Operation.SAVE_MEASUREMENT_SETUP, PermissionCodename.RESEARCH_SAVE_MEASUREMENT_SETUP);
		// @todo add support for SAVE_SCHEME_BINDING

		evaluationTranslation = new HashMap<Operation, PermissionCodename>();
		evaluationTranslation.put(Operation.READ_TRACE_FILE, PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM_FILE);
		evaluationTranslation.put(Operation.SAVE_TRACE_FILE, PermissionCodename.EVALUATION_SAVE_REFLECTOGRAM_FILE);
		evaluationTranslation.put(Operation.LOAD_TRACE, PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM);
		evaluationTranslation.put(Operation.LOAD_ETALON, PermissionCodename.EVALUATION_OPEN_ETALON_REFLECTOGRAM);
		evaluationTranslation.put(Operation.EDIT_ETALON, PermissionCodename.EVALUATION_EDIT_ETALON);
		evaluationTranslation.put(Operation.SAVE_MEASUREMENT_SETUP, PermissionCodename.EVALUATION_SAVE_MEASUREMENT_SETUP);

		defaultTranslation = new HashMap<Operation, PermissionCodename>();

		currentTranslation = defaultTranslation;
	}

	private PermissionManager() {
		// non instantiable
	}
}
