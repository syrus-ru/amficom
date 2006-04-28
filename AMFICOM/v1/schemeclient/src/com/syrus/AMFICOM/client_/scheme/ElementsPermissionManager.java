/*-
 * $Id: ElementsPermissionManager.java,v 1.1 2006/04/28 09:55:52 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.ELEMENTS_EDITOR_ENTER;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.ELEMENTS_EDITOR_CREATE_AND_EDIT;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.ELEMENTS_EDITOR_SAVING;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.util.Log;

public class ElementsPermissionManager {
	
	public static boolean isEntranceAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(ELEMENTS_EDITOR_ENTER);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isCreationAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(ELEMENTS_EDITOR_CREATE_AND_EDIT);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isEditionAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(ELEMENTS_EDITOR_CREATE_AND_EDIT);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
		
	public static boolean isTypeEditionAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isSavingAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(ELEMENTS_EDITOR_SAVING);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
}

