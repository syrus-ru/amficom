/*
 * $Id: AbstractPropertiesFrame.java,v 1.6 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public abstract class AbstractPropertiesFrame extends JInternalFrame 
 implements OperationListener {
	ApplicationContext aContext;
	Dispatcher dispatcher;
	JScrollPane scrollPane;
	
	protected StorableObjectEditor editor;
	protected JComponent emptyPane;
	
	protected AbstractPropertiesFrame(String title, ApplicationContext aContext) {
		super(title);
		
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setContext(aContext);
	}
	
	private void jbInit() throws Exception {
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/general.gif")));

		this.scrollPane = new JScrollPane();
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		this.emptyPane = new JPanel();
		emptyPane.setBackground(Color.WHITE);
		setPropertiesPane(emptyPane);
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		
		if (this.dispatcher != null) {
			this.dispatcher.unregister(this, ObjectSelectedEvent.TYPE);
		}
		this.dispatcher = aContext.getDispatcher();
		this.dispatcher.register(this, ObjectSelectedEvent.TYPE);
	}
	
	public void doDefaultCloseAction() {
		if (isMaximum())
			try {
				setMaximum(false);
			} catch (java.beans.PropertyVetoException ex) {
				// ignore
			}
		super.doDefaultCloseAction();
	}
	
	public void setVisualManager(VisualManager manager) {
//		if (this.editor != null)
//			this.editor.commitChanges();
		JComponent comp = emptyPane;
		this.editor = null;
		if (manager != null) {
			this.editor = getEditor(manager);
			comp = this.editor.getGUI();
		}
		setPropertiesPane(comp);
	}
	
	protected void setPropertiesPane(JComponent propertiesPane) {
		scrollPane.getViewport().removeAll();
		scrollPane.getViewport().add(propertiesPane);
	}
	
	public void operationPerformed(OperationEvent e) {
		if (e.getActionCommand().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent event = (ObjectSelectedEvent) e;
			
			setVisualManager(event.getVisualManager());				
			if (this.editor != null)
				this.editor.setObject(event.getSelectedObject());	
		}
	}
	
	protected abstract StorableObjectEditor getEditor(VisualManager manager);
	
//	protected PropertiesMananager getPropertiesPaneManager() {
//		PropertiesMananager manager = null;
//		try {
//			final String methodName1 = "getPropertyManagerClassName";
//			try {
//				Class clazz = Class.forName((String) (controller.getClass().getMethod(
//						methodName1, new Class[0]).invoke(controller.getClass(),
//						new Object[0])));
//				final String methodName2 = "getInstance";
//				try {
//					Method method = clazz.getMethod(methodName2, new Class[0]); 
//					manager = (PropertiesMananager) (method.invoke(clazz, new Object[0]));
//				} catch (NoSuchMethodException nsme) {
//					System.err.println("WARNING: " + clazz.getName() + '.' + methodName2 + "() not found.");
//				}
//			} catch (NoSuchMethodException nsme) {
//				System.err.println("WARNING: " + controller.getClass().getName() + '.' + methodName1 + "() not found.");
//			}
//		} catch (InvocationTargetException ite) {
//			ite.printStackTrace();
//			ite.getTargetException().printStackTrace();
//		} catch (Throwable t) {
//			t.printStackTrace();
//		}
//		return manager;
//	}
}
