/*-
* $Id: Launcher.java,v 1.4 2005/09/14 09:08:27 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.launcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.util.Log;


/**
 * local implementation of Винтилйатар
 * @version $Revision: 1.4 $, $Date: 2005/09/14 09:08:27 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class Launcher {

	private static final int CANNOT_RUN_EXIT = -1;
	
	private static final String HANDLER_PROP_NAME = "sun.awt.exception.handler";
	
	private Launcher() {
		// singleton
		assert false;
	}	
	
	private static void addAWTHandler() {
		String property = System.getProperty(HANDLER_PROP_NAME);
		if (property == null) {
			System.setProperty(HANDLER_PROP_NAME, DefaultThrowableHandler.class.getName()); 
		}
	}
	
	public static void launchApplicationClass(final Class applicationClass) {
		try {
			addAWTHandler();
			Constructor declaredConstructor = applicationClass.getDeclaredConstructor(new Class[] {});			
			declaredConstructor.newInstance(new Object[] {});
		} catch (final SecurityException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ ", caught a SecurityException, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ " doen't contains simple public constuctor, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ " doen't contains simple public constuctor, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final InstantiationException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ ", caught a InstantiationException, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ ", caught a IllegalAccessException, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final InvocationTargetException e) {
			// too unlikely 
			new Launcher.DefaultThrowableHandler(LangModelGeneral.getString("Error")).handle(e.getCause());
		} 
	}
	
	
	public static class DefaultThrowableHandler {
		
		private final String title;
		
		public DefaultThrowableHandler(final String title) {
			this.title = title;
		}
		
		/**
		 * <p><b>Clients must never explicitly call this method. Only for AWT Thread purposes</b></p>
		 */
		public DefaultThrowableHandler() {
			this(LangModelGeneral.getString("Error.AWTThread"));
		}
		
		public void handle(final Throwable thrown) {
			thrown.printStackTrace();
			
			final String msg = "AMFICOMAWTHandler.handle | unhandled exception " 
				+ thrown.getClass().getName() + ", exit";
			
			System.err.println(msg);	
			
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			Log.errorException(thrown);
			
			final String text = "<html>" 
				+ LangModelGeneral.getString("Error.GetUncatchedException") + ":<br>"
				+ thrown.getClass().getSimpleName() + " : " + thrown.getMessage() + "<br>"
				+ "<br><br>" + LangModelGeneral.getString("Message.Information.ApplicationWillBeTerminated")
				+ "</html>";
			JOptionPane.showMessageDialog(null, text, this.title, JOptionPane.ERROR_MESSAGE);
			System.exit(CANNOT_RUN_EXIT);
		}
	}
}

