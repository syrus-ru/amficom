/*
 * $Id: UgoTabbedPane.java,v 1.19 2006/02/15 12:18:10 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * @author $Author: stas $
 * @version $Revision: 1.19 $, $Date: 2006/02/15 12:18:10 $
 * @module schemeclient
 */

public class UgoTabbedPane extends JPanel {
	private static final long serialVersionUID = -1701913114506241472L;

	protected ApplicationContext aContext;
	protected SchemeMarqueeHandler marqueeHandler;
	protected UgoPanel panel;
	protected UgoToolBar toolBar;
	protected boolean editable;
	
	public UgoTabbedPane() {
		this.marqueeHandler =  new SchemeMarqueeHandler(this);
		setLayout(new BorderLayout());
		add(createPanel(), BorderLayout.CENTER);
		add(createToolBar(), BorderLayout.NORTH);
		
		this.panel.getGraph().addMouseWheelListener(this.marqueeHandler);
	}
	
	public UgoTabbedPane(ApplicationContext aContext) {
		this();
		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		this.panel.getGraph().setContext(aContext);
	}
	
	public ApplicationContext getContext() {
		return this.aContext;
	}
	
	protected JComponent createToolBar() {
		this.toolBar = new UgoToolBar(this);  
		return this.toolBar;
	}
		
	protected JComponent createPanel() {
		this.panel = new UgoPanel(this.aContext);
		this.panel.getGraph().setMarqueeHandler(this.marqueeHandler);
		
		
		
		JScrollPane graphView = new JScrollPane(this.panel.getGraph());
		return graphView;
	}
	
	public SchemeMarqueeHandler getMarqueeHandler() {
		return this.marqueeHandler;
	}

	/**
	 * @return selected UgoPanel
	 */
	public UgoPanel getCurrentPanel() {
		return this.panel;
	}
	
	/**
	 * @return set of UgoPanels
	 */
	public Set<? extends UgoPanel> getAllPanels() {
		return Collections.singleton(this.panel);
	}
	
	/**
	 * @return selected SchemeGraph
	 */
	public SchemeGraph getGraph() {
		UgoPanel p = getCurrentPanel();
		if (p != null)
			return p.getGraph();
		return null;
	}

	public boolean hasUnsavedChanges(UgoPanel p) {
		return false;
	}

	public void setEditable(boolean b) {
		this.editable = b;
		for (UgoPanel p : getAllPanels()) {
			p.getGraph().setEditable(b);
		}
	}
	
	public void setToolBarVisible(boolean b) {
		this.toolBar.setVisible(b);
	}

	public void setGraphChanged(boolean b) {
		getGraph().setGraphChanged(b);
	}
}
