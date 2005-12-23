/*-
 * $Id: PermissionManager.java,v 1.14 2005/12/23 10:19:35 bob Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.util.Log;

/**
 * Описывает права и обеспечивает их трансляцию в Checker.
 * Позволяет использовать кэширование прав.
 * @author saa
 * @author $Author: bob $
 * @version $Revision: 1.14 $, $Date: 2005/12/23 10:19:35 $
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

	private static boolean cacheable = false;
	private static Map<Operation, Boolean> cache = null;

	public static boolean isPermitted(Operation op) {
		// read cache
		if (cacheable && cache != null && cache.containsKey(op))
			return cache.get(op).booleanValue();
		PermissionCodename code = currentTranslation.get(op);
		if (code == null) {
			// the functionality requested is not defined for this mode
			return false;
		}
		try {
			final long t0 = System.nanoTime();
			final boolean permitted =
				LoginManager.isLoggedIn()
				&& Checker.isPermitted(code);
			final long t1 = System.nanoTime();
			Log.debugMessage("Permission checked for " + op + " in " + (t1 - t0) / 1e6 + " ms", Level.FINEST);
			if (cacheable) {
				if (cache == null)
					cache = new HashMap<Operation, Boolean>();
				cache.put(op, Boolean.valueOf(permitted));
			}
			return permitted;
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			// XXX: if an ApplicationException happens, treat as 'disallow'
			return false;
		}
	}

	/**
	 * Sets Analysis permissions mode
	 */
	public static void setAnalysisTranslation() {
		currentTranslation = analysisTranslation;
		refresh();
	}
	/**
	 * Sets Reseach permissions mode
	 */
	public static void setReseachTranslation() {
		currentTranslation = researchTranslation;
		refresh();
	}
	/**
	 * Sets Evaluation permissions mode
	 */
	public static void setEvaluationTranslation() {
		currentTranslation = evaluationTranslation;
		refresh();
	}
	/**
	 * Sets default permissions mode, nothing allowed
	 */
	public static void setDefaultTranslation() {
		currentTranslation = defaultTranslation;
		refresh();
	}

	/**
	 * Разрешает/запрещает кэширование прав
	 * @param cacheable true для включения кэша прав
	 */
	public static void setCacheable(boolean cacheable) {
		PermissionManager.cacheable = cacheable;
		if (!cacheable)
			refresh();
	}

	/**
	 * Обновить права.
	 * При выключенном кэшировании ничего не делает.
	 * При включенном кэшировании очищает кэш и загружает все права.
	 */
	public static void refresh() {
		resetCache();
		// preload
		if (cacheable) {
			for (Operation op: currentTranslation.keySet()) {
				isPermitted(op);
			}
		}
	}

	/**
	 * Сбрасывает кэшированные права
	 */
	private static void resetCache() {
		cache = null;
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
		researchTranslation.put(Operation.LOAD_ETALON, PermissionCodename.RESEARCH_OPEN_REFLECTOGRAM); // в эталоне будет показываться только р/г
		// @todo add support for SAVE_SCHEME_BINDING

		evaluationTranslation = new HashMap<Operation, PermissionCodename>();
		evaluationTranslation.put(Operation.READ_TRACE_FILE, PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM_FILE);
		evaluationTranslation.put(Operation.SAVE_TRACE_FILE, PermissionCodename.EVALUATION_SAVE_REFLECTOGRAM_FILE);
		evaluationTranslation.put(Operation.LOAD_TRACE, PermissionCodename.EVALUATION_OPEN_REFLECTOGRAM);
		evaluationTranslation.put(Operation.LOAD_ETALON, PermissionCodename.EVALUATION_OPEN_ETALON_REFLECTOGRAM);
		evaluationTranslation.put(Operation.EDIT_ETALON, PermissionCodename.EVALUATION_EDIT_ETALON);
		
		// TODO rename Operation.SAVE_MEASUREMENT_SETUP to Operation.SAVE_ETALON 
		evaluationTranslation.put(Operation.SAVE_MEASUREMENT_SETUP, PermissionCodename.EVALUATION_SAVE_ETALON);

		defaultTranslation = new HashMap<Operation, PermissionCodename>();

		currentTranslation = defaultTranslation;
	}

	private PermissionManager() {
		// non instantiable
	}
}
