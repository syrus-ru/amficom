/*
 * $Id: CableLinkTypeLayout.java,v 1.2 2005/03/01 07:17:58 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.*;
import java.util.List;

import java.awt.*;

import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.scheme.SchemeUtils;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/01 07:17:58 $
 * @module schemeclient_v1
 */

public class CableLinkTypeLayout implements OperationListener {
	private static final int FIBER_RADIUS = 8;
	private static final int GAP = 10;

	private int radius = 20;
	CableLinkType type;
	private ApplicationContext internalContext = new ApplicationContext();
	private UgoPanel panel;
	private Map mapping = new HashMap();

	// private int nFibers, nModules;

	public CableLinkTypeLayout() {
		internalContext.setDispatcher(new Dispatcher());
		internalContext.getDispatcher().register(this, SchemeNavigateEvent.type);

		panel = new UgoPanel(internalContext);
		panel.getGraph().setGraphEditable(false);
		panel.getGraph().setAntiAliased(true);
	}
	
	public Dispatcher getInternalDispatcher() {
		return internalContext.getDispatcher();
	}

	public void setObject(Object or) {
		this.type = (CableLinkType) or;
		this.mapping.clear();

		int nModules = 8;
		if (type.getCodename().equals("okst8")
				|| type.getCodename().equals("okst16"))
			nModules = 6;

		int tmp = (int) (2 * FIBER_RADIUS * Math.sqrt(Math.round((double) type
				.getCableThreadTypes().size()	/ (double) nModules + 0.499)));
		if (tmp > radius)
			radius = tmp;

		panel.getGraph().removeAll();
		createModules(nModules);
		createFibers(nModules, type.getCableThreadTypes());
	}

	private void createFibers(int nModules, List fibers) {
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
				CableThreadType ctt = (CableThreadType)it.next();
				
				int x = module_center_x
						+ (int) (radius / 2 * (Math.cos(j * inner_angle))) - FIBER_RADIUS;
				int y = module_center_y
						+ (int) (radius / 2 * (Math.sin(j * inner_angle))) - FIBER_RADIUS;
				Rectangle bounds = new Rectangle(x, y, 2 * FIBER_RADIUS,
						2 * FIBER_RADIUS);
				Color c = new Color(ctt.getColor());
				addThreadCell(panel.getGraph(), ctt, bounds,	c);
			}
		}
	}

	private void createModules(int nModules) {
		double angle = 2 * Math.PI / nModules;
		int r1 = radius;
		int r2 = (int) ((radius * nModules) / Math.PI);
		if (r2 < (1.415 * r1))
			r2 = (int) (1.415 * r1);

		addCell(panel.getGraph(), "", new Rectangle(GAP - 8, GAP - 8,
				16 + 2 * (r2 + r1), 16 + 2 * (r2 + r1)), Color.LIGHT_GRAY);
		addCell(panel.getGraph(), "", new Rectangle(GAP - 1, GAP - 1,
				2 + 2 * (r2 + r1), 2 + 2 * (r2 + r1)), Color.WHITE);
		addCell(panel.getGraph(), "", new Rectangle(GAP
				+ (int) Math.round(radius * 1.915) + 2, GAP
				+ (int) Math.round(radius * 1.915) + 2, 2 * (r2 - r1), 2 * (r2 - r1)),
				Color.GRAY);
		for (int i = 0; i < nModules; i++) {
			int x = GAP + (int) (r2 * (1 + Math.cos(i * angle)));
			int y = GAP + (int) (r2 * (1 + Math.sin(i * angle)));
			Rectangle bounds = new Rectangle(x, y, 2 * radius, 2 * radius);
			addCell(panel.getGraph(), "", bounds, Color.WHITE);
		}
		panel.setGraphSize(new Dimension(2 * GAP + 2 * (r2 + r1), 2 * GAP + 2 * (r2 + r1)));
	}

	public UgoPanel getPanel() {
		return panel;
	}

	public void operationPerformed(OperationEvent oe) {
		if (oe.getActionCommand().equals(SchemeNavigateEvent.type)) {
			SchemeNavigateEvent ev = (SchemeNavigateEvent) oe;
			if (ev.OTHER_OBJECT_SELECTED) {
				Object[] objs = (Object[]) ev.getSource();
				for (int i = 0; i < objs.length; i++) {
					if (objs[i] instanceof ThreadTypeCell) {
						ThreadTypeCell cell = (ThreadTypeCell)objs[i];
						Color newColor = new Color(cell.getCableThreadType().getColor());
						panel.getGraph().setSelectionCell(cell);
						GraphActions.setObjectBackColor(panel.getGraph(), cell, newColor);
					} else if (objs[i] instanceof EllipseCell) {
						panel.getGraph().clearSelection();
					}
				}
			}
		}
	}

	void addThreadCell(SchemeGraph graph, CableThreadType threadType,
			Rectangle bounds, Color color) {
		String name;

		try {
			int num = ResourceUtil.parseNumber(SchemeUtils.parseThreadName(threadType.getName()));
			name = String.valueOf(num);
		} 
		catch (Exception ex) {
			name = threadType.getName();
		}

		Map viewMap = new HashMap();
		ThreadTypeCell cell = new ThreadTypeCell(name);
		cell.setCableThreadType(threadType);
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBackground(map, color);
		GraphConstants.setBorderColor(map, Color.BLACK);
		viewMap.put(cell, map);
		graph.getGraphLayoutCache().insert(new Object[] { cell }, viewMap, null,
				null, null);
		mapping.put(threadType, cell);
	}
	
	public Object getCell(CableThreadType ctt) {
		return mapping.get(ctt);
	}

	void addCell(SchemeGraph graph, Object userObject, Rectangle bounds,
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