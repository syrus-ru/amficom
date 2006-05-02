/*-
 * $Id: ModelingPermissionManager.java,v 1.1 2006/05/02 09:15:34 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Model;

import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MODELING_ENTER;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MODELING_OPEN_MAP;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MODELING_OPEN_SCHEME;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MODELING_SAVE_REFLECTOGRAM_MODEL;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.util.Log;

public class ModelingPermissionManager {
	public static boolean isEntranceAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(MODELING_ENTER);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isOpenSchemeAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(MODELING_OPEN_SCHEME);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isOpenMapAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(MODELING_OPEN_MAP);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isSavingAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(MODELING_SAVE_REFLECTOGRAM_MODEL);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}

}
