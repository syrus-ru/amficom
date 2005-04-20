/*
 * $Id: AbstractPropertiesFrame.java,v 1.3 2005/04/20 12:00:11 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/04/20 12:00:11 $
 * @module generalclient_v1
 */

public abstract class AbstractPropertiesFrame extends JInternalFrame implements ChangeListener {
	protected StorableObjectEditor editor;
	protected JComponent emptyPane;
	
	protected AbstractPropertiesFrame(String title) {
		super(title);
		
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit() throws Exception {
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/general.gif")));

		this.getContentPane().setLayout(new BorderLayout());
		
		this.emptyPane = new JPanel();
		emptyPane.setBackground(Color.WHITE);
		setPropertiesPane(emptyPane);
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
		if (this.editor != null) {
			this.editor.removeChangeListener(this);
		}
		JComponent comp = emptyPane;
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
		this.getContentPane().removeAll();
		this.getContentPane().add(propertiesPane, BorderLayout.CENTER);
		this.updateUI();
	}
	
	public void stateChanged(ChangeEvent e) { 
		// should be overriden by children 
	}
	protected abstract StorableObjectEditor getEditor(VisualManager manager);
}
