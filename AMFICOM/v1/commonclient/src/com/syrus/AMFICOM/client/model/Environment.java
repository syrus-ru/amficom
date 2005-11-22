/*-
 * $Id: Environment.java,v 1.18 2005/11/22 15:04:49 bass Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JFrame;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.util.Log;
import static java.util.logging.Level.*;

/**
 * Класс Environment используется для хранения общей для приложения информации.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2005/11/22 15:04:49 $
 * @module commonclient
 */
public final class Environment {
	private static Dispatcher theDispatcher = new Dispatcher();

	private static List<Window> windows = new ArrayList<Window>();

	private static JFrame activeWindow = null;

	private Environment() {
		assert false;
	}

	public static Dispatcher getDispatcher() {
		return theDispatcher;
	}

	public static void addWindow(final Window window) {
		Log.debugMessage("name: " + window.getName(), FINEST);
		windows.add(window);
	}

	public static void disposeWindow(final Window window) {
		Log.debugMessage("name: " + window.getName(), FINEST);
		windows.remove(window);
		window.dispose();
		checkForExit();
	}

	public static void checkForExit() {
		if (windows.isEmpty()) {
			System.exit(0);
		}
	}

	public static void setActiveWindow(JFrame window) {
		activeWindow = window;
	}

	public static JFrame getActiveWindow() {
		return activeWindow;
	}
}
