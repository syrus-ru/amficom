/*
 * $Id: UgoTabbedPane.java,v 1.9 2005/08/01 07:52:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
 */

public class UgoTabbedPane extends JPanel {
	protected ApplicationContext aContext;
	protected SchemeMarqueeHandler marqueeHandler;
	protected UgoPanel panel;
	protected UgoToolBar toolBar;
	protected boolean editable;
	
	public UgoTabbedPane() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public UgoTabbedPane(ApplicationContext aContext) {
		this();
		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		panel.getGraph().setContext(aContext);
	}
	
	public ApplicationContext getContext() {
		return aContext;
	}
	
	private void jbInit() throws Exception {
		marqueeHandler =  new SchemeMarqueeHandler(this);
		setLayout(new BorderLayout());
		add(createPanel(), BorderLayout.CENTER);
		add(createToolBar(), BorderLayout.NORTH);
	}
	
	protected JComponent createToolBar() {
		this.toolBar = new UgoToolBar(this);  
		return toolBar;
	}
		
	protected JComponent createPanel() {
		panel = new UgoPanel(aContext);
		panel.getGraph().setMarqueeHandler(marqueeHandler);
		JScrollPane graphView = new JScrollPane(panel.getGraph());
		return graphView;
	}
	
	public SchemeMarqueeHandler getMarqueeHandler() {
		return marqueeHandler;
	}

	/**
	 * @return selected UgoPanel
	 */
	public UgoPanel getCurrentPanel() {
		return panel;
	}
	
	/**
	 * @return set of UgoPanels
	 */
	public Set<UgoPanel> getAllPanels() {
		return Collections.singleton(panel);
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

	public boolean removePanel(UgoPanel p) {
		return true;
	}

	public boolean removeAllPanels() {
		return removePanel(panel);
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

	protected void fixImages(SchemeGraph graph) {
		DeviceGroup[] groups = GraphActions.findAllGroups(graph, graph.getRoots());
		for (int i = 0; i < groups.length; i++) {
			Identifier id = groups[i].getElementId();
			try {
				StorableObject object = StorableObjectPool.getStorableObject(id, false);
				switch (groups[i].getType()) {
				case DeviceGroup.SCHEME_ELEMENT:
					SchemeElement se = (SchemeElement)object;
					DeviceCell cell = GraphActions.getMainCell(groups[i]);
					if (cell != null) {
						GraphActions.setText(graph, cell, se.getLabel());
						ImageIcon icon = null;
						if (se.getSymbol() != null)
							icon = new ImageIcon(se.getSymbol().getImage());
						GraphActions.setImage(graph, cell, icon);
					}
					break;
				case DeviceGroup.PROTO_ELEMENT:
					SchemeProtoElement proto = (SchemeProtoElement)object;
					cell = GraphActions.getMainCell(groups[i]);
					if (cell != null) {
						GraphActions.setText(graph, cell, proto.getLabel());
						ImageIcon icon = null;
						if (proto.getSymbol() != null)
							icon = new ImageIcon(proto.getSymbol().getImage());
						GraphActions.setImage(graph, cell, icon);
					}
					break;
				}
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
	}

	/**
	 * @param schemeCellContainer Scheme or SchemeElement or SchemeProtoElement
	 * @param doClone create copy of objects or open themself
	 * @return Map of cloned DefaultGraphCells (oldCell, newCell) if doClone is true, empty map overwise  
	 */
	public Map<DefaultGraphCell, DefaultGraphCell> openSchemeCellContainer(SchemeCellContainer schemeCellContainer, boolean doClone) {
		Map<DefaultGraphCell, DefaultGraphCell> clones = Collections.emptyMap();
		SchemeGraph graph = getGraph();
//		GraphActions.clearGraph(graph);
		if (schemeCellContainer.getUgoCell() != null) {
			clones = getCurrentPanel().insertCell(schemeCellContainer.getUgoCell().getData(), new Point(0, 0), doClone);
			fixImages(graph);
		}
		setGraphChanged(false);
		return clones;
	}
	
	public void setGraphChanged(boolean b) {
		getGraph().setGraphChanged(b);
	}
}
