/*-
 * $Id: MapPermissionManager.java,v 1.1 2006/05/03 04:47:33 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.editor;

import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MAP_EDITOR_EDIT_BINDING;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MAP_EDITOR_ENTER;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MAP_EDITOR_DELETE;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MAP_EDITOR_SAVE_BINDING;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MAP_EDITOR_SAVE_TOPOLOGICAL_SCHEME;
import static com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename.MAP_EDITOR_SAVE_TOPOLOGICAL_VIEW;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.util.Application;
import com.syrus.util.Log;

public class MapPermissionManager {
	public static boolean isEntranceAllowed() {
		try {
			return LoginManager.isLoggedIn() && Checker.isPermitted(MAP_EDITOR_ENTER);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}

	public static boolean isEditionAllowed() {
		try {
			return Application.getApplicationName().equals(MapEditor.APPLICATION_NAME)
					&& LoginManager.isLoggedIn() && Checker.isPermitted(MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isSavingAllowed() {
		try {
			return Application.getApplicationName().equals(MapEditor.APPLICATION_NAME)
					&& LoginManager.isLoggedIn() && Checker.isPermitted(MAP_EDITOR_SAVE_TOPOLOGICAL_SCHEME);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isDeletingAllowed() {
		try {
			return Application.getApplicationName().equals(MapEditor.APPLICATION_NAME)
					&& LoginManager.isLoggedIn() && Checker.isPermitted(MAP_EDITOR_DELETE);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isViewSavingAllowed() {
		try {
			return Application.getApplicationName().equals(MapEditor.APPLICATION_NAME)
					&& LoginManager.isLoggedIn() && Checker.isPermitted(MAP_EDITOR_SAVE_TOPOLOGICAL_VIEW);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isSchemeBoundEditionAllowed() {
		try {
			return Application.getApplicationName().equals(MapEditor.APPLICATION_NAME)
					&& LoginManager.isLoggedIn() && Checker.isPermitted(MAP_EDITOR_EDIT_BINDING);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	public static boolean isSchemeBoundSavingAllowed() {
		try {
			return Application.getApplicationName().equals(MapEditor.APPLICATION_NAME)
					&& LoginManager.isLoggedIn() && Checker.isPermitted(MAP_EDITOR_SAVE_BINDING);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return false;
		}
	}
	
	
}
