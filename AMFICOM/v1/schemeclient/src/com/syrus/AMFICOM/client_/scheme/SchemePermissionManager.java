/*-
 * $Id: SchemePermissionManager.java,v 1.4 2006/06/06 12:43:26 stas Exp $
 *
 * Copyright ї 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.ELEMENTS_EDITOR_CREATE_AND_EDIT;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.ELEMENTS_EDITOR_ENTER;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.SCHEME_CREATE_AND_EDIT;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.SCHEME_DELETE;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.SCHEME_ENTER;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.SCHEME_SAVING;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.util.Application;
import com.syrus.util.Log;

public class SchemePermissionManager {
	public static enum Operation {
		ENTER,
		CREATE,
		EDIT,
		DELETE,
		SAVE,
		EDIT_TYPE
	}

	private static Map<Operation, PermissionCodename> schemeTranslation;
	private static Map<Operation, PermissionCodename> elementsTranslation;
	private static Map<Operation, PermissionCodename> currentTranslation;
	private static boolean isModuleSuitable = false;
	
	private static boolean cacheable = true;
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
				isModuleSuitable
				&& LoginManager.isLoggedIn()
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
			return false;
		}
	}
	
	public static void setSchemeTranslation() {
		isModuleSuitable = Application.getApplicationName().equals(SchemeEditor.APPLICATION_NAME);
		currentTranslation = schemeTranslation;
		refresh();
	}
	
	public static void setElementsTranslation() {
		isModuleSuitable = Application.getApplicationName().equals(ElementsEditor.APPLICATION_NAME);
		currentTranslation = elementsTranslation;
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
		schemeTranslation = new HashMap<Operation, PermissionCodename>();
		schemeTranslation.put(Operation.ENTER, SCHEME_ENTER);
		schemeTranslation.put(Operation.CREATE, SCHEME_CREATE_AND_EDIT);
		schemeTranslation.put(Operation.EDIT, SCHEME_CREATE_AND_EDIT);
		schemeTranslation.put(Operation.SAVE, SCHEME_SAVING);
		schemeTranslation.put(Operation.DELETE, SCHEME_DELETE);
		schemeTranslation.put(Operation.EDIT_TYPE, ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE);
		
		elementsTranslation = new HashMap<Operation, PermissionCodename>();
		elementsTranslation.put(Operation.ENTER, ELEMENTS_EDITOR_ENTER);
		elementsTranslation.put(Operation.CREATE, ELEMENTS_EDITOR_CREATE_AND_EDIT);
		elementsTranslation.put(Operation.EDIT, ELEMENTS_EDITOR_CREATE_AND_EDIT);
		elementsTranslation.put(Operation.SAVE, ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE);
		elementsTranslation.put(Operation.EDIT_TYPE, ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE);
	}
}
