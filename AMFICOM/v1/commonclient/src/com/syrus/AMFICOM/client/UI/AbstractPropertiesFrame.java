/*-
 * $Id: AbstractPropertiesFrame.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module commonclient_v1
 */

public abstract class AbstractPropertiesFrame extends JInternalFrame implements ChangeListener {
	protected AbstractEventHandler eventHandler;
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
	
	public void setEventhandler(AbstractEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.eventHandler != null) {
			this.eventHandler.setContext(aContext);
		}
	}
	
	public void setVisualManager(VisualManager manager) {
		if (this.editor != null) {
			this.editor.removeChangeListener(this);
		}
		JComponent comp = this.emptyPane;
		this.editor = null;
		if (manager != null) {
			this.editor = getEditor(manager);
			if (this.editor != null) {
				comp = this.editor.getGUI();
				this.editor.addChangeListener(this);
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

	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	protected abstract StorableObjectEditor getEditor(VisualManager manager);
	
	public StorableObjectEditor getCurrentEditor() {
		return this.editor;
	}
}

