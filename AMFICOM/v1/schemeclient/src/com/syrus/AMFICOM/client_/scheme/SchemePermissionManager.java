/*-
 * $Id: SchemePermissionManager.java,v 1.3 2006/05/03 04:51:02 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.SCHEME_ENTER;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.SCHEME_CREATE_AND_EDIT;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.SCHEME_SAVING;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.SCHEME_DELETE;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.util.Application;
import com.syrus.util.Log;

public class SchemePermissionManager {
	
	public static boolean isEntranceAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(SCHEME_ENTER);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isCreationAllowed() {
		try {
			return Application.getApplicationName().equals(SchemeEditor.APPLICATION_NAME) 
					&& LoginManager.isLoggedIn() && Checker.isPermitted(SCHEME_CREATE_AND_EDIT);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isEditionAllowed() {
		try {
			return Application.getApplicationName().equals(SchemeEditor.APPLICATION_NAME)
					&& LoginManager.isLoggedIn() && Checker.isPermitted(SCHEME_CREATE_AND_EDIT);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
		
	public static boolean isDeletionAllowed() {
		try {
			return Application.getApplicationName().equals(SchemeEditor.APPLICATION_NAME)
					&& LoginManager.isLoggedIn() && Checker.isPermitted(SCHEME_DELETE);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isSavingAllowed() {
		try {
			return Application.getApplicationName().equals(SchemeEditor.APPLICATION_NAME)
					&& LoginManager.isLoggedIn() && Checker.isPermitted(SCHEME_SAVING);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isTypeEditionAllowed() {
		try {
			return (Application.getApplicationName().equals(SchemeEditor.APPLICATION_NAME)
					|| Application.getApplicationName().equals(ElementsEditor.APPLICATION_NAME))
					&& LoginManager.isLoggedIn() && Checker.isPermitted(ELEMENTS_EDITOR_CREATE_CHANGE_SAVE_TYPE);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
}
