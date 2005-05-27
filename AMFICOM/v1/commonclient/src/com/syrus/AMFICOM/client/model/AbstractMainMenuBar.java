/*-
 * $Id: AbstractMainMenuBar.java,v 1.1 2005/05/27 16:16:11 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import javax.swing.JMenuBar;


/**
 * @version $Revision: 1.1 $, $Date: 2005/05/27 16:16:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class AbstractMainMenuBar extends JMenuBar {

	public static final String			MENU_SESSION					= "menuSession";
	public static final String			MENU_SESSION_NEW				= "menuSessionNew";
	public static final String			MENU_SESSION_CLOSE				= "menuSessionClose";
	public static final String			MENU_SESSION_OPTIONS			= "menuSessionOptions";
	public static final String			MENU_SESSION_CONNECTION			= "menuSessionConnection";
	public static final String			MENU_SESSION_CHANGE_PASSWORD	= "menuSessionChangePassword";
	public static final String			MENU_SESSION_DOMAIN				= "menuSessionDomain";

	public static final String			MENU_VIEW_ARRANGE				= "menuViewArrange";

	public static final String			MENU_EXIT						= "menuExit";
	
	public static final String	MENU_HELP					= "menuHelp";
	public static final String	MENU_HELP_ABOUT				= "menuHelpAbout";

	protected ApplicationModelListener	applicationModelListener;
	protected ApplicationModel			aModel;

	public AbstractMainMenuBar(ApplicationModel aModel) {
		this.aModel = aModel;
	}

	public void setModel(ApplicationModel aModel) {
		this.aModel = aModel;
	}

	/**
	 * @return Returns the applicationModelListener.
	 */
	public ApplicationModelListener getApplicationModelListener() {
		return this.applicationModelListener;
	}
}
