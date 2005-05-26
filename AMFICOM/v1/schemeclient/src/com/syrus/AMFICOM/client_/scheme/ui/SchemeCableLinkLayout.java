/*-
 * $Id: SchemeCableLinkLayout.java,v 1.1 2005/05/26 07:40:52 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.*;
import java.beans.*;
import java.util.*;

import javax.swing.*;

import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.ThreadCell;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/05/26 07:40:52 $
 * @module schemeclient_v1
 */

public class SchemeCableLinkLayout extends DefaultStorableObjectEditor implements PropertyChangeListener {
	protected SchemeCableLink link;
	ApplicationContext aContext;
	
	private static final int FIBER_RADIUS = 8;
	private static final int GAP = 10;
	private int radius = 20;
	
	private ApplicationContext internalContext = new ApplicationContext();
	private UgoPanel panel;
//	private Map mapping = new HashMap();
	protected JScrollPane scrollPane;

	public SchemeCableLinkLayout() {
		internalContext.setDispatcher(new Dispatcher());
		internalContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);

		panel = new UgoPanel(internalContext);
		panel.getGraph().setGraphEditable(false);
		panel.getGraph().setAntiAliased(true);
		scrollPane = new JScrollPane(panel.getGraph());
	}

	public void setObject(Object or) {
		this.link = (SchemeCableLink) or;
//		this.mapping.clear();
		GraphActions.clearGraph(panel.getGraph());

		if (this.link != null) {
		// TODO разобраться с числом модулей
			int nModules = 8;
			if (link.getAbstractLinkType().getCodename().equals("okst8")
					|| link.getAbstractLinkType().getCodename().equals("okst16"))
				nModules = 6;

			Set scts = link.getSchemeCableThreads();
			int tmp = (int) (2 * FIBER_RADIUS * Math.sqrt(Math.round((double) 
					scts.size() / (double) nModules + 0.499)));
			if (tmp > radius)
				radius = tmp;

			panel.getGraph().removeAll();
			createModules(nModules);
			createFibers(nModules, scts);
		}
	}
	
	public Object getObject() {
		return link;
	}
	
	public void commitChanges() {
		// TODO Auto-generated method stub
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	private void createFibers(int nModules, final Collection fibers) {
		int nFibers = fibers.size();
		int moduleFibers = nFibers / nModules;
		int additionalFibers = nFibers - (nModules * moduleFibers);

		double angle = 2 * Math.PI / nModules;
		double inner_angle = 2 * Math.PI
				/ (moduleFibers + (additionalFibers == 0 ? 0 : 1));
		int r1 = radius;
		int r2 = (int) ((radius * nModules) / Math.PI);
		if (r2 < (1.415 * r1))
			r2 = (int) (1.415 * r1);

		Iterator it = fibers.iterator();
		for (int i = 0; i < nModules; i++) {
			int module_center_x = GAP + radius + (int) (r2 * (1 + Math.cos(i * angle)));
			int module_center_y = GAP + radius + (int) (r2 * (1 + Math.sin(i * angle)));
			for (int j = 0; j < (i < additionalFibers ? moduleFibers + 1 : moduleFibers); j++) {
				SchemeCableThread sct = (SchemeCableThread)it.next();
				
				int x = module_center_x
						+ (int) (radius / 2 * (Math.cos(j * inner_angle))) - FIBER_RADIUS;
				int y = module_center_y
						+ (int) (radius / 2 * (Math.sin(j * inner_angle))) - FIBER_RADIUS;
				Rectangle bounds = new Rectangle(x, y, 2 * FIBER_RADIUS,
						2 * FIBER_RADIUS);
				Color c = new Color(sct.getCableThreadType().getColor());
				addThreadCell(panel.getGraph(), sct, bounds,	c);
			}
		}
	}

	private void createModules(int nModules) {
		double angle = 2 * Math.PI / nModules;
		int r1 = radius;
		int r2 = (int) ((radius * nModules) / Math.PI);
		if (r2 < (1.415 * r1))
			r2 = (int) (1.415 * r1);

		addCell(panel.getGraph(), "", new Rectangle(GAP - 8, GAP - 8, //$NON-NLS-1$
				16 + 2 * (r2 + r1), 16 + 2 * (r2 + r1)), Color.LIGHT_GRAY);
		addCell(panel.getGraph(), "", new Rectangle(GAP - 1, GAP - 1, //$NON-NLS-1$
				2 + 2 * (r2 + r1), 2 + 2 * (r2 + r1)), Color.WHITE);
		addCell(panel.getGraph(), "", new Rectangle(GAP //$NON-NLS-1$
				+ (int) Math.round(radius * 1.915) + 2, GAP
				+ (int) Math.round(radius * 1.915) + 2, 2 * (r2 - r1), 2 * (r2 - r1)),
				Color.GRAY);
		for (int i = 0; i < nModules; i++) {
			int x = GAP + (int) (r2 * (1 + Math.cos(i * angle)));
			int y = GAP + (int) (r2 * (1 + Math.sin(i * angle)));
			Rectangle bounds = new Rectangle(x, y, 2 * radius, 2 * radius);
			addCell(panel.getGraph(), "", bounds, Color.WHITE); //$NON-NLS-1$
		}
		panel.setGraphSize(new Dimension(2 * GAP + 2 * (r2 + r1), 2 * GAP + 2 * (r2 + r1)));
	}

	public JComponent getGUI() {
		return scrollPane;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent)evt;
			if (ev.isSelected(ObjectSelectedEvent.OTHER_OBJECT)) {
				Object[] objs = (Object[]) ev.getSource();
				for (int i = 0; i < objs.length; i++) {
					if (objs[i] instanceof ThreadCell) {
						ThreadCell cell = (ThreadCell)objs[i];
						Color newColor = new Color(cell.getSchemeCableThread().getCableThreadType().getColor());
						panel.getGraph().setSelectionCell(cell);
						GraphActions.setObjectBackColor(panel.getGraph(), cell, newColor);
					} else if (objs[i] instanceof EllipseCell) {
						panel.getGraph().clearSelection();
					}
				}
			}
		}
	}

	private void addThreadCell(SchemeGraph graph, SchemeCableThread schemeCableThread,
			Rectangle bounds, Color color) {
		String name;

		try {
			int num = ResourceUtil.parseNumber(SchemeUtils.parseThreadName(schemeCableThread.getName()));
			name = String.valueOf(num);
		} 
		catch (Exception ex) {
			name = schemeCableThread.getName();
		}

		Map viewMap = new HashMap();
		ThreadCell cell = ThreadCell.createInstance(name, bounds, color, viewMap, schemeCableThread);
		graph.getGraphLayoutCache().insert(new Object[] { cell }, viewMap, null,
				null, null);
//		mapping.put(threadType, cell);
	}
	
//	public ThreadTypeCell getCell(CableThreadType ctt) {
//		return (ThreadTypeCell)mapping.get(ctt);
//	}

	private void addCell(SchemeGraph graph, Object userObject, Rectangle bounds,
			Color color) {
		Map viewMap = new HashMap();
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
