/*-
 * $Id: Environment.java,v 1.19 2005/11/24 07:53:06 bob Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import javax.swing.JFrame;

import com.syrus.AMFICOM.client.event.Dispatcher;

/**
 * Класс Environment используется для хранения общей для приложения информации.
 * Класс инвиромент - остатог дирьма андрюсега
 * 
 * @author $Author: bob $
 * @version $Revision: 1.19 $, $Date: 2005/11/24 07:53:06 $
 * @module commonclient
 */
public final class Environment {
	private static Dispatcher theDispatcher = new Dispatcher();

	private static JFrame activeWindow = null;

	private Environment() {
		assert false;
	}

	/**
	 * @deprecated use {@link AbstractMainFrame#getGlobalDispatcher()} 
	 */
	@Deprecated
	public static Dispatcher getDispatcher() {
		return theDispatcher;
	}

	/**
	 * @deprecated use {@link AbstractMainFrame#setActiveMainFrame(AbstractMainFrame)} 
	 */
	@Deprecated
	public static void setActiveWindow(JFrame window) {
		activeWindow = window;
	}

	/**
	 * @deprecated use {@link AbstractMainFrame#getActiveMainFrame()} 
	 */
	@Deprecated
	public static JFrame getActiveWindow() {
		return activeWindow;
	}
}
