/*-
 * $Id: Environment.java,v 1.20 2005/11/28 13:18:13 bob Exp $
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
 * @version $Revision: 1.20 $, $Date: 2005/11/28 13:18:13 $
 * @module commonclient
 */
public final class Environment {

	private Environment() {
		assert false;
	}

	/**
	 * @deprecated use {@link AbstractMainFrame#getGlobalDispatcher()} 
	 */
	@Deprecated
	public static Dispatcher getDispatcher() {
		return AbstractMainFrame.getGlobalDispatcher();
	}

	/**
	 * @deprecated use {@link AbstractMainFrame#setActiveMainFrame(AbstractMainFrame)} 
	 */
	@Deprecated
	public static void setActiveWindow(JFrame window) {
		AbstractMainFrame.setActiveMainFrame((AbstractMainFrame) window);
	}

	/**
	 * @deprecated use {@link AbstractMainFrame#getActiveMainFrame()} 
	 */
	@Deprecated
	public static JFrame getActiveWindow() {
		return AbstractMainFrame.getActiveMainFrame();
	}
}
