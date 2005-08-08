/*-
 * $Id: CableLinkTypeLayout.java,v 1.8 2005/08/08 11:58:06 arseniy Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.ThreadTypeCell;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.CableThreadTypeWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.WrapperComparator;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.8 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
 */

public class CableLinkTypeLayout extends DefaultStorableObjectEditor implements PropertyChangeListener {
	protected CableLinkType type;
	ApplicationContext aContext;
	
	private static final int FIBER_RADIUS = 8;
	private static final int GAP = 10;
	private int radius = 20;
	
	private ApplicationContext internalContext = new ApplicationContext();
	private UgoTabbedPane panel;
//	private Map mapping = new HashMap();
	protected JScrollPane scrollPane;

	public CableLinkTypeLayout() {
		this.internalContext.setDispatcher(new Dispatcher());
		this.internalContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);

		this.panel = new UgoTabbedPane(this.internalContext);
		this.panel.getGraph().setGraphEditable(false);
		this.panel.getGraph().setAntiAliased(true);
		this.scrollPane = new JScrollPane(this.panel.getGraph());
	}

	public void setObject(Object or) {
		this.type = (CableLinkType) or;
//		this.mapping.clear();
		GraphActions.clearGraph(this.panel.getGraph());

		if (this.type != null) {
		// TODO ����������� � ������ �������
			int nModules = 6;
			if (this.type.getCodename().equals("okst8")
					|| this.type.getCodename().equals("okst16"))
				nModules = 6;

			List ctts = getSortedThreadTypes();
			int tmp = (int) (2 * FIBER_RADIUS * Math.sqrt(Math.round((double) 
					ctts.size() / (double) nModules + 0.499)));
			if (tmp > this.radius)
				this.radius = tmp;

			this.panel.getGraph().removeAll();
			createModules(nModules);
			createFibers(nModules, ctts);
		}
	}
	
	public Object getObject() {
		return this.type;
	}
	
	public void commitChanges() {
		// TODO Auto-generated method stub
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	private List<CableThreadType> getSortedThreadTypes() {
		List<CableThreadType> threads = new LinkedList<CableThreadType>(this.type.getCableThreadTypes(false));
		Collections.sort(threads, new WrapperComparator<CableThreadType>(CableThreadTypeWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_CODENAME));
		return threads;
	}

	private void createFibers(int nModules, final Collection fibers) {
		int nFibers = fibers.size();
		int moduleFibers = nFibers / nModules;
		int additionalFibers = nFibers - (nModules * moduleFibers);

		double angle = 2 * Math.PI / nModules;
		double inner_angle = 2 * Math.PI
				/ (moduleFibers + (additionalFibers == 0 ? 0 : 1));
		int r1 = this.radius;
		int r2 = (int) ((this.radius * nModules) / Math.PI);
		if (r2 < (1.415 * r1))
			r2 = (int) (1.415 * r1);

		Iterator it = fibers.iterator();
		for (int i = 0; i < nModules; i++) {
			int module_center_x = GAP + this.radius + (int) (r2 * (1 + Math.cos(i * angle)));
			int module_center_y = GAP + this.radius + (int) (r2 * (1 + Math.sin(i * angle)));
			for (int j = 0; j < (i < additionalFibers ? moduleFibers + 1 : moduleFibers); j++) {
				CableThreadType ctt = (CableThreadType)it.next();
				
				int x = module_center_x
						+ (int) (this.radius / 2 * (Math.cos(j * inner_angle))) - FIBER_RADIUS;
				int y = module_center_y
						+ (int) (this.radius / 2 * (Math.sin(j * inner_angle))) - FIBER_RADIUS;
				Rectangle bounds = new Rectangle(x, y, 2 * FIBER_RADIUS,
						2 * FIBER_RADIUS);
				Color c = new Color(ctt.getColor());
				addThreadCell(this.panel.getGraph(), ctt, bounds,	c);
			}
		}
	}

	private void createModules(int nModules) {
		double angle = 2 * Math.PI / nModules;
		int r1 = this.radius;
		int r2 = (int) ((this.radius * nModules) / Math.PI);
		if (r2 < (1.415 * r1))
			r2 = (int) (1.415 * r1);

		addCell(this.panel.getGraph(), "", new Rectangle(GAP - 8, GAP - 8, //$NON-NLS-1$
				16 + 2 * (r2 + r1), 16 + 2 * (r2 + r1)), Color.LIGHT_GRAY);
		addCell(this.panel.getGraph(), "", new Rectangle(GAP - 1, GAP - 1, //$NON-NLS-1$
				2 + 2 * (r2 + r1), 2 + 2 * (r2 + r1)), Color.WHITE);
		addCell(this.panel.getGraph(), "", new Rectangle(GAP //$NON-NLS-1$
				+ (int) Math.round(this.radius * 1.915) + 2, GAP
				+ (int) Math.round(this.radius * 1.915) + 2, 2 * (r2 - r1), 2 * (r2 - r1)),
				Color.GRAY);
		for (int i = 0; i < nModules; i++) {
			int x = GAP + (int) (r2 * (1 + Math.cos(i * angle)));
			int y = GAP + (int) (r2 * (1 + Math.sin(i * angle)));
			Rectangle bounds = new Rectangle(x, y, 2 * this.radius, 2 * this.radius);
			addCell(this.panel.getGraph(), "", bounds, Color.WHITE); //$NON-NLS-1$
		}
		this.panel.getCurrentPanel().setGraphSize(new Dimension(2 * GAP + 2 * (r2 + r1), 2 * GAP + 2 * (r2 + r1)));
	}

	public JComponent getGUI() {
		return this.scrollPane;
	}

	public void propertyChange(PropertyChangeEvent oe) {
		if (oe.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent) oe;
			if (ev.isSelected(ObjectSelectedEvent.OTHER_OBJECT)) {
				Object obj = ev.getSelectedObject();
				if (obj instanceof ThreadTypeCell) {
					ThreadTypeCell cell = (ThreadTypeCell)obj;
					Color newColor = new Color(cell.getCableThreadType().getColor());
					this.panel.getGraph().setSelectionCell(cell);
					GraphActions.setObjectBackColor(this.panel.getGraph(), cell, newColor);
				} else if (obj instanceof EllipseCell) {
					this.panel.getGraph().clearSelection();
				}
			}
		}
	}

	private void addThreadCell(SchemeGraph graph, CableThreadType threadType,
			Rectangle bounds, Color color) {
		String name = threadType.getName();

//		try {
//			int num = ResourceUtil.parseNumber(SchemeUtils.parseThreadName(threadType.getName()));
//			name = String.valueOf(num);
//		} 
//		catch (Exception ex) {
//			name = threadType.getName();
//		}

		Map viewMap = new HashMap();
		ThreadTypeCell cell = ThreadTypeCell.createInstance(name, bounds, color, viewMap, threadType);
		graph.getGraphLayoutCache().insert(new Object[] { cell }, viewMap, null,
				null, null);
//		mapping.put(threadType, cell);
	}
	
//	public ThreadTypeCell getCell(CableThreadType ctt) {
//		return (ThreadTypeCell)mapping.get(ctt);
//	}

	private void addCell(SchemeGraph graph, Object userObject, Rectangle bounds,
			Color color) {
		Map<EllipseCell, Map> viewMap = new HashMap<EllipseCell, Map>();
		EllipseCell cell = new EllipseCell(userObject);
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBackground(map, color);
		GraphConstants.setBorderColor(map, Color.BLACK);
		viewMap.put(cell, map);
		graph.getGraphLayoutCache().insert(new Object[] { cell }, viewMap, null,
				null, null);
	}
}
