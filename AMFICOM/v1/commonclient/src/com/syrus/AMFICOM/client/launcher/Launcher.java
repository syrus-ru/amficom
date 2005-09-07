/*-
* $Id: Launcher.java,v 1.3 2005/09/07 07:09:02 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/09/07 07:09:02 $
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
			System.setProperty(HANDLER_PROP_NAME, MyAWTHandler.class.getName()); 
		}
	}
	
	public static void launchApplicationClass(final Class applicationClass) {
		try {
			addAWTHandler();
			Constructor declaredConstructor = applicationClass.getDeclaredConstructor(new Class[] {});			
			declaredConstructor.newInstance(new Object[] {});
		} catch (SecurityException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ ", caught a SecurityException, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ " doen't contains simple public constuctor, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ " doen't contains simple public constuctor, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (InstantiationException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ ", caught a InstantiationException, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ ", caught a IllegalAccessException, exit";
			System.err.println(msg);
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ ", caught a " + cause.getClass().getName() + " during invocation, exit";
			
			System.err.println(msg);	
			
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			Log.errorException(cause);
			
			final String text = "<html>" 
				+ LangModelGeneral.getString("Error.GetUncatchedException") + ":<br>"
				+ cause.getClass().getSimpleName() + " : " + cause.getMessage() + "<br>"
				+ "<br><br>" + LangModelGeneral.getString("Message.Information.ApplicationWillBeTerminated")
				+ "</html>";
			JOptionPane.showMessageDialog(null, text, LangModelGeneral.getString("Error"), JOptionPane.ERROR_MESSAGE);
			System.exit(CANNOT_RUN_EXIT);
		} 
	}
	
	
	public static class MyAWTHandler {
		
		public void handle(final Throwable thrown) {
			thrown.printStackTrace();
			
			final String msg = "AMFICOMAWTHandler.handle | unhandled exception in awt thead " 
				+ thrown.getClass().getName() + ", exit";
			
			System.err.println(msg);	
			
			Log.debugMessage(msg, Log.DEBUGLEVEL01);
			Log.errorException(thrown);
			
			final String text = "<html>" 
				+ LangModelGeneral.getString("Error.GetUncatchedException") + ":<br>"
				+ thrown.getClass().getSimpleName() + " : " + thrown.getMessage() + "<br>"
				+ "<br><br>" + LangModelGeneral.getString("Message.Information.ApplicationWillBeTerminated")
				+ "</html>";
			JOptionPane.showMessageDialog(null, text, LangModelGeneral.getString("Error.AWTThread"), JOptionPane.ERROR_MESSAGE);
			System.exit(CANNOT_RUN_EXIT);
		}
	}
}

