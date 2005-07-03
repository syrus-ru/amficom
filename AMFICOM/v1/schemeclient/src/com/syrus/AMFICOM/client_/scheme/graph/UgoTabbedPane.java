/*
 * $Id: UgoTabbedPane.java,v 1.6 2005/06/22 10:16:06 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.BorderLayout;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/06/22 10:16:06 $
 * @module schemeclient_v1
 */

public class UgoTabbedPane extends JPanel implements PropertyChangeListener {
	protected ApplicationContext aContext;
	protected SchemeMarqueeHandler marqueeHandler;
	protected UgoPanel panel;
	
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
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(SchemeEvent.TYPE, this);
		}
		if (aContext != null) {
			this.aContext = aContext;
			this.aContext.getDispatcher().addPropertyChangeListener(SchemeEvent.TYPE, this);
			for (Iterator it = getAllPanels().iterator(); it.hasNext();)
				((UgoPanel)it.next()).setContext(aContext);
		}
	}
	
	public ApplicationContext getContext() {
		return aContext;
	}
	private void jbInit() throws Exception {
		marqueeHandler =  new SchemeMarqueeHandler(this);
		setLayout(new BorderLayout());
		add(createPanel(), BorderLayout.CENTER);
	}
	
	protected JComponent createToolBar() {
		return new UgoToolBar(this);
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
	public Set getAllPanels() {
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
	/*	if (p.getGraph().isGraphChanged()) {
			int res = JOptionPane.CANCEL_OPTION;
			if (p.getGraph().getScheme() != null)
				res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
						"Схема \"" + p.getGraph().getScheme().getName()
								+ "\" была изменена. Вы действительно хотите закрыть схему?",
						"Подтверждение", JOptionPane.YES_NO_OPTION);
			else if (p.getGraph().getSchemeElement() != null)
				res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
						"Элемент \"" + p.getGraph().getSchemeElement().getName()
								+ "\" был изменен. Вы действительно хотите закрыть схему?",
						"Подтверждение", JOptionPane.YES_NO_OPTION);

			if (res != JOptionPane.OK_OPTION) {
				//				SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, this, null);
				//				ssc.execute();
				//				if (ssc.ret_code == SchemeSaveCommand.CANCEL)
				return false;
			}
		}*/
		return true;
	}

	public void removeAllPanels() {
		removePanel(panel);
	}

	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent) ae;
			if (see.isType(SchemeEvent.OPEN_SCHEME)) {
				Scheme scheme = (Scheme) see.getObject();
				openScheme(scheme);
			} else if (see.isType(SchemeEvent.OPEN_SCHEMEELEMENT)) {
				SchemeElement schemeElement = (SchemeElement) see.getObject();
				openSchemeElement(schemeElement);
			} else if (see.isType(SchemeEvent.OPEN_PROTOELEMENT)) {
				SchemeProtoElement proto = (SchemeProtoElement) see.getObject();
				openSchemeProtoElement(proto);
			}
		}
	}

	public void openScheme(Scheme sch) {
		UgoPanel p = getCurrentPanel();
		p.getSchemeResource().setScheme(sch);
		p.getSchemeResource().setSchemeElement(null);
		GraphActions.clearGraph(p.getGraph());
//		if (sch.getUgoCell() != null)
//			p.insertCell(sch.getUgoCell().getData(), new Point(0, 0), true);
	}
	
	public void openSchemeElement(SchemeElement se) {
		UgoPanel p = getCurrentPanel();
		p.getSchemeResource().setScheme(null);
		p.getSchemeResource().setSchemeElement(se);
		GraphActions.clearGraph(p.getGraph());
		if (se.getUgoCell() != null)
			p.insertCell(se.getUgoCell().getData(), new Point(0, 0), true);
	}

	public void openSchemeProtoElement(SchemeProtoElement proto) {
		UgoPanel p = getCurrentPanel();
		p.getSchemeResource().setSchemeProtoElement(proto);
		p.getSchemeResource().setSchemeElement(null);
		GraphActions.clearGraph(p.getGraph());
		if (proto.getUgoCell() != null)
			p.insertCell(proto.getUgoCell().getData(), new Point(0, 0), true);
	}
	/*
	public void removeScheme(Scheme sch) {
		for (Iterator it = getAllPanels().iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next();
			SchemeResource res = p.getSchemeResource();
			if (sch.equals(res.getScheme())) {
				res.setScheme(null);
				SchemeGraph graph = p.getGraph();
				GraphActions.clearGraph(graph);
				graph.setGraphChanged(false);
			}
		}
	}
	
	public void removeSchemeElement(SchemeElement se) {
		for (Iterator it = getAllPanels().iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next();
			SchemeResource res = p.getSchemeResource();
			if (se.equals(res.getSchemeElement())) {
				res.setSchemeElement(null);
				SchemeGraph graph = p.getGraph();
				GraphActions.clearGraph(graph);
				graph.setGraphChanged(false);
			}
		}
	}
	
	public void removeSchemeProtoElement(SchemeProtoElement proto) {
		for (Iterator it = getAllPanels().iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next();
			SchemeResource res = p.getSchemeResource();
			if (proto.equals(res.getSchemeProtoElement())) {
				res.setSchemeProtoElement(null);
				SchemeGraph graph = p.getGraph();
				GraphActions.clearGraph(graph);
				graph.setGraphChanged(false);
			}
		}
	}*/
	
	public void setGraphChanged(boolean b) {
		getGraph().setGraphChanged(b);
	}
}



