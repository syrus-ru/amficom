package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;
import java.util.List;

import java.awt.Color;

import com.jgraph.graph.DefaultEdge;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;

public class AlarmedPathAnimator// extends Thread implements Runnable
{
	boolean alarmState = false;
	int timeInterval = 1000;

	SchemePanel panel;
	public ApplicationContext aContext;

	DefaultEdge[] edges;
	Object[] objects;

	public AlarmedPathAnimator(ApplicationContext aContext,  SchemePanel panel, SchemePath path, PathElement alarmedPE)
	{
		this.panel = panel;
		this.aContext = aContext;

		Object[] cells = findAlarmedObjects(panel, path, alarmedPE);
		System.out.println("found " + cells.length + " cells to repaint");

		ArrayList _edges = new ArrayList();
		ArrayList _objects = new ArrayList();
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultEdge)
				_edges.add(cells[i]);
			else
				_objects.add(cells[i]);
		}

		edges = (DefaultEdge[])_edges.toArray(new DefaultEdge[_edges.size()]);
		objects = _objects.toArray(new Object[_objects.size()]);
	}

/*	public void run()
	{
		Object[] cells = findAlarmedObjects(panel, alarmed_link_id);
		System.out.println("found " + cells.length + " cells to repaint");

		ArrayList _edges = new ArrayList();
		ArrayList _objects = new ArrayList();
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultEdge)
				_edges.add(cells[i]);
			else
				_objects.add(cells[i]);
		}

		DefaultEdge[] edges = (DefaultEdge[])_edges.toArray(new DefaultEdge[_edges.size()]);
		Object[] objects = _objects.toArray(new Object[_objects.size()]);

		flag = true;
		while (flag)
		{
			try
			{
				if (objects.length == 0 && edges.length != 0)
				{
					CellView[] views = panel.getGraph().getGraphLayoutCache().getMapping(edges);
					for (int i = 0; i < views.length; i++)
					{
						((LinkView)views[i]).alarmed = true;
						((LinkView)views[i]).trigger = true;
					}
				}
				Component c1 = panel.getParent();
				c1.paint(c1.getGraphics());

				sleep( timeInterval );

				if (objects.length == 0 && edges.length != 0)
				{
					CellView[] views = panel.getGraph().getGraphLayoutCache().getMapping(edges);
					for (int i = 0; i < views.length; i++)
					{
						((LinkView)views[i]).trigger = false;
					}
				}
				c1.paint(c1.getGraphics());

				sleep( timeInterval );
			}
			catch (Exception e)
			{
				System.out.println("AnimateThread found: " + e);
			}
		}

//		System.out.println("stoppin thread " + hashCode());
	}*/

	public void mark()
	{
		if (objects.length == 0 && edges.length != 0)
			GraphActions.setEdgeColor(panel.getGraph(), edges, Color.red);
		if (objects.length != 0)
			GraphActions.setObjectsForeColor(panel.getGraph(), objects, Color.red);
		panel.repaint();
	}

	public void unmark()
	{
		if (objects.length == 0 && edges.length != 0)
			GraphActions.setEdgeColor(panel.getGraph(), edges, Color.black);
		if (objects.length != 0)
			GraphActions.setObjectsForeColor(panel.getGraph(), objects, Color.black);
		panel.repaint();
	}

	Object[] findAlarmedObjects(SchemePanel panel, SchemePath path, PathElement alarmedPE)
	{
		if (alarmedPE != null)
			return new Object[0];

		Object[] cells = panel.getGraph().getAll();
		List edges_to_paint = new LinkedList();

		if (alarmedPE.type().equals(Type.SCHEME_CABLE_LINK))
		{
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DefaultCableLink) {
						SchemeCableLink sl = ((DefaultCableLink)cells[i]).getSchemeCableLink();
						if (sl.equals(alarmedPE.abstractSchemeElement())) {
							edges_to_paint.add(cells[i]);
							sl.alarmed(true);
							return edges_to_paint.toArray();
						}
					}
				}
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DeviceGroup) {
						SchemeElement se = ((DeviceGroup)cells[i]).getSchemeElement();
						if (se.internalScheme() != null) {
							Scheme scheme = se.internalScheme();
							if (SchemeUtils.isSchemeContainsCableLink(scheme, alarmedPE.abstractSchemeElement().id())) {
								for (Enumeration e = ((DeviceGroup)cells[i]).children(); e.hasMoreElements(); )
									edges_to_paint.add(e.nextElement());
								se.alarmed(true);
								se.alarmedPath(path);
								se.alarmedPathElement(alarmedPE);
								return edges_to_paint.toArray();
							}
						}
					}
				}
		}
		else if (alarmedPE.type().equals(Type.SCHEME_LINK))
		{
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DefaultLink) {
						SchemeLink sl = ((DefaultLink)cells[i]).getSchemeLink();
						if (sl.equals(alarmedPE.abstractSchemeElement())) {
							edges_to_paint.add(cells[i]);
							sl.alarmed(true);
							return edges_to_paint.toArray();
						}
					}
				}
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DeviceGroup) {
						SchemeElement se = ((DeviceGroup)cells[i]).getSchemeElement();
						if (se.internalScheme() == null) {
							if (SchemeUtils.isSchemeElementContainsLink(se, alarmedPE.abstractSchemeElement().id())) {
									for (Enumeration e = ((DeviceGroup)cells[i]).children(); e.hasMoreElements(); )
										edges_to_paint.add(e.nextElement());
									se.alarmed(true);
									se.alarmedPath(path);
									se.alarmedPathElement(alarmedPE);
									return edges_to_paint.toArray();
								}
						}
						else {
							Scheme scheme = se.internalScheme();
							if (SchemeUtils.isSchemeContainsLink(scheme, alarmedPE.abstractSchemeElement().id())) {
								for (Enumeration e = ((DeviceGroup)cells[i]).children(); e.hasMoreElements(); )
									edges_to_paint.add(e.nextElement());
								se.alarmed(true);
								se.alarmedPath(path);
								se.alarmedPathElement(alarmedPE);
								return edges_to_paint.toArray();
							}
						}
					}
				}
		}
		else if (alarmedPE.type().equals(Type.SCHEME_ELEMENT))
		{
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DeviceGroup) {
						SchemeElement se = ((DeviceGroup)cells[i]).getSchemeElement();
						if (se.equals(alarmedPE.abstractSchemeElement())) {
							edges_to_paint.add(cells[i]);
							se.alarmed(true);
							return edges_to_paint.toArray();
						}
					}
				}
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DeviceGroup) {
						SchemeElement se = ((DeviceGroup)cells[i]).getSchemeElement();
						if (se.internalScheme() == null) {
							if (SchemeUtils.isSchemeElementContainsElement(se, (SchemeElement)alarmedPE.abstractSchemeElement())) {
									for (Enumeration e = ((DeviceGroup)cells[i]).children(); e.hasMoreElements(); )
										edges_to_paint.add(e.nextElement());
									se.alarmed(true);
									se.alarmedPath(path);
									se.alarmedPathElement(alarmedPE);
									return edges_to_paint.toArray();
								}
						}
						else {
							Scheme scheme = se.internalScheme();
							if (SchemeUtils.isSchemeContainsElement(scheme, (SchemeElement)alarmedPE.abstractSchemeElement())) {
								for (Enumeration e = ((DeviceGroup)cells[i]).children(); e.hasMoreElements(); )
									edges_to_paint.add(e.nextElement());
								se.alarmed(true);
								se.alarmedPath(path);
								se.alarmedPathElement(alarmedPE);
								return edges_to_paint.toArray();
							}
						}
					}
				}
		}
		return edges_to_paint.toArray();
	}
}
