/*
 * $Id: AbstractPropertiesFrame.java,v 1.2 2005/03/11 16:10:46 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.lang.reflect.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/11 16:10:46 $
 * @module schemeclient_v1
 */

public abstract class AbstractPropertiesFrame extends JInternalFrame 
 implements OperationListener {
	ApplicationContext aContext;
	Dispatcher dispatcher;
	JScrollPane scrollPane;
	
	protected ObjectResourceController controller;
	protected ObjectResourcePropertiesPane panel;
	protected GeneralPanel emptyPane;
	
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
		
		this.emptyPane = new GeneralPanel();
		JComponent pane = this.emptyPane.getGUI();
		pane.setBackground(Color.WHITE);
		setPropertiesPane(pane);
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		
		if (this.dispatcher != null) {
			this.dispatcher.unregister(this, TreeDataSelectionEvent.type);
		}
		this.dispatcher = aContext.getDispatcher();
		this.dispatcher.register(this, TreeDataSelectionEvent.type);
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
	
	public void setObjectResourceController(ObjectResourceController controller) {
		this.controller = controller;

		if (this.controller != null) {
			PropertiesMananager manager = getPropertiesPaneManager();
			if (manager != null)
				this.panel = getPropertiesPane(manager);
			else
				this.panel = emptyPane;
		}
		else
			this.panel = emptyPane;
		setPropertiesPane((JComponent)this.panel);
	}
	
	protected void setObject(Object obj) {
		this.panel.setObject(obj);
	}
	
	protected void setPropertiesPane(JComponent propertiesPane) {
		scrollPane.getViewport().removeAll();
		scrollPane.getViewport().add(propertiesPane);
	}
	
		/**
	 * @param e
	 * @see com.syrus.AMFICOM.Client.General.Event.OperationListener#operationPerformed(com.syrus.AMFICOM.Client.General.Event.OperationEvent)
	 */
	public void operationPerformed(OperationEvent e) {
		if (e.getActionCommand().equals(TreeDataSelectionEvent.type)) {
			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent) e;
			ObjectResourceController controller0 = tdse.getController();

			if (this.controller == null || !this.controller.equals(controller0))
				setObjectResourceController(controller0);

			List data = tdse.getList();
			int n = tdse.getSelectionNumber();
			if (n != -1)
				setObject(data.get(n));
			else
				setObject(null);
		}
	}
	
	protected abstract ObjectResourcePropertiesPane getPropertiesPane(PropertiesMananager manager);
	
	protected PropertiesMananager getPropertiesPaneManager() {
		PropertiesMananager manager = null;
		try {
			final String methodName1 = "getPropertyManagerClassName";
			try {
				Class clazz = Class.forName((String) (controller.getClass().getMethod(
						methodName1, new Class[0]).invoke(controller.getClass(),
						new Object[0])));
				final String methodName2 = "getInstance";
				try {
					Method method = clazz.getMethod(methodName2, new Class[0]); 
					manager = (PropertiesMananager) (method.invoke(clazz, new Object[0]));
				} catch (NoSuchMethodException nsme) {
					System.err.println("WARNING: " + clazz.getName() + '.' + methodName2 + "() not found.");
				}
			} catch (NoSuchMethodException nsme) {
				System.err.println("WARNING: " + controller.getClass().getName() + '.' + methodName1 + "() not found.");
			}
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
			ite.getTargetException().printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return manager;
	}
}
