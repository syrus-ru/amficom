package com.syrus.AMFICOM.Client.Schematics.UI;

import java.util.*;
import java.util.List;

import java.awt.*;

import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;

public class CableLayout implements OperationListener
{
	private static final int FIBER_RADIUS = 8;
	private static final int GAP = 10;
	private int radius = 20;

	SchemeCableLink link;
	private ApplicationContext internalContext = new ApplicationContext();
	private UgoPanel panel;

//	private int nFibers, nModules;

	public CableLayout(SchemeCableLink link)
	{
		this.link = link;

		internalContext.setDispatcher(new Dispatcher());
		internalContext.getDispatcher().register(this, SchemeNavigateEvent.type);

		panel = new UgoPanel(internalContext);
		panel.getGraph().setGraphEditable(false);
		panel.getGraph().setAntiAliased(true);

//		CableLinkType type = link.cableLinkType();
		int nModules = 8;
//		if (type.codename.equals("okst8") ||
//				type.codename.equals("okst16"))
//			nModules = 6;

		int tmp = (int)(2 * FIBER_RADIUS * Math.sqrt(Math.round((double)link.schemeCableThreads().length / (double)nModules + 0.499)));
		if (tmp > radius)
			radius = tmp;

		createModules(nModules);
		createFibers(nModules, Arrays.asList(link.schemeCableThreads()));
	}

	private void createFibers(int nModules, List fibers)
	{
		int nFibers = fibers.size();
		int moduleFibers = nFibers / nModules;
		int additionalFibers = nFibers - (nModules * moduleFibers);

		double angle = 2 * Math.PI / (double)(nModules);
		double inner_angle = 2 * Math.PI / (double)(moduleFibers + (additionalFibers == 0 ? 0 : 1));
		int r1 = radius;
		int r2 = (int)((radius * nModules) / Math.PI);
		if (r2 < (1.415 * r1))
			r2 = (int)(1.415 * r1);

		Iterator it = fibers.iterator();
		for (int i = 0; i < nModules; i++)
		{
			int module_center_x = GAP + radius + (int)(r2 * (1 + Math.cos(i * angle)));
			int module_center_y = GAP + radius + (int)(r2 * (1 + Math.sin(i * angle)));
			for (int j = 0; j < (i < additionalFibers ? moduleFibers + 1: moduleFibers); j++)
			{
				int x = module_center_x + (int)(radius / 2 * (Math.cos(j * inner_angle))) - FIBER_RADIUS;
				int y = module_center_y + (int)(radius / 2 * (Math.sin(j * inner_angle))) - FIBER_RADIUS;
				Rectangle bounds = new Rectangle(x, y, 2 * FIBER_RADIUS, 2 * FIBER_RADIUS);
				Color c = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
				addThreadCell(panel.getGraph(), (SchemeCableThread)it.next(), bounds, c);
			}
		}
	}

	private void createModules(int nModules)
	{
		double angle = 2 * Math.PI / (double)(nModules);
		int r1 = radius;
		int r2 = (int)((radius * nModules) / Math.PI);
		if (r2 < (1.415 * r1))
			r2 = (int)(1.415 * r1);

		addCell(panel.getGraph(), "",
						new Rectangle(GAP - 8,
													GAP - 8,
													16 + 2 * (r2 + r1),
													16 + 2 * (r2 + r1)),
						Color.LIGHT_GRAY);
		addCell(panel.getGraph(), "",
						new Rectangle(GAP - 1,
													GAP - 1,
													2 + 2 * (r2 + r1),
													2 + 2 * (r2 + r1)),
						Color.WHITE);
		addCell(panel.getGraph(), "",
						new Rectangle(GAP + (int) Math.round(radius * 1.915) + 2,
													GAP + (int) Math.round(radius * 1.915) + 2,
													2 * (r2 - r1),
													2 * (r2 - r1)),
						Color.GRAY);
		for (int i = 0; i < nModules; i++)
		{
			int x = GAP + (int)(r2 * (1 + Math.cos(i * angle)));
			int y = GAP + (int)(r2 * (1 + Math.sin(i * angle)));
			Rectangle bounds = new Rectangle(x, y, 2 * radius, 2 * radius);
			addCell(panel.getGraph(), "", bounds, Color.WHITE);
		}
	}

	public UgoPanel getPanel()
	{
		return panel;
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent)oe;
			if (ev.OTHER_OBJECT_SELECTED)
			{
				Object[] objs = (Object[])ev.getSource();
				for (int i = 0; i < objs.length; i++)
				{
					if (objs[i] instanceof ThreadCell)
					{

					}
					else if (objs[i] instanceof EllipseCell)
					{
						EllipseCell cell = (EllipseCell)objs[i];
						panel.getGraph().clearSelection();
					}
				}
			}
		}
	}

	void addThreadCell(SchemeGraph graph, SchemeCableThread thread, Rectangle bounds, Color color)
	{
		String name;

		try {
			int num = ResourceUtil.parseNumber(SchemeUtils.parseThreadName(thread.name()));
			name = String.valueOf(num);
		}
		catch (Exception ex) {
			name = "";
		}

		Map viewMap = new HashMap();
		ThreadCell cell = new ThreadCell(name);
		cell.setSchemeCableThread(thread);
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBackground(map, color);
		GraphConstants.setBorderColor(map, Color.BLACK);
		viewMap.put(cell, map);
		graph.getGraphLayoutCache().insert(new Object[] {cell}, viewMap, null, null, null);
	}

	void addCell(SchemeGraph graph, Object userObject, Rectangle bounds, Color color)
	{
		Map viewMap = new HashMap();
		EllipseCell cell = new EllipseCell(userObject);
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBackground(map, color);
		GraphConstants.setBorderColor(map, Color.BLACK);
		viewMap.put(cell, map);
		graph.getGraphLayoutCache().insert(new Object[] {cell}, viewMap, null, null, null);
	}
}