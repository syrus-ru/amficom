/*-
 * $Id: AbstractPropertiesFrame.java,v 1.4 2005/08/19 12:45:55 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * @author $Author: bob $
 * @version $Revision: 1.4 $, $Date: 2005/08/19 12:45:55 $
 * @module commonclient
 */

public abstract class AbstractPropertiesFrame extends JInternalFrame {
	protected List<AbstractEventHandler> eventHandlers = new LinkedList<AbstractEventHandler>();
	protected StorableObjectEditor editor;
	protected JComponent emptyPane;
	
	protected AbstractPropertiesFrame(String title) {
		super(title);
		this.createUI();
	}
	
	private void createUI() {
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		this.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));

		this.getContentPane().setLayout(new BorderLayout());
		
		this.emptyPane = new JPanel();
		this.emptyPane.setBackground(Color.WHITE);
		setPropertiesPane(this.emptyPane);
	}
	
	public void doDefaultCloseAction() {
		if (isMaximum()) {
			try {
				setMaximum(false);
			} catch (java.beans.PropertyVetoException ex) {
				// ignore
			}
		}
		super.doDefaultCloseAction();
	}
	
	public void addEventhandler(AbstractEventHandler eventHandler) {
		this.eventHandlers.add(eventHandler);
	}
	
	public void setContext(ApplicationContext aContext) {
		for (AbstractEventHandler handler : this.eventHandlers) {
			handler.setContext(aContext);
		}
	}
	
	public void setVisualManager(VisualManager manager) {
		JComponent comp = this.emptyPane;
		this.editor = null;
		if (manager != null) {
			this.editor = getEditor(manager);
			if (this.editor != null) {
				comp = this.editor.getGUI();
			}
		}
		setPropertiesPane(comp);
	}
	
	protected void setPropertiesPane(JComponent propertiesPane) {
		Container contentPane = this.getContentPane();
		contentPane.removeAll();
		contentPane.add(propertiesPane, BorderLayout.CENTER);
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				AbstractPropertiesFrame.this.updateUI();
			}
		});
	}
	
	protected abstract StorableObjectEditor getEditor(VisualManager manager);
	
	public StorableObjectEditor getCurrentEditor() {
		return this.editor;
	}
}

