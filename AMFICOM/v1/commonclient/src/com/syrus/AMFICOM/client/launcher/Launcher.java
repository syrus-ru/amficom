/*-
* $Id: Launcher.java,v 1.8 2005/10/30 15:20:24 bass Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.launcher;

import java.awt.BorderLayout;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.util.Log;


/**
 * local implementation of Винтилйатар
 * @version $Revision: 1.8 $, $Date: 2005/10/30 15:20:24 $
 * @author $Author: bass $
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
			assert Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ " doen't contains simple public constuctor, exit";
			System.err.println(msg);
			assert Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ " doen't contains simple public constuctor, exit";
			System.err.println(msg);
			assert Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final InstantiationException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ ", caught a InstantiationException, exit";
			System.err.println(msg);
			assert Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			final String msg = "Launcher.launchApplicationClass | applicationClass " 
				+ applicationClass.getName() 
				+ ", caught a IllegalAccessException, exit";
			System.err.println(msg);
			assert Log.debugMessage(msg, Log.DEBUGLEVEL01);
			System.exit(CANNOT_RUN_EXIT);
		} catch (final InvocationTargetException e) {
			// too unlikely 
			new Launcher.DefaultThrowableHandler(I18N.getString("Error")).handle(e.getCause());
		} 
	}
	
	
	public static void main(String[] args) {
		try {
			throw new Exception("" + Integer.parseInt("a"));
		} catch (Exception e) {
			new DefaultThrowableHandler().handle(e);
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
			this(I18N.getString("Common.Launcher.AWTThread"));
		}
		
		public void handle(final Throwable thrown) {
			thrown.printStackTrace();
			
			final String msg = "AMFICOMAWTHandler.handle | unhandled exception " 
				+ thrown.getClass().getName() + ", exit";
			
			System.err.println(msg);	
			
			assert Log.debugMessage(msg, Log.DEBUGLEVEL01);
			assert Log.errorMessage(thrown);
			
			StackTraceElement[] stackTrace = thrown.getStackTrace();
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(thrown);
			for (final StackTraceElement traceElement : stackTrace) {
				root.add(new DefaultMutableTreeNode(traceElement.toString()));					
			}			
			
			final JTree tree = new JTree(new DefaultTreeModel(root));
			tree.collapsePath(new TreePath(root));
			
			final JPanel panel = new JPanel(new BorderLayout());
			final JLabel label = new JLabel(I18N.getString("Common.Launcher.GetUncatchedException") + ":");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			
			
			panel.add(label, BorderLayout.NORTH);
			
			panel.add(new JScrollPane(tree), BorderLayout.CENTER);

			final JLabel label2 = new JLabel(
				I18N.getString("Common.Launcher.ApplicationWillBeTerminated"));
			label2.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(label2, BorderLayout.SOUTH);

			JOptionPane.showMessageDialog(null, panel, this.title, JOptionPane.ERROR_MESSAGE);
			System.exit(CANNOT_RUN_EXIT);
		}
	}
}

